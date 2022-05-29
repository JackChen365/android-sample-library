package com.github.jackchen.android.core.window;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import com.github.jackchen.android.core.component.ComponentManager;
import com.github.jackchen.android.core.component.ComponentContainer;
import java.util.Set;

/**
 * @author Created by cz
 * @date 2020-01-28 21:30
 * @email bingo110@126.com
 */
public class AppcompatWindowDelegate implements WindowDelegate {

    @Override
    public View onCreateView(AppCompatActivity context, Object object, ViewGroup parentView, View view,
            Bundle saveInstance) {
        Set<ComponentContainer> componentContainerSet = ComponentManager.INSTANCE.getComponentContainerSet();
        View componentView = view;
        for (ComponentContainer componentContainer : componentContainerSet) {
            //If this component is available
            if (componentContainer.isComponentAvailable(object)) {
                //We use different component change the view
                componentView = componentContainer.getComponentView(context, object, parentView, componentView);
                //Here we call onCreateView function
                componentContainer.onCreatedView(context, componentContainer, componentView);
            }
        }
        return componentView;
    }
}
