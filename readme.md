## AndroidSampleLibrary

> During the very critical moment. I made a plan for learning other computer languages. I was realized that I've lost a lot of source code.<br>
  After I finished one project or finished one part of the project. I forgot it immediately. That's why I was keep working on the Sample Library. Here is the fourth or fifth version of a test project.<br>
  I must say it's my favorite<br>

After one week of hard working. I've finished some functions:
* [Sample component](document/component/sampleCompoent.md)
* [Sample function](document/function/sampleFunction.md)
* [Action processor](document/actionprocessor/actionProcessor.md)

### Annotations
The [Annotations](document/annotations/sampleAnnotation.md)

#### [中文文档](document/readme-cn.md)

### Sample
[APK FILE](https://github.com/momodae/LibraryResources/blob/master/AndroidSampleLibrary/file/app-debug.apk?raw=true)

### Better ways for you to simplify your samples.
[Usage](document/usage.md)

### Gradle

```
//Project:build.gradle ---------------
buildscript {
    ext.sample_version = '1.3.1'
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
    dependencies {
        ...
        //Here is our plugin.
        classpath "com.github.momodae.AndroidSampleLibrary:plugin:$sample_version"
}

allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

//Project:app -------------
apply plugin: 'sample'
dependencies {
    ...
    implementation "com.github.momodae.AndroidSampleLibrary:library:$sample_version"
}

```

### Pictures

![Image1](https://github.com/momodae/LibraryResources/blob/master/AndroidSampleLibrary/image/image1.gif?raw=true)<br>
![Image2](https://github.com/momodae/LibraryResources/blob/master/AndroidSampleLibrary/image/image2.gif?raw=true)<br>

### How to use this library.

* The simplest way to use this library.
If you want to use this library as simple as possible. The only thing you have to do is configure all the dependency and plugin. That's it.
I will help you generate all the categories and the sample information.
For example

```
|--test1
    |-- Sample1Activity
    |-- Sample2Activity
|--test2
    |-- Sample1Activity
    |-- Sample2Activity
```

From the package and class. I will help you generate all the demonstrate structure. Noticed that this is only for Activity and Fragment.
Because we collect all the Activity/Fragment, If you have other classes that you use them for special purposes. Use the annotation class:@Exclude.

* The other way, configure the category and sample by yourself.

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

* Setup an exception handler. If your sample does not works properly. Here you could trace the exception and check it out
you do not have an exception handler. When the exception occurred. The application will crash. For a demo, It not a problem.

    ```
    val projectApplication = SampleApplication.getProjectApplication()
    projectApplication.androidSample.registerExceptionHandler { context, e, registerItem, item ->
        Log.e(TAG, "Exception occurs:" + e.message)
    }
    ```


* The different between RefCategory and Category.
> We use R.string to support i18n. If you want to support that. The Category is more convenient.


### About me

A programmer in mainland China.

If you have any new idea about this project, feel free to contact me with this email:bingo110@126.com


