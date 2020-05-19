package com.cz.android.sample.library.main.component;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.cz.android.sample.AndroidSample;
import com.cz.android.sample.api.AndroidSampleConstant;
import com.cz.android.sample.api.item.Demonstrable;
import com.cz.android.sample.api.item.RegisterItem;
import com.cz.android.sample.library.R;
import com.cz.android.sample.library.main.SampleApplication;
import com.cz.android.sample.library.main.adapter.SampleTemplateAdapter;
import com.cz.android.sample.main.MainSampleComponentFactory;

import java.util.List;

/**
 * @author Created by cz
 * @date 2020-01-27 19:25
 * @email bingo110@126.com
 */
public class DefaultMainSampleFragment extends Fragment implements MainSampleComponentFactory {

    @Override
    public Fragment getFragmentComponent() {
        return new DefaultMainSampleFragment();
    }

    /**
     * If you want to have you own tool bar.
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        AppCompatDelegate delegate = activity.getDelegate();
        delegate.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sample_fragment_main, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final FragmentActivity context = getActivity();
        final AppCompatActivity fragmentActivity = (AppCompatActivity)context;
        SampleApplication projectApplication = SampleApplication.getProjectApplication();
        final AndroidSample androidSample = projectApplication.getAndroidSample();
        Intent intent = fragmentActivity.getIntent();
        String title = intent.getStringExtra("title");
        View view = getView();
        Toolbar sampleToolBar=view.findViewById(R.id.sampleToolBar);
        ListView sampleListView=view.findViewById(R.id.sampleListView);
        String category;
        if(null==title) {
            category=AndroidSampleConstant.CATEGORY_ROOT;
            sampleToolBar.setTitle(R.string.app_name);
            fragmentActivity.setSupportActionBar(sampleToolBar);

            //Here show all the testCases
            List<RegisterItem> testCases = androidSample.getTestCases();
            if(null!=testCases&&!testCases.isEmpty()){
                alertTestCaseDialog(androidSample,context,testCases);
            }
        } else {
            category = title;
            sampleToolBar.setTitle(title);
            sampleToolBar.setSubtitle(intent.getStringExtra("desc"));
            fragmentActivity.setSupportActionBar(sampleToolBar);
            ActionBar supportActionBar = fragmentActivity.getSupportActionBar();
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            sampleToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentActivity.finish();
                }
            });
        }
        List<Demonstrable> demonstrableList = androidSample.getDemonstrableList(category);
        final SampleTemplateAdapter adapter = new SampleTemplateAdapter(context, demonstrableList);
        sampleListView.setAdapter(adapter);
        sampleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Demonstrable demonstrable = adapter.getItem(i);
                if(demonstrable instanceof RegisterItem){
                    //run this sample
                    RegisterItem registerItem = (RegisterItem) demonstrable;
                    androidSample.start(context,registerItem);
                } else {
                    //move to the subcategories
                    String category = demonstrable.getTitle();
                    List<Demonstrable> demonstrableList = androidSample.getDemonstrableList(category);
                    if(demonstrableList.isEmpty()){
                        Toast.makeText(context.getApplicationContext(),"Couldn't found more sample items!", Toast.LENGTH_SHORT).show();
                    } else {
                        ComponentName activityComponent = getLauncherActivityComponent(context);
                        Intent intent=new Intent();
                        intent.setComponent(activityComponent);
                        intent.putExtra("title",demonstrable.getTitle());
                        intent.putExtra("desc",demonstrable.getDescription());
                        context.startActivity(intent);
                    }
                }
            }
        });
    }

    /**
     * Show all the testCases.
     * @param context
     * @param testCases
     */
    private void alertTestCaseDialog(final AndroidSample androidSample, final FragmentActivity context,@NonNull final List<RegisterItem> testCases) {
        if(1==testCases.size()){
            //Run this testcase immediately
            RegisterItem registerItem = testCases.get(0);
            androidSample.start(context,registerItem);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            CharSequence[] items=new CharSequence[testCases.size()];
            for(int i=0;i<testCases.size();i++){
                RegisterItem registerItem = testCases.get(i);
                items[i]=registerItem.getTitle();
            }
            builder.setTitle(R.string.sample_choice_test_case)
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            RegisterItem registerItem = testCases.get(i);
                            androidSample.start(context,registerItem);
                        }
                    }).setCancelable(false);
            builder.show();
        }
    }

    /**
     * This function return launcher activity component
     */
    private ComponentName getLauncherActivityComponent(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent launchIntent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        return launchIntent.getComponent();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }
}
