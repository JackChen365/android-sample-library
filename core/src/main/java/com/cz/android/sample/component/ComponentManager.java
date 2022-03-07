package com.cz.android.sample.component;

import androidx.annotation.NonNull;
import com.cz.android.sample.extension.ExtensionHandler;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Created by cz
 * @date 2020-01-27 21:09
 * @email bingo110@126.com
 */
public class ComponentManager implements ExtensionHandler<ComponentContainer> {
    private static final String EXTENSION_CLASS_NAME = ComponentContainer.class.getName();
    /**
     * A singleton object
     */
    private static final ComponentManager componentManager = new ComponentManager();
    /**
     * The extra component list that will decorate every sample
     */
    private Set<ComponentContainer> componentContainerSet = new TreeSet<>(new Comparator<ComponentContainer>() {
        @Override
        public int compare(ComponentContainer c1, ComponentContainer c2) {
            int i = c2.getComponentPriority() - c1.getComponentPriority();
            return 0 == i ? -1 : i;
        }
    });

    public static ComponentManager getInstance() {
        return componentManager;
    }

    private ComponentManager() {
    }

    @Override
    public boolean handle(@NonNull final String className, @NonNull final String superClass,
            @NonNull final List<String> interfaces) {
        if (interfaces.contains(EXTENSION_CLASS_NAME)) {
            try {
                final Class<?> clazz = Class.forName(className);
                ComponentContainer componentContainer = (ComponentContainer) clazz.newInstance();
                register(componentContainer);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    @Override public void register(final ComponentContainer extension) {
        componentContainerSet.add(extension);
    }

    @Override public void unregister(final ComponentContainer extension) {
        componentContainerSet.remove(extension);
    }

    /**
     * Relate companion component
     */
    public void relateCompanionComponent() {
        Set<ComponentContainer> componentContainerSet = componentManager.getComponentContainerSet();
        for (ComponentContainer componentContainer : componentContainerSet) {
            if (componentContainer instanceof CompanionComponentContainer) {
                CompanionComponentContainer companionComponentContainer = (CompanionComponentContainer) componentContainer;
                Class<CompanionComponentContainer>[] companionComponentClassArray = companionComponentContainer
                        .getCompanionComponent();
                for (Class<CompanionComponentContainer> clazz : companionComponentClassArray) {
                    for (ComponentContainer component : componentContainerSet) {
                        if (clazz.isInstance(component)) {
                            companionComponentContainer.addCompanionComponent((CompanionComponentContainer) component);
                        }
                    }
                }
            }
        }
    }

    public Set<ComponentContainer> getComponentContainerSet() {
        return componentContainerSet;
    }
}
