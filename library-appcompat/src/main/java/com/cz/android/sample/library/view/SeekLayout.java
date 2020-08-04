package com.cz.android.sample.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cz.android.sample.library.R;


/**
 * @author Created by cz
 * @date 2020-01-27 18:59
 * @email bingo110@126.com
 */
public class SeekLayout extends LinearLayout {
    /**
     * seekBar progress change listener
     */
    private OnProgressChangeListener listener;

    public SeekLayout(Context context) {
        this(context,null,0);
    }

    public SeekLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SeekLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.VERTICAL);
        inflate(context, R.layout.sample_seek_layout,this);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SeekLayout);
        setSeekLayoutTitle(a.getString(R.styleable.SeekLayout_seek_title));
        setSeekLayoutMax(a.getInteger(R.styleable.SeekLayout_seek_max,100));
        a.recycle();


        final TextView titleTextView= findViewById(R.id.titleText);
        final CharSequence title = titleTextView.getText();
        SeekBar seekBar=findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                titleTextView.setText(title+":"+i);
                if(null!=listener){
                    listener.onProgressChanged(seekBar,i,b);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    /**
     * set max value to seek bar
     * @param max
     */
    public void setSeekLayoutMax(int max) {
        SeekBar seekBar=findViewById(R.id.seekBar);
        seekBar.setMax(max);
    }

    /**
     * set title text
     * @param text
     */
    public void setSeekLayoutTitle(String text) {
        TextView titleTextView= findViewById(R.id.titleText);
        titleTextView.setText(text);
    }

    public void setOnProgressChangeListener(OnProgressChangeListener listener){
        this.listener=listener;
    }

    /**
     * Interface responsible for receiving progress value change event
     */
    public interface OnProgressChangeListener{
        /**
         * when progress changed
         * @param seekBar
         * @param i
         * @param fromUser
         */
        void onProgressChanged(SeekBar seekBar, int i, boolean fromUser);
    }

}
