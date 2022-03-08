## Annotations
| Annotation | Note |
| ------ | ------ |
| Register | register a sample |
| RefRegister | Same as @Register but only use R.string |
| Category | register a category |
| RefCategory | Same as @Register but only use R.string |
| ActionProcessor |  |
| Component |  |
| Function |  |
| MainComponent |  |
| TestCase |  |


* *Register*
> If you want to support i18n then use the RefRegister instead

```
@Register(title="SampleTitle",desc="SampleDescription",category = "Which category this sample belong")
class Sample {
    ...
}

```

* *Category*
> If you want to support i18n then use the RefRegister instead

```
@Category(title="category title",desc = "category description")
class Object{
    ...
}
```

Make sure that all your samples has an exact category or it will lose from nowhere

* *ActonProcessor*
> If you got a special sample that I do not support. You could implement your own sample action processor

For instance:
* I am not support alertDialog. Then I write some code like this.

```
//With this annotation. this class will register to the action processor manager automatically
@ActionProcessor
public class AlertDialogActionProcessor extends AbsActionProcessor<AlertDialog> {
    //Check is this class object you want to take
    @Override
    public boolean isInstance(Class clazz) {
        return SampleObject.class.isAssignableFrom(clazz);
    }

    //Then after you get this register sample. Here use the information to do what you want.
    @Override
    public AlertDialog getInstance(FragmentActivity context, RegisterItem item, Class clazz) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        SampleObject object;
        try{
            object= (SampleObject) clazz.newInstance();
        } catch (InstantiationException e){
        }
        return (AlertDialog) object.getObject(context);
    }

    @Override
    public void run(FragmentActivity context, RegisterItem sampleItem, AlertDialog item) {
        item.show();
    }
}

//Now I can show you all different kinds of AlertDialog
 @RefRegister(title = R.string.dialog_sample1,desc =R.string.dialog_sample1_desc,category = R.string.dialog)
public class DialogSample1 implements SampleObject {
    @Override
    public Object getObject(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Login Alert")
                .setMessage("Are you sure, you want to continue ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context,"Selected Option: YES",Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context,"Selected Option: No",Toast.LENGTH_SHORT).show();
                    }
                });
        //Creating dialog box
        return builder.create();
    }
}

```

Make sure after you have your own ActionProcessor. mark as ActionProcessor


* *Component*

    If you want to inject a common view into some specific samples. You should check this out [More detail](https://github.com/momodae/AndroidSampleLibrary/blob/master/document/component/sampleCompoent.md)

* *Function*

    If you want to do something every time when you open a sample. You should check this out [More detail](https://github.com/momodae/AndroidSampleLibrary/blob/master/document/function/sampleFunction.md)

* *MainComponent*

    This is an annotation that register a main component. Actually you don't have to implement your own main component.<br>
    The component is for the programmer who want to have their own home page. I will give you all the information that you registered in.


Take a look at [MainSampleFragment](https://github.com/momodae/AndroidSampleLibrary/blob/master/app/src/main/java/com/cz/sample/custom/main/MainSampleFragment.java)

* *TestCase*

    This one is when you want to run a sample immediately. But you have a lot of samples.
    You can mark your sample as @TestCase. Then I will start your sample at home page.
    However, if you have more than one @TestCase, I will alert a dialog for you to choose which one you want to demonstrate.

![](image/https://github.com/momodae/LibraryResources/blob/master/AndroidSampleLibrary/image/test_case_image.png?raw=true)
