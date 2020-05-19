package com.cz.android.sample.library;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.api.AndroidSourceDirectorySet;
import com.android.build.gradle.api.AndroidSourceSet;
import com.cz.android.sample.library.transform.SampleTransform;

import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.PluginContainer;

import java.io.File;
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
            //We copy all the source file into the assets folder.
            AppExtension appExtension = project.getExtensions().getByType(AppExtension.class);
            NamedDomainObjectContainer<AndroidSourceSet> sourceSets = appExtension.getSourceSets();
            sourceSets.forEach(sourceSet->{
                AndroidSourceDirectorySet java = sourceSet.getJava();
                Set<File> srcDirs = java.getSrcDirs();
                AndroidSourceDirectorySet assets = sourceSet.getAssets();
                assets.srcDirs(srcDirs);
            });
            //Register the gradle transform.
            SampleTransform sampleTransform = new SampleTransform(project);
            appExtension.registerTransform(sampleTransform);
        }
    }

    public static boolean isAndroidProject(Project project) {
        PluginContainer plugins = project.getPlugins();
        return null!=plugins.findPlugin("com.android.application");
    }
}
