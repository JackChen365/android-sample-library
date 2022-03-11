package com.cz.android.sample.main.component;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.cz.android.sample.AndroidSample;
import com.cz.android.sample.api.SampleItem;
import com.cz.android.sample.core.R;
import com.cz.android.sample.core.databinding.SampleFragmentMainBinding;
import com.cz.android.sample.main.adapter.MutableListAdapter;
import com.cz.android.sample.main.adapter.SampleListAdapter;
import java.util.List;

/**
 * @author Created by cz
 * @date 2020-01-27 19:25
 * @email bingo110@126.com
 */
public class DefaultMainSampleFragment extends Fragment {
    private SampleFragmentMainBinding binding;
    /**
     * If you want to have you own tool bar.
     *
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        AppCompatDelegate delegate = activity.getDelegate();
        delegate.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = SampleFragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        final AndroidSample androidSample = AndroidSample.Companion.getInstance();
        Intent intent = activity.getIntent();
        String title = intent.getStringExtra("title");
        String desc = intent.getStringExtra("desc");
        String path = intent.getStringExtra("path");
        if (null == path) {
            binding.sampleToolbar.setTitle(R.string.app_name);
            activity.setSupportActionBar(binding.sampleToolbar);

            //Here show all the testCases
            List<SampleItem> testCases = androidSample.getTestCases();
            if (null != testCases && !testCases.isEmpty()) {
                alertTestCaseDialog(androidSample, activity, testCases);
            }
        } else {
            binding.sampleToolbar.setTitle(title);
            binding.sampleToolbar.setSubtitle(desc);
            activity.setSupportActionBar(binding.sampleToolbar);
            ActionBar supportActionBar = activity.getSupportActionBar();
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            binding.sampleToolbar.setNavigationOnClickListener(view1 -> getActivity().finish());
        }
        List<AndroidSample.PathNode> pathNodeList = androidSample.getPathNodeList(path);
        final DividerItemDecoration itemDecoration = new DividerItemDecoration(activity,
                LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(activity, R.drawable.sample_list_divider));
        binding.sampleList.addItemDecoration(itemDecoration);
        binding.sampleList.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        final SampleListAdapter adapter = new SampleListAdapter(pathNodeList);
        binding.sampleList.setAdapter(adapter);
        adapter.setOnItemClickListener((list, v, position) -> {
            final AndroidSample.PathNode pathNode = list.get(position);
            final Object item = pathNode.getItem();
            if (item instanceof SampleItem) {
                SampleItem sampleItem = (SampleItem) item;
                androidSample.start(activity, sampleItem);
                return;
            }
            String category = item.toString();
            String subDirectory = path + "/" + category;
            Intent newIntent = new Intent();
            ComponentName activityComponent = getLauncherActivityComponent(activity);
            newIntent.setComponent(activityComponent);
            newIntent.putExtra("title", category);
            newIntent.putExtra("path", subDirectory);
            startActivity(intent);
        });
    }

    /**
     * Show all the testCases.
     *
     * @param context
     * @param testCases
     */
    private void alertTestCaseDialog(final AndroidSample androidSample, final AppCompatActivity context,
            @NonNull final List<SampleItem> testCases) {
        if (1 == testCases.size()) {
            //Run this testcase immediately
            SampleItem sampleItem = testCases.get(0);
            androidSample.start(context, sampleItem);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            CharSequence[] items = new CharSequence[testCases.size()];
            for (int i = 0; i < testCases.size(); i++) {
                SampleItem sampleItem = testCases.get(i);
                items[i] = sampleItem.title;
            }
            builder.setTitle(R.string.sample_choice_test_case)
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SampleItem sampleItem = testCases.get(i);
                            androidSample.start(context, sampleItem);
                        }
                    }).setCancelable(true);
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
