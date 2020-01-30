package com.cz.android.sample.library.component.memory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.cz.android.sample.component.ComponentContainer;
import com.cz.android.sample.library.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * @author Created by cz
 * @date 2020-01-29 19:06
 * @email bingo110@126.com
 */
public class SampleMemoryComponent implements ComponentContainer {

    @Override
    public boolean isComponentAvailable(@NonNull Object object) {
        SampleMemory sampleDocument = object.getClass().getAnnotation(SampleMemory.class);
        return null!=sampleDocument&&sampleDocument.value();
    }

    @Override
    public View getComponentView(@NonNull FragmentActivity context, @NonNull Object object, @NonNull ViewGroup parentView, @NonNull View view) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View createView = layoutInflater.inflate(R.layout.sample_memory_layout, parentView, false);
        final View memoryView = createView.findViewById(R.id.sampleMemoryView);
        final ViewGroup sampleMemoryContainer = createView.findViewById(R.id.sampleMemoryContainer);
        final FloatingActionButton sampleMemoryButton = createView.findViewById(R.id.sampleMemoryButton);
        sampleMemoryButton.setSelected(true);
        sampleMemoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sampleMemoryButton.setSelected(!sampleMemoryButton.isSelected());
                memoryView.setVisibility(sampleMemoryButton.isSelected()?View.VISIBLE:View.INVISIBLE);
            }
        });
        sampleMemoryContainer.addView(view);
        return createView;
    }

    @Override
    public void onCreatedView(@NonNull FragmentActivity context, @NonNull Object object, @NonNull View view) {
    }

    @Override
    public int getComponentPriority() {
        return 1;
    }
}
