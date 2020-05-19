package com.cz.android.sample.library.task;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.api.AndroidSourceDirectorySet;
import com.android.build.gradle.api.AndroidSourceSet;

import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.bundling.Jar;

import java.io.File;
import java.util.Set;

/**
 * @author Created by cz
 * @date 2020-05-19 14:17
 * @email chenzhen@okay.cn
 */
public class SourceCodeJarTask extends Jar {

    public SourceCodeJarTask() {
    }

    @TaskAction
    public void jarSource(){
        Project project = getProject();
        AppExtension appExtension = project.getExtensions().getByType(AppExtension.class);
        NamedDomainObjectContainer<AndroidSourceSet> sourceSets = appExtension.getSourceSets();
        File buildDir = project.getBuildDir();
        sourceSets.forEach(sourceSet->{
            AndroidSourceDirectorySet assets = sourceSet.getAssets();
            File jarFile=new File(buildDir,"libs");
            assets.srcDir(jarFile);
            AndroidSourceDirectorySet java = sourceSet.getJava();
            Set<File> srcDirs = java.getSrcDirs();
            //Copy file from those directions.
            from(srcDirs);
        });
    }
}
