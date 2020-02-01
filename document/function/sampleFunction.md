## Sample Function

> After I start a sample. However, I want to do something.<br>
Such as request runtime permission, or collect all the use time and sort samples dynamically.<br>
But I can't just update my project every time when the time when I want to change something. That why we have this.<br>


##### *Related Source File*

```
//SourceCode:library/com.cz.android.sample.library.function.permission
    |-- Permission
    |-- PermissionObserver
    |-- @SamplePermission
    |-- SamplePermissionFunction (This is the implementation of the Function)
    |-- SamplePermissoinsFragment
```

#### SamplePermissionFunction

```
@Function
public class SamplePermissionFunction implements SampleFunction {
    /**
     * If your function wants to do some initial work. Here we inject the fragment.
     * But if we don't we this function, call SamplePermissionsFragment.injectIfNeededIn(context);
     * and then try to get fragment from the FragmentManager It just didn't exist
     * @param context
     */
    @Override
    public void init(FragmentActivity context) {
        //inject permission fragment
        SamplePermissionsFragment.injectIfNeededIn(context);
    }

    /**
     * Check this class and determined this class needs to run this function
     * @param clazz
     * @return
     */
    @Override
    public boolean isAvailable(Class<?> clazz) {
        SamplePermission samplePermission = clazz.getAnnotation(SamplePermission.class);
        return (null!=samplePermission&&
                null!=samplePermission.value()&&
                0 < samplePermission.value().length);
    }

    @Override
    public void run(FragmentActivity context, final Object object, RegisterItem item) {
        SamplePermission samplePermission = item.clazz.getAnnotation(SamplePermission.class);
        String[] permissions = samplePermission.value();
        //Here add permission observer
        SamplePermissionsFragment permissionsFragment=SamplePermissionsFragment.get(context);
        PermissionObserver permissionObserver=null;
        if(object instanceof PermissionObserver){
            permissionObserver = (PermissionObserver) object;
        }
        final PermissionObserver observer=permissionObserver;
        PermissionObserver permissionObserverWrapper = new PermissionObserver() {
            @Override
            public void onGranted(Permission permission) {
                if(null!=observer){
                    observer.onGranted(permission);
                }
            }

            @Override
            public void onDenied(Permission permission) {
                if(null!=observer){
                    observer.onDenied(permission);
                }
            }
        };
        //add permission observer
        permissionsFragment.addPermissionObserver(permissions,permissionObserverWrapper);
        //Request permission
        permissionsFragment.requestPermissions(permissions);
    }
}

```

> How we use this function

```

@SamplePermission(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE)
@RefRegister(title=R.string.function_permission_sample1,desc = R.string.function_permission_sample1_desc,category = R.string.sample_function)
class SamplePermissionActivity : SampleAppCompatActivity(),PermissionObserver{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_function_permission_sample)
    }

    override fun onGranted(permission: Permission) {
        val text = getString(R.string.permission_granted, permission.name)
        Toast.makeText(applicationContext,text,Toast.LENGTH_SHORT).show()
    }

    override fun onDenied(permission: Permission) {
        val text = getString(R.string.permission_denied, permission.name)
        Toast.makeText(applicationContext,text,Toast.LENGTH_SHORT).show()
    }
}
```

### How to implement your own function

1. Mark class as a function
2. Have a class implements from the interface:SampleFunction

```
//
@Function
public class VisitRecordFunction implements SampleFunction {
    private static final String TAG="VisitRecordFunction";

    @Override
    public void init(FragmentActivity context) {
    }
    @Override
    public boolean isAvailable(Class<?> clazz) {
        // when you return false. It won't call run method below.
        return true;
    }
    @Override
    public void run(FragmentActivity context, Object object, RegisterItem item) {
        // do something...
        Log.e(TAG,"Title:"+item.getTitle()+" class:"+object.toString());
    }
}

```



### The problems

* I do know that my fragment request permission with call the method OnActivityForResult. I will fix this in next version

