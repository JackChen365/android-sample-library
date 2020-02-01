package com.cz.android.sample.processor;

import com.cz.android.sample.api.ActionProcessor;
import com.cz.android.sample.api.Category;
import com.cz.android.sample.api.Component;
import com.cz.android.sample.api.Function;
import com.cz.android.sample.api.MainComponent;
import com.cz.android.sample.api.ProjectRepository;
import com.cz.android.sample.api.RefCategory;
import com.cz.android.sample.api.RefRegister;
import com.cz.android.sample.api.Register;
import com.cz.android.sample.api.AndroidSampleConstant;
import com.cz.android.sample.api.TestCase;
import com.cz.android.sample.api.item.CategoryItem;
import com.cz.android.sample.api.item.RegisterItem;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * @author Created by cz
 * @date 2019/4/18 下午4:30
 * @email bingo110@126.com
 * this annotation process is when you build all the code. It will help collect all the sample information automatically
 *
 *
 * Example usage:
 * <pre>
 * @Category(name="动画分组",desc = "如动画转换,自定义动画等")
 * @Register(category = "动画分组",title="示例1",desc = "演示1详情")
 * class Sample1Activity : SampleAppCompatActivity()
 * </pre>
 *
 * @see Category add a new category
 * @see Register register this class as an android sample
 *
 */
@AutoService(Processor.class)
public class AndroidSampleProcessor extends AbstractProcessor {
    public Filer filter;
    public Messager messager;
    public Elements elementUtils;

    public AndroidSampleProcessor() {
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filter=processingEnvironment.getFiler();
        messager=processingEnvironment.getMessager();
        elementUtils=processingEnvironment.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        final Set<String> supportedAnnotationTyped=new HashSet<>();
        supportedAnnotationTyped.add(Register.class.getName());
        supportedAnnotationTyped.add(RefRegister.class.getName());
        supportedAnnotationTyped.add(Category.class.getName());
        supportedAnnotationTyped.add(RefCategory.class.getName());
        supportedAnnotationTyped.add(Component.class.getName());
        supportedAnnotationTyped.add(Function.class.getName());
        supportedAnnotationTyped.add(MainComponent.class.getName());
        supportedAnnotationTyped.add(ActionProcessor.class.getName());
        supportedAnnotationTyped.add(ProjectRepository.class.getName());
        supportedAnnotationTyped.add(TestCase.class.getName());
        return supportedAnnotationTyped;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        messager.printMessage(Diagnostic.Kind.NOTE, "======>start");
        Set<? extends Element> registerElements = roundEnvironment.getElementsAnnotatedWith(Register.class);
        Set<? extends Element> categoryElements = roundEnvironment.getElementsAnnotatedWith(Category.class);
        Set<? extends Element> refRegisterElements = roundEnvironment.getElementsAnnotatedWith(RefRegister.class);
        Set<? extends Element> refCategoryElements = roundEnvironment.getElementsAnnotatedWith(RefCategory.class);

        Set<? extends Element> functionElements = roundEnvironment.getElementsAnnotatedWith(Function.class);
        Set<? extends Element> componentElements = roundEnvironment.getElementsAnnotatedWith(Component.class);
        Set<? extends Element> mainComponentElements = roundEnvironment.getElementsAnnotatedWith(MainComponent.class);
        Set<? extends Element> processorElements = roundEnvironment.getElementsAnnotatedWith(ActionProcessor.class);
        Set<? extends Element> projectRepositoryElements = roundEnvironment.getElementsAnnotatedWith(ProjectRepository.class);
        Set<? extends Element> testElements = roundEnvironment.getElementsAnnotatedWith(TestCase.class);

        //start process all annotations
        messager.printMessage(Diagnostic.Kind.NOTE, "register:"+registerElements.size()+" category:"+categoryElements.size());
        final MethodSpec.Builder methodSpec = MethodSpec.constructorBuilder();
        methodSpec.addModifiers(Modifier.PUBLIC);
        addCategoryItems(categoryElements,refCategoryElements, methodSpec);
        addRegisterItems(registerElements,refRegisterElements, methodSpec);
        addFunctionItems(functionElements, methodSpec);
        addComponentItems(componentElements, methodSpec);
        addProcessorItems(processorElements, methodSpec);
        addTestItems(testElements, methodSpec);

        String sourcePath = createConfigFile(methodSpec,mainComponentElements,projectRepositoryElements);
        //one more thing,output all the source code structure of the project
        generateFileStructure(sourcePath);
        messager.printMessage(Diagnostic.Kind.NOTE, "=======>end");
        return true;
    }

