package com.cz.android.sample.library;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.api.AndroidSourceDirectorySet;
import com.android.build.gradle.api.AndroidSourceSet;
import com.android.build.gradle.api.ApplicationVariant;
import com.cz.android.sample.library.transform.SampleTransform;

import org.apache.commons.io.FileUtils;
import org.gradle.api.DomainObjectSet;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.PluginContainer;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Created by cz
 * @date 2020-05-17 16:28
 * @email bingo110@126.com
 */
public class SamplePlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        if(isAndroidProject(project)){
            //Merge all the documents inside the project.
            File buildFile = project.getBuildDir();
            File sampleBuildFile=new File(buildFile,"sample");
            File documentBuildFile=new File(sampleBuildFile,"document");
            mergeDocument(project,documentBuildFile);

            //We copy all the source file into the assets folder.
            AppExtension appExtension = project.getExtensions().getByType(AppExtension.class);
            DomainObjectSet<ApplicationVariant> applicationVariants = appExtension.getApplicationVariants();
            applicationVariants.forEach(variant->{
                variant.getApplicationId();
            });
            NamedDomainObjectContainer<AndroidSourceSet> sourceSets = appExtension.getSourceSets();
            sourceSets.forEach(sourceSet->{
                AndroidSourceDirectorySet java = sourceSet.getJava();
                Set<File> srcDirs = java.getSrcDirs();
                AndroidSourceDirectorySet assets = sourceSet.getAssets();
                assets.srcDir(sampleBuildFile);
                assets.srcDirs(srcDirs);
            });
            //Register the gradle transform.
            SampleTransform sampleTransform = new SampleTransform(project);
            appExtension.registerTransform(sampleTransform);
        }
    }

    /**
     * Collect all the documents inside this project.
     * @param project
     * @param documentBuildFile
     */
    private void mergeDocument(Project project,File documentBuildFile) {
        try {
            FileUtils.deleteDirectory(documentBuildFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!documentBuildFile.exists()){
            documentBuildFile.mkdir();
        }
        //Collect all the documents inside the project.
        Project rootProject = project.getRootProject();
        File projectDir = rootProject.getProjectDir();
        String projectAbsolutePath = projectDir.getAbsolutePath();

        List<File> documentList = collectDocuments(project);
        for(File file:documentList){
            try {
                String absolutePath = file.getAbsolutePath();
                String fileRelativePath = absolutePath.substring(projectAbsolutePath.length() + 1);

                File destFile=new File(documentBuildFile,fileRelativePath);
                File destFolder = destFile.getParentFile();
                if(!destFolder.exists()){
                    destFolder.mkdir();
                }
                FileUtils.copyFile(file, destFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<File> collectDocuments(Project project) {
        Project rootProject = project.getRootProject();
        File projectDir = rootProject.getProjectDir();
        List<String> ignoreFileList=new ArrayList<>();
        final List<File> documentList=new ArrayList<>();
        ignoreFileList.add(".idea");
        ignoreFileList.add(".gradle");
        ignoreFileList.add("build");
        try {
            Files.walkFileTree(projectDir.toPath(), new SimpleFileVisitor<Path>(){
                @Override
                public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attributes) throws IOException {
                    File file = path.toFile();
                    String name = file.getName();
                    if(ignoreFileList.contains(name)){
                        return FileVisitResult.SKIP_SUBTREE;
                    } else {
                        return FileVisitResult.CONTINUE;
                    }
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs){
                    if(!attrs.isDirectory()){
                        File f = file.toFile();
                        String name = f.getName();
                        if(name.endsWith(".md")||name.endsWith(".MD")){
                            documentList.add(f);
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return documentList;
    }

    public static boolean isAndroidProject(Project project) {
        PluginContainer plugins = project.getPlugins();
        return null!=plugins.findPlugin("com.android.application");
    }
}
