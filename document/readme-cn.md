## AndroidSampleLibrary

>  春节特殊时期,本来打算学点其他的,发现.以前有很多库,写了可能都挺长时间没更新了.也没有一个足够方便的基础扩展演示库.于是带着这种想法.便着手设计了此库

* #### 已经完成的基本功能为:
* [扩展组件](component/sampleCompoent.md)
* [扩展功能](function/sampleFunction.md)
* [演示处理器](actionprocessor/actionProcessor.md)

#### [英文文档](../readme.md)

### 示例下载
[apk file](https://github.com/momodae/AndroidSampleLibrary/raw/master/apk/app-debug.apk)

### 演示图片

![Image1](https://github.com/momodae/LibraryResources/blob/master/AndroidSampleLibrary/image/image1.gif?raw=true)<br>
![Image2](https://github.com/momodae/LibraryResources/blob/master/AndroidSampleLibrary/image/image2.gif?raw=true)<br>

### 所有常用注解
The [Annotations](document/annotations/sampleAnnotation.md)

### 如何使用
最简单的使用是直接引入库与插件后，什么也不做。我会自动根据包信息，与Activity/Fragment的类名称等生成演示结果。<p>
此处，因为会自动扫描所有的Activity/Fragment,所以如果有不是演示的Activity/Fragment,请自行加上注解@Exclude.
另外发生冲突的，会自行合并。
```
|--test1
    |-- Sample1Activity
    |-- Sample2Activity
|--test2
    |-- Sample1Activity
    |-- Sample2Activity
```

正常使用需要自己配置使用示例。以及层级结构。好处是，可以支持国际化，示例描述，以及层级展示优先级等等。具体可以根据需求自行选择。

*  Activity 演示

    可以使用 @Register 或者 @RefRegister标志一个类为演示对象<br>

    ```
    @RefRegister(title=R.string.component_sample3,desc=R.string.component_sample3_desc,category = R.string.component_category,priority = 2)
    class ComponentSourceSampleActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_component_source_code_sample)
        }
    }
    ```

*  Fragment 演示
    可以使用 @Register 或者 @RefRegister标志一个类为演示对象<br>

    ```
    @RefRegister(title=R.string.component_sample5,desc = R.string.component_sample5_desc,category = R.string.component_category)
    class ComponentSampleFragment : Fragment() {
        private var index=0
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.fragment_component_sample, container, false);
        }
    }
    ```

* 如何组织层级

    所以演示,默认放在根目录,但是如果在指定 category,则会自动关联到此分类下<br>
    ```
    @RefRegister(xxx,category = R.string.component_category)<br>
    class Sample{
        ...
    }
    ```

   一定要注意,如果此分类不存在了. 这个演示会丢失.

*  设置异常处理类,如果在启动示例时,发生一些未知异常,会回调到此处.

    ```

    val projectApplication = SampleApplication.getProjectApplication()
    projectApplication.androidSample.registerExceptionHandler { context, e, registerItem, item ->
        Log.e(TAG, "Exception occurs:" + e.message)
    }
    ```

* 使用不同的组件,扩展演示. 非常重要!默认实现了4个组件,通过上面Component 文档介绍.只需要在每一个演示处,使用注解标志即可.

    ```
    //Mark each component annotation to this sample
    @SampleMessage
    @SampleMemory
    ...
    class Sample{
        ...
    }
    ```


### 问题

这个项目花费了一周时间. 时间也比较有限.而且是第一个版本,所以会有很多问题.

一些我确认的问题如下:

* 权限申请工作不正常.
* 源码查看视图应该为一个树级,当前只是一个列表

