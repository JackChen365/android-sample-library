## SampleComponent

*Here are some questions:*

* When you finished one sample. Have your even try to show the others with your document?
* If you have a powerful component to trace memory, How you put this component to any samples?
* You want to show some information about samples, such like the version and the author? How will you do that?

As a result:It's all about how to add additional view to each samples

### Here are the solution.
* First I have an abstract component container. It an interface.

    ```
    //SourceCode:core/com.cz.android.sample.component.CompanionComponentContainer

    public interface ComponentContainer {
        /**
         * if there was a condition that make this component available or not
         * @return
         */
        boolean isComponentAvailable(@NonNull Object object);

        /**
         * Get component view by view. If you want to maintain the original view just return it back;
         * @return
         */
        View getComponentView(@NonNull FragmentActivity context,@NonNull Object object,@NonNull ViewGroup parentView,@NonNull View view);

        /**
         * When component is already. This method will call
         * @see ComponentContainer#isComponentAvailable(java.lang.Object)
         * @param context
         * @param view
         */
        void onCreatedView(@NonNull FragmentActivity context,@NonNull Object object,@NonNull View view);

        /**
         * This will put this component to top or bottom in stack
         * @return
         */
        int getComponentPriority();
    }
    ```

#### Here is a sample for ComponentContainer

##### *Sample File*

```
//SourceCode:app/com.cz.sample.custom.component
    |-- BorderComponent
    |-- BorderLayout
    |-- SampleBorder
```

> This sample wrapped the original view. That allow you show all the view border.
It comes with the annotation: SampleBorder. That's your custom annotation to check if I have to apply this component or not
<br>

```
@Component
public class BorderComponent implements ComponentContainer {
    /**
     * We check if this object has Annotation:SampleBorder.
     * If this sample object doesn't have this annotation. It won't call the other functions
     * @param object
     * @return
     */
    @Override
    public boolean isComponentAvailable(@NonNull Object object) {
        SampleBorder sampleBorder = object.getClass().getAnnotation(SampleBorder.class);
        return null!=sampleBorder&&sampleBorder.value();
    }

    /**
     * This function is an critical function. It's move like a chain. Each component will call this function
     * And return a new view for the next.
     *
     * Tips:
     * 1. If the sample is not a activity or fragment. Take a look on {@link ComponentWindowDelegate}
     *
     * @param context activity context
     * @param object the instance of the sample. It depends on which one that you registered
     * @param parentView The parent view of your original view.
     * @param view your fragment/activity content view
     * @return
     */
    @Override
    public View getComponentView(@NonNull FragmentActivity context, @NonNull Object object, @NonNull ViewGroup parentView, @NonNull View view) {
        BorderLayout borderLayout=new BorderLayout(context);
        borderLayout.addView(view);
        return borderLayout;
    }

    /**
     * After this component created a new view. This function will call automatically.
     * The view is the one you created. You only have this chance to initialize your code here or it will be changed by the other component.
     * @param context
     * @param object
     * @param view
     */
    @Override
    public void onCreatedView(@NonNull FragmentActivity context, @NonNull Object object, @NonNull View view) {
    }

    /**
     * The priority in the component queue. If you want your component run before others
     * @return
     */
    @Override
    public int getComponentPriority() {
        return 0;
    }
}

```

### Some component We already have

#### *Component List*

```
//SourceCode:library/com.cz.android.sample.library.component
|-- code
|-- document
|-- memory
|-- message

```

##### Here are few pictures
* A sample with memory and message

    ![](https://github.com/momodae/LibraryResources/blob/master/AndroidSampleLibrary/image/component_memory1.png?raw=true)<br>

* A sample with document

    ![](https://github.com/momodae/LibraryResources/blob/master/AndroidSampleLibrary/image/component_document1.png?raw=true)<br>

    ![](https://github.com/momodae/LibraryResources/blob/master/AndroidSampleLibrary/image/component_document2.png?raw=true)<br>

* The original sample1

    ![](https://github.com/momodae/LibraryResources/blob/master/AndroidSampleLibrary/image/component_memory2.png?raw=true)<br>


#### Usage
* @SampleMemory attach memory component
* @SampleMessage attach message component
* @SampleSourceCode attach source code ViewPager
* @SampleDocument attach document ViewPager

```
@SampleMemory
@SampleMessage
@SampleSourceCode
@SampleDocument("documentSample.md")
@RefRegister(title=R.string.component_sample4,desc=R.string.component_sample4_desc,category = R.string.component_category,priority = 3)
class ComponentListSampleActivity : AppCompatActivity() {
    private var index=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_component_list_sample)
        testButton.setOnClickListener {
            //This message will show up in message panel automatically
            println("Message from ComponentListSampleActivity:${index++}\n")
        }
    }
}
```

##### Tips
1. @SampleMessage we don't receive android log message but instead of System.out/System.err
2. @SampleDocument

```
//It will search the document from the whole project by the name: DocumentSample.md
1. @SampleDocument("DocumentSample.md")
For example:
    If you have a document named: DocumentSample.md in somewhere. We will use it. No matter it in app/DocumentSample.md or library/DocumentSample.md
    But if you want the specific file. You should use app/DocumentSample.md

Tips:
    Make sure when you open this sample your code was committed to the repository

2. @SampleDocument("http://xxx.DocumentSample.md") It will use this url to load the document

3. @SampleDocument("assets://DocumentSample.md") It will check the file from assets
```

### What's more

* There are some complicated problems. What if component want have the same view, Like being one part of a ViewPager<br>
So we have another special component: com.cz.android.sample.component.CompanionComponentContainer<br>
I have to admit that is not a good solution. At least it worked.<br>
