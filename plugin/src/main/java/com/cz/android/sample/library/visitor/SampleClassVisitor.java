package com.cz.android.sample.library.visitor;

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

    private static final String SUPER_COMPAT_ACTIVITY_CLASS_NAME="androidx/appcompat/app/AppCompatActivity";
    private static final String SUPER_FRAGMENT_ACTIVITY_CLASS_NAME="androidx/appcompat/app/AppCompatActivity";
    private static final String SUPER_FRAGMENT_CLASS_NAME="androidx/fragment/app/Fragment";

    private static final List<String> SAMPLE_CLASS_LIST= new ArrayList<>();
    private static final List<String> EXCLUDE_ANNOTATION_LIST= new ArrayList<>();

    static {
        SAMPLE_CLASS_LIST.add(SUPER_COMPAT_ACTIVITY_CLASS_NAME);
        SAMPLE_CLASS_LIST.add(SUPER_FRAGMENT_ACTIVITY_CLASS_NAME);
        SAMPLE_CLASS_LIST.add(SUPER_FRAGMENT_CLASS_NAME);

        EXCLUDE_ANNOTATION_LIST.add("com/cz/android/sample/main/MainSampleComponentFactory");
        EXCLUDE_ANNOTATION_LIST.add("Lcom/cz/android/sample/api/Exclude;");
        EXCLUDE_ANNOTATION_LIST.add("Lcom/cz/android/sample/api/MainComponent;");
        EXCLUDE_ANNOTATION_LIST.add("Lcom/cz/android/sample/api/RefRegister;");
        EXCLUDE_ANNOTATION_LIST.add("Lcom/cz/android/sample/api/Register;");
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
        if("androidx/appcompat/app/AppCompatActivity".equals(superName) || "androidx/fragment/app/FragmentActivity".equals(superName)){
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
