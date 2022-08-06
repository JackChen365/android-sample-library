## ActionProcessor
For a sample library. We are not going to just demonstrate Activity or Fragment. But sometimes display a dialog or something just print a message.
Long before this version. I believe this is good enough. But one day one of my friends told me. I have a Dialog library. All I want is to click the title and display the dialog.
Then that makes me start try to abstract the action. Here is how we support this function.


```
public abstract class ActionProcessor {
    /**
     * consider if you want to process this item. usually is class object. HowEver It will be an custom object.
     */
    public abstract boolean isAvailable(Class clazz);

    /**
     * process the register configuration
     *
     * @param context    Android context object. Here you is an FragmentActivity.
     * @param sampleItem the one that you register which is all the information you could use
     * @throws Exception when you execute an action failed throw an exception
     */
    public abstract void execute(AppCompatActivity context, SampleItem sampleItem) throws SampleFailedException;
}
```

This is how we support `Activity`

```
public class ActivityClassActionProcessor extends ActionProcessor {
    @Override public boolean isAvailable(final Class clazz) {
        return Activity.class.isAssignableFrom(clazz);
    }

    @Override public void execute(final AppCompatActivity context, final SampleItem sampleItem)
            throws SampleFailedException {
        Class<?> clazz = sampleItem.clazz();
        Intent intent = new Intent(context, clazz);
        intent.putExtra(SampleConstants.PARAMETER_TITLE, sampleItem.title);
        intent.putExtra(SampleConstants.PARAMETER_DESC, sampleItem.desc);
        context.startActivity(intent);
    }
}

```

Quite easy, right?

Here is a demo to support AlertDialog instance

```
@Extension
public class AlertDialogActionProcessor extends SampleInterfaceProcessor<AlertDialog> {
    @Override public void execute(final AppCompatActivity context, final AlertDialog dialog)
            throws SampleFailedException {
        dialog.show();
    }
}
```

You can check the [demo](../app/src/main/java/com/github/jackchen/sample/dialog/DialogSample.kt) for more details.

The annotation class `Extension` is to register this processor as an extension.


