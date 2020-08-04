package com.cz.android.sample.library.visitor;

import com.cz.android.sample.api.Exclude;
import com.cz.android.sample.api.MainComponent;
import com.cz.android.sample.api.RefRegister;
import com.cz.android.sample.api.Register;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by cz
 * @date 2020-05-17 15:12
 * @email bingo110@126.com
 */
public class SampleClassVisitor extends ClassVisitor {
    private static final String SUPER_CLASS_NAME="com/cz/android/sample/library/appcompat/SampleAppCompatActivity";

    private static final String ANDROIDX_COMPAT_ACTIVITY_CLASS_NAME ="androidx/appcompat/app/AppCompatActivity";
    private static final String ANDROIDX_FRAGMENT_ACTIVITY_CLASS_NAME ="androidx/fragment/app/FragmentActivity";
    private static final String ANDROIDX_FRAGMENT_CLASS_NAME ="androidx/fragment/app/Fragment";

    private static final String APPCOMPAT_COMPAT_ACTIVITY_CLASS_NAME ="android/support/v7/app/AppCompatActivity";
    private static final String APPCOMPAT_FRAGMENT_ACTIVITY_CLASS_NAME ="android/support/v4/app/FragmentActivity";
    private static final String APPCOMPAT_FRAGMENT_CLASS_NAME ="android/support/v4/app/Fragment";



    private static final List<String> SAMPLE_CLASS_LIST= new ArrayList<>();
    private static final List<String> EXCLUDE_ANNOTATION_LIST= new ArrayList<>();

    static {
        SAMPLE_CLASS_LIST.add(ANDROIDX_COMPAT_ACTIVITY_CLASS_NAME);
        SAMPLE_CLASS_LIST.add(ANDROIDX_FRAGMENT_ACTIVITY_CLASS_NAME);
        SAMPLE_CLASS_LIST.add(ANDROIDX_FRAGMENT_CLASS_NAME);

        SAMPLE_CLASS_LIST.add(APPCOMPAT_COMPAT_ACTIVITY_CLASS_NAME);
        SAMPLE_CLASS_LIST.add(APPCOMPAT_FRAGMENT_ACTIVITY_CLASS_NAME);
        SAMPLE_CLASS_LIST.add(APPCOMPAT_FRAGMENT_CLASS_NAME);

        EXCLUDE_ANNOTATION_LIST.add("com/cz/android/sample/main/MainSampleComponentFactory");
        EXCLUDE_ANNOTATION_LIST.add("L"+ Exclude.class.getName().replace('.','/') +";");
        EXCLUDE_ANNOTATION_LIST.add("L"+ MainComponent.class.getName().replace('.','/') +";");
        EXCLUDE_ANNOTATION_LIST.add("L"+ RefRegister.class.getName().replace('.','/') +";");
        EXCLUDE_ANNOTATION_LIST.add("L"+ Register.class.getName().replace('.','/') +";");
    }

    private String className;

    public SampleClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM6, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        //Here we change the super class name to our appcompat class.
        if(SAMPLE_CLASS_LIST.contains(superName)){
            className=name;
        }
        if(EXCLUDE_ANNOTATION_LIST.contains(superName)){
            className=null;
        }
        if(null!=interfaces){
            for(String interfaceName:interfaces){
                if(EXCLUDE_ANNOTATION_LIST.contains(interfaceName)){
                    className=null;
                    break;
                }
            }
        }
        //Here we change the super class name to our appcompat class.
        if(ANDROIDX_COMPAT_ACTIVITY_CLASS_NAME.equals(superName) || ANDROIDX_FRAGMENT_ACTIVITY_CLASS_NAME.equals(superName)){
            super.visit(version, access, name, signature, SUPER_CLASS_NAME, interfaces);
        } else if(APPCOMPAT_COMPAT_ACTIVITY_CLASS_NAME.equals(superName) || APPCOMPAT_FRAGMENT_ACTIVITY_CLASS_NAME.equals(superName)){
            super.visit(version, access, name, signature, SUPER_CLASS_NAME, interfaces);
        } else {
            super.visit(version, access, name, signature, superName, interfaces);
        }
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if(EXCLUDE_ANNOTATION_LIST.contains(desc)){
            className=null;
        }
        return super.visitAnnotation(desc, visible);
    }

    public boolean isDemonstrable(){
        return null!=className;
    }

    public String getClassName() {
        return className.replace('/','.');
    }
}