    /**
     * 添加分类注册条目
     */
    private void addCategoryItems(Set<? extends Element> elements,Set<? extends Element> refCategoryElements,MethodSpec.Builder methodSpec) {
        if(!elements.isEmpty()||!refCategoryElements.isEmpty()){
            methodSpec.addCode("// add register category object\n");
        }
        for(Element element:elements){
            //根据注解生成配置信息
            Category category = element.getAnnotation(Category.class);
            if (null != category) {
                messager.printMessage(Diagnostic.Kind.NOTE, "category:"+category.category());
                methodSpec.addStatement(AndroidSampleConstant.CATEGORY_FIELD_NAME+".add(new $T($S,$S,$S,$L))",
                        CategoryItem.class,
                        category.title(),
                        category.desc(),
                        category.category(),
                        category.priority());
            }
        }
        for(Element element:refCategoryElements){
            //根据注解生成配置信息
            RefCategory category = element.getAnnotation(RefCategory.class);
            if (null != category) {
                messager.printMessage(Diagnostic.Kind.NOTE, "category:"+category.category());
                methodSpec.addStatement(AndroidSampleConstant.CATEGORY_FIELD_NAME+".add(new $T($L,$L,$L,$L,$T.REF_TYPE))",
                        CategoryItem.class,
                        category.title(),
                        category.desc(),
                        category.category(),
                        category.priority(),
                        AndroidSampleConstant.class);
            }
        }
    }

    /**
     * 添加注册示例条目
     * @param  elements 当前Annotation元素集
     * @param  methodSpec methodSpec构造器对象
     */
    private void addRegisterItems(Set<? extends Element> elements,Set<? extends Element> refRegisterElements,MethodSpec.Builder methodSpec) {
        if(!elements.isEmpty()||!refRegisterElements.isEmpty()){
            methodSpec.addCode("// add register sample items\n");
        }
        for(Element element:elements){
            final Register registerAnnotation = element.getAnnotation(Register.class);
            methodSpec.addStatement(AndroidSampleConstant.REGISTER_FIELD_NAME+".add(new $T($S,$S,$T.class, $S,$L))",
                    RegisterItem.class,
                    registerAnnotation.title(),
                    registerAnnotation.desc(),
                    element.asType(),
                    registerAnnotation.category(),
                    registerAnnotation.priority());
        }
        for(Element element:refRegisterElements){
            final RefRegister registerAnnotation = element.getAnnotation(RefRegister.class);
            methodSpec.addStatement(AndroidSampleConstant.REGISTER_FIELD_NAME+".add(new $T($L,$L,$T.class,$L,$L,$T.REF_TYPE))",
                    RegisterItem.class,
                    registerAnnotation.title(),
                    registerAnnotation.desc(),
                    element.asType(),
                    registerAnnotation.category(),
                    registerAnnotation.priority(),
                    AndroidSampleConstant.class);
        }
    }

    private void addFunctionItems(Set<? extends Element> componentElements, MethodSpec.Builder methodSpec) {
        if(!componentElements.isEmpty()){
            methodSpec.addCode("// add register function items\n");
        }
        for(Element element:componentElements){
            String className=element.asType().toString();
            methodSpec.addStatement(AndroidSampleConstant.FUNCTION_FIELD_NAME+".add($S)",className);
        }
    }

    private void addComponentItems(Set<? extends Element> componentElements, MethodSpec.Builder methodSpec) {
        if(!componentElements.isEmpty()){
            methodSpec.addCode("// add register component items\n");
        }
        for(Element element:componentElements){
            String className=element.asType().toString();
            methodSpec.addStatement(AndroidSampleConstant.COMPONENT_FIELD_NAME+".add($S)",className);
        }
    }

    private void addProcessorItems(Set<? extends Element> processorElements, MethodSpec.Builder methodSpec) {
        if(!processorElements.isEmpty()){
            methodSpec.addCode("// add register processor items\n");
        }
        for(Element element:processorElements){
            String className=element.asType().toString();
            methodSpec.addStatement(AndroidSampleConstant.PROCESSOR_FIELD_NAME+".add($S)",className);
        }
    }


