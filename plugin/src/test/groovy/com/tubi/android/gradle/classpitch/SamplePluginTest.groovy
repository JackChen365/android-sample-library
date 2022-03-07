package com.tubi.android.gradle.classpitch

import org.apache.commons.io.FileUtils
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Shared
import spock.lang.Specification

class SamplePluginTest extends Specification{
    @Rule TemporaryFolder testProjectDir = new TemporaryFolder(new File("build/tmp"))
    @Shared private def inputAssetsProvider = new TestAssetsProvider("TestApp")

    def "test sample plugin"() {
        given:
        FileUtils.cleanDirectory(testProjectDir.root.parentFile)
        FileUtils.copyDirectory(inputAssetsProvider.functionalAssetsDir, testProjectDir.root)
        def tmpLocalProperties = new File(testProjectDir.root, "local.properties")
        tmpLocalProperties.append("sdk.dir=" + getAndroidSdkDir())

        def buildScript = new File(testProjectDir.root, "build.gradle")
        buildScript.text = buildScript.text.replaceAll("classpath", "//classpath")

        def appBuildScript = new File(testProjectDir.root, "app/build.gradle")
        appBuildScript.text = appBuildScript.text.replace("//id 'plugin-placeholder'", "id 'sample'")

        expect:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments(':app:transformClassesWithSampleForDebug', "--stacktrace")
                .withDebug(true)
                .forwardOutput()
                .withPluginClasspath()
                .build()
        null != result && result.output.contains("""
        ----| sample
        --------| class:com.tubi.android.testapp.sample.ComponentSampleFragment
        --------| class:com.tubi.android.testapp.sample.ComponentListSampleActivity
        ----| extension
        --------| class:com.tubi.android.testapp.extensions.component.BorderComponent
        --------| class:com.tubi.android.testapp.extensions.function.VisitRecordFunction
        """.stripIndent())
    }

    def "test assemble package"() {
        given:
        FileUtils.cleanDirectory(testProjectDir.root.parentFile)
        FileUtils.copyDirectory(inputAssetsProvider.functionalAssetsDir, testProjectDir.root)
        def tmpLocalProperties = new File(testProjectDir.root, "local.properties")
        tmpLocalProperties.append("sdk.dir=" + getAndroidSdkDir())

        def buildScript = new File(testProjectDir.root, "build.gradle")
        buildScript.text = buildScript.text.replaceAll("classpath", "//classpath")

        def appBuildScript = new File(testProjectDir.root, "app/build.gradle")
        appBuildScript.text = appBuildScript.text.replace("//id 'plugin-placeholder'", "id 'sample'")

        expect:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments(':app:assembleDebug', "--stacktrace")
                .withDebug(true)
                .forwardOutput()
                .withPluginClasspath()
                .build()
        null != result
    }

    private def getAndroidSdkDir() {
        def localPropertiesFile = new File('../local.properties')
        if (localPropertiesFile.exists()) {
            Properties local = new Properties()
            local.load(new FileReader(localPropertiesFile))
            if (local.containsKey('sdk.dir')) {
                def property = local.getProperty("sdk.dir")
                if (null != property) {
                    File sdkDir = new File(property)
                    if (sdkDir.exists()) {
                        return property
                    }
                }
            }
        }
        return new NullPointerException("Can not found the initial android SDK configuration.")
    }
}
