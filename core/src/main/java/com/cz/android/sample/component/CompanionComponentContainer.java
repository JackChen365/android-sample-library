package com.cz.android.sample.component;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by cz
 * @date 2020-01-31 13:03
 * @email chenzhen@okay.cn
 * This component container is used for two different component container that has same work
 * For example the first one will create a ViewPager. So did the second one.
 *
 * Here are the solution. We implement component container as a CompanionComponentContainer
 * Once there has one create a component. The others don't have to do the same.
 */
public abstract class CompanionComponentContainer implements ComponentContainer {
    /**
     * All the companion component
     */
    private List<CompanionComponentContainer> companionComponentContainers=new ArrayList<>();

    /**
     * If the common component is created;
     */
    private boolean isComponentCreated;

    /**
     * Create companion component. When There are one component created. The others won't created again
     * @param context
     * @param object
     * @param parentView
     * @param view
     * @return
     */
    public abstract View onCreateCompanionComponent(@NonNull FragmentActivity context, @NonNull Object object, @NonNull ViewGroup parentView, @NonNull View view);

    /**
     * Return a class array. So that I could add all the companion to this object
     */
    public abstract Class<CompanionComponentContainer>[] getCompanionComponent();

    /**
     * @param context
     * @param object
     * @param parentView
     * @param view
     * @return
     */
    public View getCompanionComponent(@NonNull FragmentActivity context, @NonNull Object object, @NonNull ViewGroup parentView, @NonNull View view){
        View companionComponentView = onCreateCompanionComponent(context, object, parentView, view);
        return companionComponentView;
    }

    public boolean isComponentCreated() {
        return isComponentCreated;
    }

    public void setComponentCreated() {
        if(!isComponentCreated){
            isComponentCreated = true;
            for(CompanionComponentContainer componentContainer:companionComponentContainers){
                componentContainer.setComponentCreated();
            }
        }
    }

    /**
     * Restore created state.
     */
    public void resetComponent(){
        if(isComponentCreated){
            isComponentCreated=false;
            for(CompanionComponentContainer componentContainer:companionComponentContainers){
                componentContainer.resetComponent();
            }
        }
    }

    /**
     * Add one companion. When this object initialize component it will trigger the others
     * @param componentContainer
     */
    public void addCompanionComponent(CompanionComponentContainer componentContainer){
        this.companionComponentContainers.add(componentContainer);
    }

    public List<CompanionComponentContainer> getCompanionComponentContainers() {
        return companionComponentContainers;
    }


}
