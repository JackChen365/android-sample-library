package com.cz.android.sample.window;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import com.cz.android.sample.component.CompanionComponentContainer;
import com.cz.android.sample.component.ComponentContainer;
import com.cz.android.sample.component.ComponentManager;
import java.util.Set;

/**
 * @author Created by cz
 * @date 2020-01-28 21:30
 * @email bingo110@126.com
 */
public class AppcompatWindowDelegate implements WindowDelegate {

    @Override
    public View onCreateView(AppCompatActivity context, Object object, ViewGroup parentView, View view, Bundle saveInstance) {
        ComponentManager componentManager = ComponentManager.getInstance();
        Set<ComponentContainer> componentContainerSet = componentManager.getComponentContainerSet();
        View componentView=view;
        for(ComponentContainer componentContainer:componentContainerSet){
            //If this component is available
            if(componentContainer.isComponentAvailable(object)){
                //process companionComponentContainer
                if(componentContainer instanceof CompanionComponentContainer){
                    CompanionComponentContainer companionComponentContainer = (CompanionComponentContainer) componentContainer;
                    if(!companionComponentContainer.isComponentCreated()){
                        componentView=companionComponentContainer.getCompanionComponent(context, object, parentView, componentView,saveInstance);
                        companionComponentContainer.setComponentCreated();
                    }
                }
                //We use different component change the view
                componentView = componentContainer.getComponentView(context,object, parentView,componentView,saveInstance);
                //Here we call onCreateView function
                componentContainer.onCreatedView(context,componentContainer,componentView);
            }
        }
        //Restore create state
        for(ComponentContainer componentContainer:componentContainerSet) {
            if (componentContainer instanceof CompanionComponentContainer) {
                CompanionComponentContainer companionComponentContainer = (CompanionComponentContainer) componentContainer;
                companionComponentContainer.resetComponent();
            }
        }
        return componentView;
    }
}
