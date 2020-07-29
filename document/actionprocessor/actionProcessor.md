## ActionProcessor
For a sample library. We are not going to just demonstrate Activity or Fragment. But sometimes display a dialog or something just print a message.
Long before this version. I believe this is good enough. But one day one of my friends tells me. I have a Dialog library. All I want is to click the title and display the dialog.
Then that makes me start try to abstract the action. How to support this function dynamically.


```
public abstract class AbsActionProcessor<T> {
    /**
     * consider if you want to process this item. usually is class object. HowEver It will be an custom object.
     */
    public abstract boolean isInstance(Class clazz);

    /**
     * Get object instance from class object
     * @param context
     * @param clazz
     * @return
     */
    public abstract T getInstance(FragmentActivity context,RegisterItem item,Class clazz) throws Exception;

    /**
     * process the register configuration
     * @param context Android context object. Here you is an FragmentActivity.
     * @param registerItem the one that you register which is all the information you could use
     * @throws Exception when you execute an action failed throw an exception
     */
    public abstract void run(FragmentActivity context, RegisterItem registerItem,T item) throws Exception;
}

```

The abstract class was the basic class for us to use. For a Activity. We got an implementation.

```
@ActionProcessor
public class ActivityClassActionProcessor extends ClassActionProcessor {
    @Override
    public boolean isInstance(Class item) {
        //Test if the sample class is available for the processor.
        return super.isInstance(item)&& Activity.class.isAssignableFrom(item);
    }

    @Override
    public void run(FragmentActivity context, RegisterItem registerItem, Class clazz){
        //How the activity processor handle the class.  
        Intent intent = new Intent(context, clazz);
        intent.putExtra("title",registerItem.title);
        intent.putExtra("desc",registerItem.desc);
        context.startActivity(intent);
    }
}
```

Here is another one to support Dialog

```
@ActionProcessor
public class DialogClassActionProcessor extends ClassActionProcessor {
    @Override
    public boolean isInstance(Class item) {
        return super.isInstance(item)&& Dialog.class.isAssignableFrom(item);
    }

    @Override
    public void run(FragmentActivity context, RegisterItem registerItem, Class clazz) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        Constructor<Dialog> constructor = (Constructor<Dialog>) clazz.getConstructor(Context.class);
        Dialog dialog = constructor.newInstance(context);
        dialog.show();
    }
}
```
You may aware of the Annotation: @ActionProcessor. This is for the plugin to collect all the action processor together.
Because of the abstraction. Now we could easily support all kinds of demos.





