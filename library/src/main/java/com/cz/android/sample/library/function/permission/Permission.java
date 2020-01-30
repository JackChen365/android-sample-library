package com.cz.android.sample.library.function.permission;

import java.util.List;

public class Permission {
    public final String name;
    public final boolean granted;
    public final boolean shouldShowRequestPermissionRationale;

    public Permission(String name, boolean granted) {
        this(name, granted, false);
    }

    public Permission(String name, boolean granted, boolean shouldShowRequestPermissionRationale) {
        this.name = name;
        this.granted = granted;
        this.shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale;
    }

    public Permission(List<Permission> permissions) {
        name = combineName(permissions);
        granted = combineGranted(permissions);
        shouldShowRequestPermissionRationale = combineShouldShowRequestPermissionRationale(permissions);
    }

    @Override
    @SuppressWarnings("SimplifiableIfStatement")
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Permission that = (Permission) o;

        if (granted != that.granted) return false;
        if (shouldShowRequestPermissionRationale != that.shouldShowRequestPermissionRationale)
            return false;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (granted ? 1 : 0);
        result = 31 * result + (shouldShowRequestPermissionRationale ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "name='" + name + '\'' +
                ", granted=" + granted +
                ", shouldShowRequestPermissionRationale=" + shouldShowRequestPermissionRationale +
                '}';
    }

    private String combineName(List<Permission> permissions) {
        StringBuilder output=new StringBuilder();
        for(Permission permission:permissions){
            String name=permission.name;
            if (name.length() == 0) {
                output.append(name);
            } else {
                output.append(", ").append(name);
            }
        }
        return output.toString();
    }

    private Boolean combineGranted(List<Permission> permissions) {
        for(Permission permission:permissions){
            if(permission.granted){
                return true;
            }
        }
        return false;
    }

    private Boolean combineShouldShowRequestPermissionRationale(List<Permission> permissions) {
        for(Permission permission:permissions){
            if(permission.shouldShowRequestPermissionRationale){
                return true;
            }
        }
        return false;
    }
}
