package com.cz.android.sample.library.appcompat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ArrayRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Created by cz
 * @date 2020-01-28 18:37
 * @email bingo110@126.com
 */
public class SimpleArrayAdapter<E> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static SimpleArrayAdapter createFromResource(Context context, @ArrayRes int res){
        return new SimpleArrayAdapter(context, context.getResources().getStringArray(res));
    }

    private final LayoutInflater layoutInflater;
    private @LayoutRes int layoutResources;
    private List<E> items= new ArrayList<>();

    public SimpleArrayAdapter(Context context, E[] items){
        this(context, android.R.layout.simple_list_item_1, Arrays.asList(items));
    }

    public SimpleArrayAdapter(Context context, @LayoutRes int layout,E[]  items){
        this(context, layout, Arrays.asList(items));
    }

    public SimpleArrayAdapter(Context context, List<E> items){
        this(context,android.R.layout.simple_list_item_1,items);
    }

    public SimpleArrayAdapter(Context context,@LayoutRes int layout,@NonNull List<E> items){
        this.layoutInflater = LayoutInflater.from(context);
        this.layoutResources = layout;
        this.items.addAll(items);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerView.ViewHolder(layoutInflater.inflate(layoutResources,parent,false)) {};
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;
        E item = getItem(position);
        if (null != item) {
            textView.setText(item.toString());
        }
    }

    public E getItem(int position){
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
