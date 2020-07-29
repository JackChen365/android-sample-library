package com.cz.android.sample.library.transform;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.cz.android.sample.api.AndroidSampleConstant;
import com.cz.android.sample.api.item.CategoryItem;
import com.cz.android.sample.api.item.RegisterItem;
import com.cz.android.sample.library.create.AndroidSampleTemplateCreator;
import com.cz.android.sample.library.visitor.AnnotationCheckerVisitor;
import com.cz.android.sample.library.visitor.SampleClassVisitor;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import org.apache.commons.io.FileUtils;
import org.gradle.api.Project;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.android.build.api.transform.QualifiedContent.DefaultContentType.CLASSES;

/**
 * Created by cz
 * @date 2020-05-17 11:57
 * @email bingo110@126.com
 */
public class SampleTransform extends Transform {
    private final Project project;

    public SampleTransform(Project project) {
        this.project = project;
    }

    @Override
    public String getName() {
        return "Sample";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return ImmutableSet.of(CLASSES);
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return Sets.immutableEnumSet(QualifiedContent.Scope.PROJECT);
    }

    @Override
    public Set<? super QualifiedContent.Scope> getReferencedScopes() {
        return Sets.immutableEnumSet(QualifiedContent.Scope.PROJECT);
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws IOException {
        if (transformInvocation.isIncremental()) {
            throw new UnsupportedOperationException("Unsupported incremental build!");
        }
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();
        outputProvider.deleteAll();
        Collection<TransformInput> inputs = transformInvocation.getInputs();
        //Copy all the jar and classes to the where they need to...
        for (TransformInput input : inputs) {
            input.getJarInputs().parallelStream().forEach(jarInput -> {
                File dest = outputProvider.getContentLocation(jarInput.getName(), jarInput.getContentTypes(), jarInput.getScopes(), Format.JAR);
                if (dest.exists()) {
                    throw new RuntimeException("Jar file " + jarInput.getName() + " already exists!" +
                            " src: " + jarInput.getFile().getPath() + ", dest: " + dest.getPath());
                }
                try {
                    FileUtils.copyFile(jarInput.getFile(), dest);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        }

        File outputFile=null;
        Map<String, List<String>> configurationClassMap=new HashMap<>();
        List<CategoryItem> categoryList=new ArrayList<>();
        List<RegisterItem> registerList=new ArrayList<>();
        List<String> sampleClassList=new ArrayList<>();
        for (TransformInput input : inputs) {
            Collection<DirectoryInput> directoryInputs = input.getDirectoryInputs();
            if (null==outputFile && null != directoryInputs && !directoryInputs.isEmpty()) {
                DirectoryInput directoryInput = directoryInputs.iterator().next();
                outputFile = outputProvider.getContentLocation(directoryInput.getName(), directoryInput.getContentTypes(), directoryInput.getScopes(), Format.DIRECTORY);
            }
            directoryInputs.forEach(dir -> {
                try {
                    final File file = dir.getFile();
                    if (file.isDirectory()) {
                        Files.walk(file.toPath()).filter(path -> {
                            String name = path.toFile().getName();
                            return name.endsWith(".class") && !name.startsWith("R$") &&
                                    !"R.class".equals(name) && !"BuildConfig.class".equals(name);
                        }).forEach(path -> {
                            File classFile = path.toFile();
                            try {
                                processJavaClassFile(file,classFile,configurationClassMap,categoryList,registerList,sampleClassList);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    File destFolder = outputProvider.getContentLocation(dir.getName(), dir.getContentTypes(), dir.getScopes(), Format.DIRECTORY);
                    FileUtils.copyDirectory(dir.getFile(), destFolder);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        }
        try {
            generateConfigurationClassFile(outputFile,configurationClassMap,categoryList,registerList,sampleClassList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //The debug log.
        System.out.println("The sample plugin >");
        for(Map.Entry<String,List<String>> entry: configurationClassMap.entrySet()){
            String annotation = entry.getKey();
            List<String> classList = entry.getValue();
            System.out.println("----| annotation:"+annotation);
            classList.forEach(className-> System.out.println("--------| class:"+className));
        }
        String classPath=AndroidSampleConstant.CLASS_PACKAGE.replace(".","/")+
                "/"+AndroidSampleConstant.ANDROID_SIMPLE_CLASS_NAME+".class";
        File classPathFile=new File(outputFile,classPath);
        System.out.println("Generated class file:"+classPathFile.getPath());
    }

    private void processJavaClassFile(File classFolder,File file, Map<String,List<String>> configurationMap,
                                      List<CategoryItem> categoryList,List<RegisterItem> registerList,List<String> sampleClassList) throws IOException {
        //The first step: We process context classes include the Application class file.
        byte[] bytes = Files.readAllBytes(file.toPath());
        try {
            //Change the super activity class of this sample.
            processSampleClass(sampleClassList,file,bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Check if the class is the configured class.
        collectConfigurationFile(bytes,configurationMap,categoryList,registerList);
    }

    private void processSampleClass(List<String> sampleClassList, File file, byte[] bytes) throws IOException {
        ClassReader classReader = new ClassReader(bytes);
        ClassWriter classWriter = new ClassWriter(classReader,ClassWriter.COMPUTE_MAXS);
        SampleClassVisitor sampleClassVisitor = new SampleClassVisitor(classWriter);
        classReader.accept(sampleClassVisitor, ClassReader.EXPAND_FRAMES);
        if(sampleClassVisitor.isDemonstrable()){
            String className = sampleClassVisitor.getClassName();
            sampleClassList.add(className);
        }
        byte[] code = classWriter.toByteArray();
        FileOutputStream fos = new FileOutputStream(file.getParentFile().getAbsoluteFile() + File.separator + file.getName());
        fos.write(code);
        fos.close();
    }


    private void collectConfigurationFile(byte[] bytes, Map<String,List<String>> configurationMap,
                                          List<CategoryItem> categoryList,List<RegisterItem> registerList) {
        ClassReader classReader = new ClassReader(bytes);
        ClassWriter classWriter = new ClassWriter(classReader,ClassWriter.COMPUTE_MAXS);
        AnnotationCheckerVisitor configurationVisitor = new AnnotationCheckerVisitor(classWriter);
        classReader.accept(configurationVisitor, ClassReader.EXPAND_FRAMES);

        List<String> annotationList = configurationVisitor.getAnnotationList();
        if(null!=annotationList&&!annotationList.isEmpty()){
            for(String annotation:annotationList){
                List<String> configurationList = configurationMap.get(annotation);
                if(null==configurationList){
                    configurationList=new ArrayList<>();
                    configurationMap.put(annotation,configurationList);
                }
                String className = configurationVisitor.getClassName();
                configurationList.add(className);
            }
            CategoryItem categoryItem = configurationVisitor.getCategoryItem();
            if(null!=categoryItem){
                categoryList.add(categoryItem);
            }
            RegisterItem registerItem = configurationVisitor.getRegisterItem();
            if(null!=registerItem){
                registerList.add(registerItem);
            }
        }
    }

    private void generateConfigurationClassFile(File outputFile, Map<String, List<String>> configurationMap,
                                                List<CategoryItem> categoryList,List<RegisterItem> registerList,
                                                List<String> sampleClassList) throws Exception {
        List<String> functionList = configurationMap.get(AnnotationCheckerVisitor.ANNOTATION_FUNCTION);
        List<String> componentList = configurationMap.get(AnnotationCheckerVisitor.ANNOTATION_COMPONENT);
        List<String> processorList = configurationMap.get(AnnotationCheckerVisitor.ANNOTATION_ACTION_PROCESSOR);
        List<String> testCaseList = configurationMap.get(AnnotationCheckerVisitor.ANNOTATION_TEST_CASE);
        List<String> categoryGeneratorList = configurationMap.get(AnnotationCheckerVisitor.ANNOTATION_CATEGORY_GENERATOR);
        List<String> mainComponentList = configurationMap.get(AnnotationCheckerVisitor.ANNOTATION_MAIN_COMPONENT);

        String categoryGenerator=null;
        if(null!=categoryGeneratorList&&!categoryGeneratorList.isEmpty()){
            categoryGenerator=categoryGeneratorList.get(0);
        }
        String mainComponent=null;
        if(null!=mainComponentList&&!mainComponentList.isEmpty()){
            mainComponent=mainComponentList.get(0);
        }
        String classPath=AndroidSampleConstant.CLASS_PACKAGE.replace(".","/");
        File classPathFile=new File(outputFile,classPath);
        if(!classPathFile.exists()){
            classPathFile.mkdirs();
        }
        AndroidSampleTemplateCreator.create(outputFile,classPath+"/"+AndroidSampleConstant.ANDROID_SIMPLE_CLASS_NAME,
                categoryList,registerList,sampleClassList,functionList, componentList,processorList,testCaseList,categoryGenerator,mainComponent);
    }
}