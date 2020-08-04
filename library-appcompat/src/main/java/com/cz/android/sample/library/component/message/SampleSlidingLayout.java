package com.cz.android.sample.library.component.message;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import androidx.annotation.IdRes;

import com.cz.android.sample.library.R;

/**
 * @author Created by cz
 * @date 2020-02-03 09:16
 * @email bingo110@126.com
 */
public class SampleSlidingLayout extends ViewGroup {
    private static final String TAG="SampleSlidingLayout";
    /**
     * The maximum child view count this container could have
     */
    private final static int MAX_VIEW_COUNT=3;
    /**
     * Center handle view's id
     */
    private int handleId= View.NO_ID;
    /**
     * Different weight between top and bottom panel
     */
    private float slidingWeight=0f;
    /**
     * The last drag x,y point
     */
    private float lastX=0f;
    private float lastY=0f;
    /**
     * scaled touch slop
     */
    private int scaledTouchSlop=0;
    /**
     * Is begin to drag
     */
    private boolean isBeingDragged=false;
    /**
     * The temp rect
     */
    private RectF tempRect= new RectF();

    public SampleSlidingLayout(Context context) {
        this(context,null, R.attr.sampleSlidingLayout);
    }

    public SampleSlidingLayout(Context context, AttributeSet attrs) {
        this(context, attrs,R.attr.sampleSlidingLayout);
    }

    public SampleSlidingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SampleSlidingLayout, defStyleAttr, R.style.SampleSlidingLayout);
        setSlidingHandle(a.getResourceId(R.styleable.SampleSlidingLayout_sliding_handle, View.NO_ID));
        setSlidingWeight(a.getFloat(R.styleable.SampleSlidingLayout_sliding_weight,0f));
        a.recycle();
    }

    /**
     * Setup the handle view's id
     */
    private void setSlidingHandle(@IdRes int id) {
        if(id==View.NO_ID){
            throw new NullPointerException("Before you drag the layout,you should set sliding_handle to the handle bar!");
        }
        this.handleId=id;
    }

    /**
     * Setup the top and bottom panel's split weight
     */
    private void setSlidingWeight(float weight) {
        this.slidingWeight=weight;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //Check if handle view existed
        View handleView = findViewById(handleId);
        if(null==handleView){
            Resources resources = getResources();
            throw new IllegalArgumentException("Handle id is invalid! Please check your id:"+resources.getResourceEntryName(handleId));
        }
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        int childCount = getChildCount();
        if(childCount> MAX_VIEW_COUNT){
            throw new IllegalArgumentException("SlidingLayout can't add additional views!");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int measuredHeight = getMeasuredHeight();
        //The handle view
        View handleLayout=findViewById(handleId);
        measureChild(handleLayout,widthMeasureSpec,  heightMeasureSpec);

        int layoutHeight = measuredHeight - handleLayout.getMeasuredHeight() - paddingTop - paddingBottom;

        //The top panel
        View startLayout=getChildAt(0);
        //First measure the top panel
        float startLayoutHeight = slidingWeight * layoutHeight;
        startLayout.measure(widthMeasureSpec,  MeasureSpec.makeMeasureSpec((int) startLayoutHeight,MeasureSpec.EXACTLY));

        //Measured the bottom view
        int childCount = getChildCount();
        View endLayout=getChildAt(childCount-1);
        float endLayoutHeight = (1f-slidingWeight) * layoutHeight;
        endLayout.measure(widthMeasureSpec,  MeasureSpec.makeMeasureSpec((int) endLayoutHeight,MeasureSpec.EXACTLY));
    }


    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        View handleLayout=findViewById(handleId);
        View startLayout=getChildAt(0);

        int offsetTop = getPaddingTop();
        //Layout the top view
        startLayout.layout(paddingLeft,offsetTop,
                measuredWidth-paddingRight, offsetTop+startLayout.getMeasuredHeight());
        offsetTop+=startLayout.getMeasuredHeight();
        //Layout the handle view
        handleLayout.layout(paddingLeft, offsetTop,
                measuredWidth-paddingRight, offsetTop+handleLayout.getMeasuredHeight());
        offsetTop+=handleLayout.getMeasuredHeight();
        //Layout the bottom view
        int childCount = getChildCount();
        View endLayout=getChildAt(childCount-1);
        endLayout.layout(paddingLeft,offsetTop,
                measuredWidth-paddingRight,offsetTop+endLayout.getMeasuredHeight());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        if(MotionEvent.ACTION_DOWN==action){
            lastX=ev.getX();
            lastY=ev.getY();
        } else if(MotionEvent.ACTION_MOVE==action){
            if(isBeginDragged(ev)){
                return true;
            }
        } else if(MotionEvent.ACTION_UP==action||MotionEvent.ACTION_CANCEL==action){
            isBeingDragged = false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        if (MotionEvent.ACTION_DOWN == action) {
            lastY = event.getY();
        } else if (MotionEvent.ACTION_MOVE == action) {
            //If drag the handle. start update the view
            if (isBeingDragged||isBeginDragged(event)) {
                int offsetY = (int) (event.getY() - lastY);
                lastY = event.getY();
                View startLayout = getChildAt(0);
                View handleLayout = findViewById(handleId);
                View endLayout = getChildAt(getChildCount() - 1);
                //If move down out of bounds
                if (0 > offsetY) {
                    if (handleLayout.getTop() <= startLayout.getMinimumHeight()) {
                        offsetY = 0;
                    } else if (startLayout.getMinimumHeight() > handleLayout.getTop() + offsetY) {
                        offsetY = startLayout.getMinimumHeight() - handleLayout.getTop();
                    }
                }
                //If move up out of bounds
                if (0 < offsetY) {
                    float endOffset = getMeasuredHeight() - endLayout.getTop();
                    if (endOffset <= endLayout.getMinimumHeight()) {
                        offsetY = 0;
                    } else if (endLayout.getMinimumHeight() > endOffset - offsetY) {
                        offsetY = (int) (endOffset - endLayout.getMinimumHeight());
                    }
                }
                //move the handle
                handleLayout.offsetTopAndBottom(offsetY);
                endLayout.offsetTopAndBottom(offsetY);
                LayoutParams layoutParams = startLayout.getLayoutParams();
                layoutParams.height = offsetY+startLayout.getMeasuredHeight();
                LayoutParams endLayoutParams = endLayout.getLayoutParams();
                endLayoutParams.height = offsetY+endLayout.getMeasuredHeight();
                //update the weight of this view
                int measuredHeight = getMeasuredHeight();
                float layoutHeight = measuredHeight - handleLayout.getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
                float slidingWeight = layoutParams.height*1f / layoutHeight;
                this.slidingWeight = slidingWeight;
                requestLayout();
            }
        } else if (MotionEvent.ACTION_UP == action || MotionEvent.ACTION_CANCEL == action) {
            isBeingDragged = false;
        }
        return true;
    }


    /**
     * Check if you touch in handle area, if the finger move offset over scale touch slop then start drag
     */
    private boolean isBeginDragged(MotionEvent ev) {
        float x = ev.getX();
        float y = ev.getY();
        float offsetX = Math.abs(x - lastX);
        float offsetY = Math.abs(y - lastY);
        View handleLayout = findViewById(handleId);
                tempRect.set(handleLayout.getLeft(), handleLayout.getTop(), handleLayout.getRight(), handleLayout.getBottom());
        if (tempRect.contains(x,y)) {
            if (offsetX > scaledTouchSlop || offsetY > scaledTouchSlop) {
                //start drag
                isBeingDragged = true;
                return true;
            }
        }
        return false;
    }
}
