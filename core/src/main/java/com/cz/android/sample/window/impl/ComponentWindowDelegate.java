package com.cz.android.sample.window.impl;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.cz.android.sample.component.ComponentContainer;
import com.cz.android.sample.component.ComponentManager;
import com.cz.android.sample.window.WindowDelegate;

import java.util.Set;

/**
 * @author Created by cz
 * @date 2020-01-28 21:30
 * @email bingo110@126.com
 */
public class ComponentWindowDelegate implements WindowDelegate {
    @NonNull
    @Override
    public View onCreateView(@NonNull FragmentActivity context,@NonNull Object object,@NonNull ViewGroup parentView, @NonNull View view) {
        ComponentManager componentManager = ComponentManager.getInstance();
        Set<ComponentContainer> componentContainerSet = componentManager.getComponentContainerSet();
        View componentView=view;
        for(ComponentContainer componentContainer:componentContainerSet){
            //If this component is available
            if(componentContainer.isComponentAvailable(object)){
                //We use different component change the view
                componentView = componentContainer.getComponentView(context,object, parentView,componentView);
                //Here we call onCreateView function
                componentContainer.onCreatedView(context,componentContainer,componentView);
            }
        }
        return componentView;
    }
}