    /**
     * Add test case item. If you want to open a sample case immediately.
     * @param testElements
     * @param methodSpec
     */
    private void addTestItems(Set<? extends Element> testElements, MethodSpec.Builder methodSpec) {
        if(!testElements.isEmpty()){
            methodSpec.addCode("// add register test classes\n");
        }
        for(Element element:testElements){
            String className=element.asType().toString();
            methodSpec.addStatement(AndroidSampleConstant.TEST_FIELD_NAME+".add($S)",className);
        }
    }

    private String createConfigFile(MethodSpec.Builder methodSpec,Set<? extends Element> mainComponentElements,Set<? extends Element> projectRepositoryElements) {
        ClassName stringItem = ClassName.get(String.class);
        ClassName registerItem = ClassName.get(AndroidSampleConstant.ITEM_CLASS_NAME, "RegisterItem");
        ClassName categoryItem = ClassName.get(AndroidSampleConstant.ITEM_CLASS_NAME, "CategoryItem");
        ClassName list = ClassName.get("java.util", "List");
        ClassName arrayList = ClassName.get("java.util", "ArrayList");

        TypeName registerList = ParameterizedTypeName.get(list, registerItem);
        FieldSpec registerField = FieldSpec.builder(registerList, AndroidSampleConstant.REGISTER_FIELD_NAME)
                .addModifiers(Modifier.FINAL)
                .initializer("new $T<>()", arrayList)
                .build();

        TypeName categoryList = ParameterizedTypeName.get(list, categoryItem);
        FieldSpec categoryField = FieldSpec.builder(categoryList, AndroidSampleConstant.CATEGORY_FIELD_NAME)
                .addModifiers(Modifier.FINAL)
                .initializer("new $T<>()", arrayList)
                .build();

        TypeName functionList = ParameterizedTypeName.get(list, stringItem);
        FieldSpec functionField = FieldSpec.builder(functionList, AndroidSampleConstant.FUNCTION_FIELD_NAME)
                .addModifiers(Modifier.FINAL)
                .initializer("new $T<>()", arrayList)
                .build();

        TypeName componentList = ParameterizedTypeName.get(list, stringItem);
        FieldSpec componentField = FieldSpec.builder(componentList, AndroidSampleConstant.COMPONENT_FIELD_NAME)
                .addModifiers(Modifier.FINAL)
                .initializer("new $T<>()", arrayList)
                .build();

        TypeName processorList = ParameterizedTypeName.get(list, stringItem);
        FieldSpec processorField = FieldSpec.builder(processorList, AndroidSampleConstant.PROCESSOR_FIELD_NAME)
                .addModifiers(Modifier.FINAL)
                .initializer("new $T<>()", arrayList)
                .build();

        TypeName testCaseList = ParameterizedTypeName.get(list, stringItem);
        FieldSpec testCaseField = FieldSpec.builder(testCaseList, AndroidSampleConstant.TEST_FIELD_NAME)
                .addModifiers(Modifier.FINAL)
                .initializer("new $T<>()", arrayList)
                .build();

        FieldSpec mainComponentField=null;
        Iterator<? extends Element> iterator = mainComponentElements.iterator();
        if(iterator.hasNext()){
            Element element = iterator.next();
            String className=element.asType().toString();
            mainComponentField = FieldSpec.builder(String.class, AndroidSampleConstant.MAIN_COMPONENT_FIELD_NAME)
                    .addModifiers(Modifier.FINAL)
                    .initializer("\""+className+"\"")
                    .build();
        }

        FieldSpec repositoryUrlField=null;
        iterator = projectRepositoryElements.iterator();
        if(iterator.hasNext()){
            Element element = iterator.next();
            ProjectRepository projectRepository = element.getAnnotation(ProjectRepository.class);
            repositoryUrlField = FieldSpec.builder(String.class, AndroidSampleConstant.REPOSITORY_URL_FIELD_NAME)
                    .addModifiers(Modifier.FINAL)
                    .initializer("\""+projectRepository.value()+"\"")
                    .build();
        }

        TypeSpec.Builder builder = TypeSpec.classBuilder(AndroidSampleConstant.ANDROID_SIMPLE_CLASS_NAME);
        builder.addModifiers(Modifier.FINAL,Modifier.PUBLIC)
                .addJavadoc("all the classes generated by annotation com.cz.android.sample.library.processor automatically\n" +
                            "@see com.cz.android.sample.com.cz.android.sample.library.processor.AndroidSampleProcessor\n")
                .addMethod(methodSpec.build())
                .addField(registerField)
                .addField(categoryField)
                .addField(functionField)
                .addField(componentField)
                .addField(processorField)
                .addField(testCaseField);
        if(null!=mainComponentField){
            builder.addField(mainComponentField);
        }
        if(null!=repositoryUrlField){
            builder.addField(repositoryUrlField);
        }
        String sourcePath=null;
        TypeSpec sampleClass =builder.build();
        JavaFile javaFile = JavaFile.builder(AndroidSampleConstant.CLASS_PACKAGE, sampleClass).build();
        try {
            JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(AndroidSampleConstant.ANDROID_SIMPLE_CLASS);
            sourcePath = sourceFile.toUri().getPath();
            Writer writer = sourceFile.openWriter();
            try {
                writer.write(javaFile.toString());
            } finally {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sourcePath;
    }

    /**
     * Find a specific file by file name
     * @param file
     * @param fileName
     * @return
     */
    private File findFile(File file,String fileName){
        if(fileName.equals(file.getName())){
            return file;
        } else {
            File parentFile = file.getParentFile();
            return findFile(parentFile,fileName);
        }
    }

    /**
     * generate project file structure
     * all the information is more like:
     * com/xxx/xxx
     * |--a.java
     * |--b.java
     *
     */
    private void generateFileStructure(String sourcePath) {
        if(null!=sourcePath){
            final File file=new File(sourcePath);
            File buildFile = findFile(file, "build");
            File parentFile = buildFile.getParentFile();
            File[] findFileArray = parentFile.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File f, String s) {
                    return "src".equals(s);
                }
            });
            if(0 < findFileArray.length){
                File srcFile = findFileArray[0];
                File javaSourceFile=new File(srcFile,"main/java");
                if(javaSourceFile.exists()){
                    generateFileStructure(javaSourceFile);
                }
            }
        }
    }

    private void generateFileStructure(File javaSourceFile){
        ClassName fileItem = ClassName.get(File.class);
        ClassName list = ClassName.get("java.util", "List");
        ClassName arrayList = ClassName.get("java.util", "ArrayList");
        TypeName fileList = ParameterizedTypeName.get(list, fileItem);


        final MethodSpec.Builder methodSpec = MethodSpec.constructorBuilder();
        methodSpec.addModifiers(Modifier.PUBLIC);
        FieldSpec fileListField = FieldSpec.builder(fileList, AndroidSampleConstant.PROJECT_FILE_LIST_FIELD_NAME)
                .addModifiers(Modifier.FINAL)
                .initializer("new $T<>()", arrayList)
                .build();

        String javaSourcePath = javaSourceFile.getAbsolutePath();
        Queue<File> fileQueue=new ArrayDeque<>();
        fileQueue.add(javaSourceFile);
        while(!fileQueue.isEmpty()){
            File f = fileQueue.poll();
            if(!f.isDirectory()){
                String sourcePath = f.getAbsolutePath();
                methodSpec.addStatement(AndroidSampleConstant.PROJECT_FILE_LIST_FIELD_NAME+".add(new $T($S))", File.class,sourcePath.substring(javaSourcePath.length()+1));
            } else {
                for(File subFile:f.listFiles()){
                    if(!".".equals(subFile.getName())&& !"..".equals(subFile.getName())){
                        fileQueue.add(subFile);
                    }
                }
            }
        }
        TypeSpec sampleClass = TypeSpec.classBuilder(AndroidSampleConstant.PROJECT_FILE_CLASS_NAME)
                .addModifiers(Modifier.FINAL,Modifier.PUBLIC)
                .addJavadoc("all the classes generated by annotation com.cz.android.sample.library.processor automatically\n" +
                        "@see com.cz.android.sample.com.cz.android.sample.library.processor.AndroidSampleProcessor\n")
                .addMethod(methodSpec.build())
                .addField(fileListField).build();

        JavaFile javaFile = JavaFile.builder(AndroidSampleConstant.CLASS_PACKAGE, sampleClass).build();
        Writer writer=null;
        try {
            JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(AndroidSampleConstant.PROJECT_FILE_CLASS);
            writer = sourceFile.openWriter();
            writer.write(javaFile.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(null!=writer){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}