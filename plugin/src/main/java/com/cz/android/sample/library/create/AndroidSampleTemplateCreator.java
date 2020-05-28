package com.cz.android.sample.library.create;


import com.cz.android.sample.api.AndroidSampleConstant;
import com.cz.android.sample.api.item.CategoryItem;
import com.cz.android.sample.api.item.RegisterItem;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;


public class AndroidSampleTemplateCreator implements Opcodes {

    public static void create(File outputFile, String classPath,
                              List<CategoryItem> categoryList,
                              List<RegisterItem> registerList,
                              List<String> functionList,
                              List<String> componentList,
                              List<String> processorList,
                              List<String> testCaseList,
                              String mainComponent) throws Exception {
        ClassWriter classWriter = new ClassWriter(0);
        FieldVisitor fieldVisitor;
        MethodVisitor methodVisitor;

        classWriter.visit(V1_8, ACC_PUBLIC | ACC_FINAL | ACC_SUPER, classPath, null, "java/lang/Object", null);
        {
            fieldVisitor = classWriter.visitField(ACC_FINAL, "registerList", "Ljava/util/List;", "Ljava/util/List<Lcom/cz/android/sample/api/item/RegisterItem;>;", null);
            fieldVisitor.visitEnd();
        }
        {
            fieldVisitor = classWriter.visitField(ACC_FINAL, "categoryList", "Ljava/util/List;", "Ljava/util/List<Lcom/cz/android/sample/api/item/CategoryItem;>;", null);
            fieldVisitor.visitEnd();
        }
        {
            fieldVisitor = classWriter.visitField(ACC_FINAL, "functionList", "Ljava/util/List;", "Ljava/util/List<Ljava/lang/String;>;", null);
            fieldVisitor.visitEnd();
        }
        {
            fieldVisitor = classWriter.visitField(ACC_FINAL, "componentList", "Ljava/util/List;", "Ljava/util/List<Ljava/lang/String;>;", null);
            fieldVisitor.visitEnd();
        }
        {
            fieldVisitor = classWriter.visitField(ACC_FINAL, "processorList", "Ljava/util/List;", "Ljava/util/List<Ljava/lang/String;>;", null);
            fieldVisitor.visitEnd();
        }
        {
            fieldVisitor = classWriter.visitField(ACC_FINAL, "testCaseList", "Ljava/util/List;", "Ljava/util/List<Ljava/lang/String;>;", null);
            fieldVisitor.visitEnd();
        }
        {
            fieldVisitor = classWriter.visitField(ACC_FINAL, "mainComponent", "Ljava/lang/String;", null, mainComponent);
            fieldVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            methodVisitor.visitLabel(label0);
            methodVisitor.visitLineNumber(32, label0);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            Label label1 = new Label();
            methodVisitor.visitLabel(label1);
            methodVisitor.visitLineNumber(16, label1);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitTypeInsn(NEW, "java/util/ArrayList");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false);
            methodVisitor.visitFieldInsn(PUTFIELD, classPath, "registerList", "Ljava/util/List;");
            Label label2 = new Label();
            methodVisitor.visitLabel(label2);
            methodVisitor.visitLineNumber(18, label2);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitTypeInsn(NEW, "java/util/ArrayList");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false);
            methodVisitor.visitFieldInsn(PUTFIELD, classPath, "categoryList", "Ljava/util/List;");
            Label label3 = new Label();
            methodVisitor.visitLabel(label3);
            methodVisitor.visitLineNumber(20, label3);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitTypeInsn(NEW, "java/util/ArrayList");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false);
            methodVisitor.visitFieldInsn(PUTFIELD, classPath, "functionList", "Ljava/util/List;");
            Label label4 = new Label();
            methodVisitor.visitLabel(label4);
            methodVisitor.visitLineNumber(22, label4);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitTypeInsn(NEW, "java/util/ArrayList");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false);
            methodVisitor.visitFieldInsn(PUTFIELD, classPath, "componentList", "Ljava/util/List;");
            Label label5 = new Label();
            methodVisitor.visitLabel(label5);
            methodVisitor.visitLineNumber(24, label5);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitTypeInsn(NEW, "java/util/ArrayList");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false);
            methodVisitor.visitFieldInsn(PUTFIELD, classPath, "processorList", "Ljava/util/List;");
            Label label6 = new Label();
            methodVisitor.visitLabel(label6);
            methodVisitor.visitLineNumber(26, label6);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitTypeInsn(NEW, "java/util/ArrayList");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false);
            methodVisitor.visitFieldInsn(PUTFIELD, classPath, "testCaseList", "Ljava/util/List;");

            if(null!=mainComponent){
                Label label7 = new Label();
                methodVisitor.visitLabel(label7);
                methodVisitor.visitLineNumber(28, label7);
                methodVisitor.visitVarInsn(ALOAD, 0);
                methodVisitor.visitLdcInsn(mainComponent);
                methodVisitor.visitFieldInsn(PUTFIELD, classPath, "mainComponent", "Ljava/lang/String;");
            }

            for(CategoryItem categoryItem: categoryList){
                Label label9 = new Label();
                methodVisitor.visitLabel(label9);
                methodVisitor.visitLineNumber(34, label9);
                methodVisitor.visitVarInsn(ALOAD, 0);
                methodVisitor.visitFieldInsn(GETFIELD, classPath, "categoryList", "Ljava/util/List;");
                methodVisitor.visitTypeInsn(NEW, "com/cz/android/sample/api/item/CategoryItem");
                methodVisitor.visitInsn(DUP);
                if(AndroidSampleConstant.REF_TYPE==categoryItem.type){
                    methodVisitor.visitLdcInsn(new Integer(categoryItem.titleRes));
                    methodVisitor.visitLdcInsn(new Integer(categoryItem.descRes));
                    methodVisitor.visitLdcInsn(new Integer(categoryItem.categoryRes));
                    methodVisitor.visitLdcInsn(new Integer(categoryItem.priority));
                    methodVisitor.visitLdcInsn(new Integer(categoryItem.type));
                    methodVisitor.visitMethodInsn(INVOKESPECIAL, "com/cz/android/sample/api/item/CategoryItem", "<init>", "(IIIII)V", false);
                } else {
                    methodVisitor.visitLdcInsn(null==categoryItem.title?"":categoryItem.title);
                    methodVisitor.visitLdcInsn(null==categoryItem.desc?"":categoryItem.desc);
                    if(null==categoryItem.category){
                        methodVisitor.visitLdcInsn(AndroidSampleConstant.CATEGORY_ROOT);
                    } else {
                        methodVisitor.visitLdcInsn(categoryItem.category);
                    }
                    methodVisitor.visitLdcInsn(new Integer(categoryItem.priority));
                    methodVisitor.visitMethodInsn(INVOKESPECIAL, "com/cz/android/sample/api/item/CategoryItem", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V", false);
                }
                methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
                methodVisitor.visitInsn(POP);
            }

            for(RegisterItem registerItem: registerList){
                Label label10 = new Label();
                methodVisitor.visitLabel(label10);
                methodVisitor.visitLineNumber(36, label10);
                methodVisitor.visitVarInsn(ALOAD, 0);
                methodVisitor.visitFieldInsn(GETFIELD, classPath, "registerList", "Ljava/util/List;");
                methodVisitor.visitTypeInsn(NEW, "com/cz/android/sample/api/item/RegisterItem");
                methodVisitor.visitInsn(DUP);
                String className = registerItem.className;
                String classDesc = "L"+className.replace(".", "/")+";";
                if(AndroidSampleConstant.REF_TYPE==registerItem.type){
                    methodVisitor.visitLdcInsn(new Integer(registerItem.titleRes));
                    methodVisitor.visitLdcInsn(new Integer(registerItem.descRes));
                    methodVisitor.visitLdcInsn(Type.getType(classDesc));
                    methodVisitor.visitLdcInsn(new Integer(registerItem.categoryRes));
                    methodVisitor.visitLdcInsn(new Integer(registerItem.priority));
                    methodVisitor.visitLdcInsn(registerItem.type);
                    methodVisitor.visitMethodInsn(INVOKESPECIAL, "com/cz/android/sample/api/item/RegisterItem", "<init>", "(IILjava/lang/Class;III)V", false);
                } else {
                    methodVisitor.visitLdcInsn(null==registerItem.title?"":registerItem.title);
                    methodVisitor.visitLdcInsn(null==registerItem.desc?"":registerItem.desc);
                    methodVisitor.visitLdcInsn(Type.getType(classDesc));
                    if(null==registerItem.category) {
                        methodVisitor.visitLdcInsn(AndroidSampleConstant.CATEGORY_ROOT);
                    } else {
                        methodVisitor.visitLdcInsn(registerItem.category);
                    }
                    methodVisitor.visitLdcInsn(new Integer(registerItem.priority));
                    methodVisitor.visitMethodInsn(INVOKESPECIAL, "com/cz/android/sample/api/item/RegisterItem", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Class;Ljava/lang/String;I)V", false);
                }
                methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
                methodVisitor.visitInsn(POP);
            }

            if(null!=functionList){
                for(String function:functionList){
                    Label label12 = new Label();
                    methodVisitor.visitLabel(label12);
                    methodVisitor.visitLineNumber(39, label12);
                    methodVisitor.visitVarInsn(ALOAD, 0);
                    methodVisitor.visitFieldInsn(GETFIELD, classPath, "functionList", "Ljava/util/List;");
                    methodVisitor.visitLdcInsn(function);
                    methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
                    methodVisitor.visitInsn(POP);
                }
            }

            if(null!=componentList){
                for(String component:componentList){
                    Label label13 = new Label();
                    methodVisitor.visitLabel(label13);
                    methodVisitor.visitLineNumber(41, label13);
                    methodVisitor.visitVarInsn(ALOAD, 0);
                    methodVisitor.visitFieldInsn(GETFIELD, classPath, "componentList", "Ljava/util/List;");
                    methodVisitor.visitLdcInsn(component);
                    methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
                    methodVisitor.visitInsn(POP);
                }
            }

            if(null!=processorList){
                for(String processor:processorList){
                    Label label14 = new Label();
                    methodVisitor.visitLabel(label14);
                    methodVisitor.visitLineNumber(43, label14);
                    methodVisitor.visitVarInsn(ALOAD, 0);
                    methodVisitor.visitFieldInsn(GETFIELD, classPath, "processorList", "Ljava/util/List;");
                    methodVisitor.visitLdcInsn(processor);
                    methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
                    methodVisitor.visitInsn(POP);
                }
            }

            if(null!=testCaseList){
                for(String testCase:testCaseList){
                    Label label15 = new Label();
                    methodVisitor.visitLabel(label15);
                    methodVisitor.visitLineNumber(45, label15);
                    methodVisitor.visitVarInsn(ALOAD, 0);
                    methodVisitor.visitFieldInsn(GETFIELD, classPath, "testCaseList", "Ljava/util/List;");
                    methodVisitor.visitLdcInsn(testCase);
                    methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
                    methodVisitor.visitInsn(POP);
                }
            }

            Label label16 = new Label();
            methodVisitor.visitLabel(label16);
            methodVisitor.visitLineNumber(46, label16);
            methodVisitor.visitInsn(RETURN);
            Label label17 = new Label();
            methodVisitor.visitLabel(label17);
            methodVisitor.visitLocalVariable("this", "L"+classPath+";", null, label0, label17, 0);
            methodVisitor.visitMaxs(9, 1);
            methodVisitor.visitEnd();
        }
        classWriter.visitEnd();

        File classFile=new File(outputFile, classPath+".class");
        try(FileOutputStream fos = new FileOutputStream(classFile)){
            fos.write(classWriter.toByteArray());
        }


    }
}
