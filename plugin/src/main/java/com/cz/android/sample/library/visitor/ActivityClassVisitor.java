package com.cz.android.sample.library.visitor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author Created by cz
 * @date 2020-05-17 15:12
 * @email bingo110@126.com
 */
public class ActivityClassVisitor extends ClassVisitor {
    private static final String SUPER_CLASS_NAME="com/cz/android/sample/library/appcompat/SampleAppCompatActivity";
    public ActivityClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM6, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        //Here we change the super class name to our appcompat class.
        if("androidx/appcompat/app/AppCompatActivity".equals(superName)|| "androidx/fragment/app/FragmentActivity".equals(superName)){
            super.visit(version, access, name, signature, SUPER_CLASS_NAME, interfaces);
        } else {
            super.visit(version, access, name, signature, superName, interfaces);
        }
    }
}
