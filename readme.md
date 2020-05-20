## AndroidSampleLibrary

> During the very critical moment. I made a plan for learning other computer languages. I was realized that I've lost a lot of source code.<br>
  After I finished one project or finished one part of the project. I forgot it immediately. That's why I was keep working on the Sample Library. Here is the fourth or fifth version of a test project.<br>
  I must say it's my favorite<br>

After one week of hard working. I've finished some functions:
* [Sample component](document/component/sampleCompoent.md)
* [Sample function](document/function/sampleFunction.md)
* Action processor

### Annotations
The [Annotations](document/annotations/sampleAnnotation.md)

#### [中文文档](document/readme-cn.md)


### Sample
[APK FILE](https://github.com/momodae/LibraryResources/blob/master/AndroidSampleLibrary/file/app-debug.apk?raw=true)


### Gradle

```
//Project:build.gradle ---------------
buildscript {
    repositories {
        ...
        maven{ url "http://www.momoda.pro:8081/repository/maven/"}
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.5.0'
        ...
        //Here is our plugin.
        classpath 'com.cz.android.sample:plugin:1.0.0'
}

allprojects {
    repositories {
        maven{ url "http://www.momoda.pro:8081/repository/maven/"}
    }
}

//Project:app -------------
apply plugin: 'sample'
dependencies {
    ...
    implementation "com.cz.android.sample.library:sample-library:1.0.4"
}

```

### Pictures

![Image1](https://github.com/momodae/LibraryResources/blob/master/AndroidSampleLibrary/image/image1.gif?raw=true)<br>
![Image2](https://github.com/momodae/LibraryResources/blob/master/AndroidSampleLibrary/image/image2.gif?raw=true)<br>

### How to use

* Setup an exception handler. If your sample does not works properly. Here you could trace the exception and check it out

    ```

    val projectApplication = SampleApplication.getProjectApplication()
    projectApplication.androidSample.registerExceptionHandler { context, e, registerItem, item ->
        Log.e(TAG, "Exception occurs:" + e.message)
    }
    ```


* For an activity sample

    You could use either @Register or @RefRegister to set this sample up<br>

    ```
    @RefRegister(title=R.string.component_sample3,desc=R.string.component_sample3_desc,category = R.string.component_category,priority = 2)
    class ComponentSourceSampleActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_component_source_code_sample)
        }
    }
    ```

* For an fragment sample
    You could use either @Register or @RefRegister to set this sample up<br>

    ```
    @RefRegister(title=R.string.component_sample5,desc = R.string.component_sample5_desc,category = R.string.component_category)
    class ComponentSampleFragment : Fragment() {
        private var index=0
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.fragment_component_sample, container, false);
        }
    }
    ```

* How to organize each samples

    First every samples belong to root. But if you setup your sample like this:<br>
    You actually changed the category of this sample<br>
    ```
    @RefRegister(xxx,category = R.string.component_category)<br>
    class Sample{
        ...
    }
    ```

    But if this category does not exist. The sample will be lost.

* How to use different kinds of components

    ```
    //Mark each component annotation to this sample
    @SampleMessage
    @SampleMemory
    ...
    class Sample{
        ...
    }
    ```


### Problems

> Sorry, I do know a lot of problems with this project. I just do all of this by myself during the Spring festival. I was working on this project for one week.<br>
It's just the early version of this project. So issue me when you find a bug.

The problems that I knew was:

* The permission function not work properly.
* The source code list is not a tree, I should organize all the file as a tree.


### About me

A programmer in mainland China.

If you have any new idea about this project, feel free to contact me with this email:bingo110@126.com


