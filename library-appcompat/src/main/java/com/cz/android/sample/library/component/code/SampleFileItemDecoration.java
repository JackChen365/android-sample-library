package com.cz.android.sample.library.component.code;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cz.android.sample.library.R;


/**
 * @author Created by cz
 * @date 2020/5/27 10:39 PM
 * @email bingo110@126.com
 */
public class SampleFileItemDecoration extends RecyclerView.ItemDecoration {
    private final Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
    private final int hierarchyOffset;

    public SampleFileItemDecoration(Context context) {
        Resources resources = context.getResources();
        hierarchyOffset=resources.getDimensionPixelOffset(R.dimen.sample_hierarchy_depth_offset);
        int strokeWidth = resources.getDimensionPixelOffset(R.dimen.sample_hierarchy_stroke_width);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(ContextCompat.getColor(context,R.color.md_grey_400));
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
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
