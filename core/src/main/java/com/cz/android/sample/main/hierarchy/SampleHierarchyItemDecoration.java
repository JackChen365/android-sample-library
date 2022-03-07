package com.cz.android.sample.main.hierarchy;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cz.android.sample.library.R;

/**
 * @author Created by cz
 * @date 2020/5/27 10:39 PM
 * @email bingo110@126.com
 */
public class SampleHierarchyItemDecoration extends RecyclerView.ItemDecoration {
    private final Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
    private final int hierarchyOffset;

    public SampleHierarchyItemDecoration(Context context) {
        Resources resources = context.getResources();
        hierarchyOffset=resources.getDimensionPixelOffset(R.dimen.sample_hierarchy_depth_offset);
        int strokeWidth = resources.getDimensionPixelOffset(R.dimen.sample_hierarchy_stroke_width);
        paint.setStrokeWidth(strokeWidth);
        TypedArray a = context.obtainStyledAttributes(new int[]{R.attr.colorAccent});
        int colorAccent = a.getColor(0, Color.GRAY);
        paint.setColor(colorAccent);
        a.recycle();
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        if(!state.isPreLayout()){
            int childCount = parent.getChildCount();
            int offsetLeft = parent.getPaddingLeft();
            int offsetTop = parent.getPaddingTop();
            for(int i=0;i<childCount;i++){
                View childView = parent.getChildAt(i);
                int left = hierarchyOffset+childView.getPaddingLeft();
                int top = childView.getTop()+childView.getHeight()/2;
                c.drawLine(offsetLeft,offsetTop,offsetLeft,top,paint);
                c.drawLine(offsetLeft,top,left,top,paint);
            }
        }
    }
}
