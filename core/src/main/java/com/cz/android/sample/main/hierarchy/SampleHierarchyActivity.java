package com.cz.android.sample.main.hierarchy;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cz.android.sample.AndroidSample;
import com.cz.android.sample.api.AndroidSampleConstant;
import com.cz.android.sample.api.SampleItem;
import com.cz.android.sample.api.item.CategoryItem;
import com.cz.android.sample.api.item.Demonstrable;
import com.cz.android.sample.library.R;
import com.cz.android.sample.library.main.SampleApplication;
import com.cz.widget.recyclerview.adapter.listener.OnTreeNodeClickListener;
import com.cz.widget.recyclerview.adapter.support.tree.TreeAdapter;
import com.cz.widget.recyclerview.adapter.support.tree.TreeNode;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.util.List;

public class SampleHierarchyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //This Activity already has an action bar supplied by the window decor. Do not request Window.FEATURE_SUPPORT_ACTION_BAR and set windowActionBar to false in your theme to use a Toolbar instead.
        AppCompatDelegate delegate = getDelegate();
        if(!delegate.hasWindowFeature(Window.FEATURE_NO_TITLE)){
            delegate.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        setContentView(R.layout.activity_sample_hierarchy);
        //Initialize the toolbar.
        initializeToolbar();

        //Build the tree.
        SampleApplication projectApplication = SampleApplication.getProjectApplication();
        final AndroidSample androidSample = projectApplication.getAndroidSample();
        TreeNode<Demonstrable> rootNode= new TreeNode<>(null);
        buildSampleTree(androidSample,rootNode,AndroidSampleConstant.CATEGORY_ROOT);
        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SampleHierarchyItemDecoration(this));
        SampleHierarchyAdapter sampleHierarchyAdapter = new SampleHierarchyAdapter(this, rootNode);
        sampleHierarchyAdapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener<Demonstrable>() {
            @Override
            public void onNodeItemClick(@Nullable TreeNode<Demonstrable> node, @Nullable Demonstrable item, @Nullable View v, int position) {
                SampleItem sampleItem = (SampleItem) item;
                SampleDetailDialog sampleDetailDialog = new SampleDetailDialog(sampleItem);
                sampleDetailDialog.show(getSupportFragmentManager(),null);
            }
        });
        sampleHierarchyAdapter.expandAll();
        recyclerView.setAdapter(sampleHierarchyAdapter);
    }

    private void initializeToolbar(){
        //initialize the toolbar.
        Toolbar toolBar=findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        ActionBar supportActionBar = getSupportActionBar();
        Intent intent = getIntent();
        supportActionBar.setTitle(R.string.sample_hierarchy);
        supportActionBar.setSubtitle(R.string.sample_hierarchy_desc);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void buildSampleTree(AndroidSample androidSample,TreeNode<Demonstrable> parentNode, String category) {
        List<Demonstrable> sampleList = androidSample.getDemonstrableList(category);
        if(null!=sampleList){
            for(Demonstrable demonstrable:sampleList){
                TreeNode<Demonstrable> childNode=new TreeNode<Demonstrable>(parentNode,demonstrable);
                parentNode.children.add(childNode);
                if(demonstrable instanceof CategoryItem){
                    String title = demonstrable.getTitle();
                    buildSampleTree(androidSample,childNode,title);
                }
            }
        }
    }

    private static class SampleHierarchyAdapter extends TreeAdapter<Demonstrable> {
        private static final int TYPE_CATEGORY=0;
        private static final int TYPE_SAMPLE=1;
        private final Resources resources;
        private final LayoutInflater layoutInflater;
        private final int padding;

        public SampleHierarchyAdapter(Context context, TreeNode<Demonstrable> rootNode) {
            super(rootNode);
            this.resources=context.getResources();
            this.layoutInflater = LayoutInflater.from(context);
            this.padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.getDisplayMetrics());
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if(TYPE_CATEGORY==viewType){
                return new RecyclerView.ViewHolder(layoutInflater.inflate(R.layout.sample_hierrchy_group_item,parent,false)){};
            } else {
                return new RecyclerView.ViewHolder(layoutInflater.inflate(R.layout.sample_hierarchy_item,parent,false)){};
            }
        }

        @Override
        public int getItemViewType(int position) {
            Demonstrable demonstrable=getItem(position);
            int viewType=TYPE_CATEGORY;
            if(demonstrable instanceof SampleItem){
                viewType=TYPE_SAMPLE;
            }
            return viewType;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, TreeNode<Demonstrable> node, Demonstrable item, int viewType, int position) {
            String titleEntryName=null;
            String categoryEntryName=null;
            holder.itemView.setPadding(padding * node.depth,
                    holder.itemView.getPaddingTop(),
                    holder.itemView.getPaddingRight(),
                    holder.itemView.getPaddingBottom());
            if(TYPE_CATEGORY==viewType){
                CategoryItem categoryItem = (CategoryItem) item;
                ImageView imageFlagView=holder.itemView.findViewById(R.id.imageFlagView);
                TextView categoryName=holder.itemView.findViewById(R.id.categoryName);
                TextView titleRef=holder.itemView.findViewById(R.id.titleRef);
                TextView categoryRef=holder.itemView.findViewById(R.id.categoryRef);

                categoryName.setText(categoryItem.title);
                imageFlagView.setSelected(node.isExpand);

                if(0!=categoryItem.titleRes){
                    titleEntryName=resources.getString(categoryItem.titleRes);
                }
                if(0!=categoryItem.categoryRes){
                    categoryEntryName=resources.getString(categoryItem.categoryRes);
                }
                titleRef.setVisibility(null==titleEntryName?View.GONE:View.VISIBLE);
                categoryRef.setVisibility(null==categoryEntryName?View.GONE:View.VISIBLE);
                titleRef.setText(null==titleEntryName? "#" : titleEntryName);
                categoryRef.setText((null==categoryEntryName? "#" : categoryEntryName));
            } else if(TYPE_SAMPLE==viewType){
                int index = indexOf(node.parent.children,node);
                final SampleItem sampleItem = (SampleItem) item;
                TextView sampleNumber=holder.itemView.findViewById(R.id.sampleNumber);
                TextView sampleClass=holder.itemView.findViewById(R.id.sampleClass);

                sampleNumber.setText(String.valueOf(index+1));
                sampleClass.setText(sampleItem.clazz.getSimpleName());
            }
        }

        private int indexOf(List<TreeNode<Demonstrable>> children, TreeNode<Demonstrable> node){
            int index=-1;
            for(int i=0;i<children.size();i++){
                TreeNode<Demonstrable> treeNode = children.get(i);
                if(treeNode.item instanceof SampleItem &&node.item instanceof SampleItem){
                    SampleItem sampleItem = (SampleItem) treeNode.item;
                    SampleItem item = (SampleItem) node.item;
                    if(sampleItem.title.equals(item.title)){
                        index=i;
                        break;
                    }
                }
            }
            return index;
        }
    }

    public static class SampleDetailDialog extends BottomSheetDialogFragment {
        private final SampleItem sampleItem;

        public SampleDetailDialog(@NonNull SampleItem sampleItem) {
            this.sampleItem = sampleItem;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.sample_hierarchy_item_detail,container,false);
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            View view = getView();
            TextView sampleClass=view.findViewById(R.id.sampleClass);
            TextView sampleTitle=view.findViewById(R.id.sampleTitle);
            TextView sampleDesc=view.findViewById(R.id.sampleDesc);
            TextView sampleCategory=view.findViewById(R.id.sampleCategory);
            TextView sampleRef=view.findViewById(R.id.sampleRef);

            sampleClass.setText(sampleItem.clazz.getSimpleName());
            sampleTitle.setText(sampleItem.title);
            sampleDesc.setText(sampleItem.desc);
            sampleCategory.setText(sampleItem.category);

            String titleResourceEntryName=null;
            String descResourceEntryName=null;
            String categoryResourceEntryName=null;
            Resources resources = getContext().getResources();
            if(0!= sampleItem.titleRes){
                titleResourceEntryName=resources.getResourceEntryName(sampleItem.titleRes);
            }
            if(0!= sampleItem.descRes){
                descResourceEntryName=resources.getResourceEntryName(sampleItem.descRes);
            }
            if(0!= sampleItem.categoryRes){
                categoryResourceEntryName=resources.getResourceEntryName(sampleItem.categoryRes);
            }
            sampleRef.setText("title:"+(null==titleResourceEntryName? "#" : "R.string."+titleResourceEntryName)+"\n"+
                    "desc:"+(null==descResourceEntryName? "#" : "R.string."+descResourceEntryName)+"\n"+
                    "category:"+(null==categoryResourceEntryName? "#" : "R.string."+categoryResourceEntryName));

            //Launcher the sample.
            View sampleButton=view.findViewById(R.id.sampleButton);
            sampleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    FragmentActivity activity = getActivity();
                    if(null!=activity){
                        SampleApplication projectApplication = SampleApplication.getProjectApplication();
                        final AndroidSample androidSample = projectApplication.getAndroidSample();
                        androidSample.start(activity, sampleItem);
                    }
                }
            });

        }
    }
}
