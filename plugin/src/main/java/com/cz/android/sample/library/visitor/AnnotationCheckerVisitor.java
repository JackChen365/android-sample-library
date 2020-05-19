package com.cz.android.sample.library.visitor;


import com.cz.android.sample.api.ActionProcessor;
import com.cz.android.sample.api.AndroidSampleConstant;
import com.cz.android.sample.api.Category;
import com.cz.android.sample.api.Component;
import com.cz.android.sample.api.Function;
import com.cz.android.sample.api.MainComponent;
import com.cz.android.sample.api.RefCategory;
import com.cz.android.sample.api.RefRegister;
import com.cz.android.sample.api.Register;
import com.cz.android.sample.api.TestCase;
import com.cz.android.sample.api.item.CategoryItem;
import com.cz.android.sample.api.item.RegisterItem;
import com.cz.android.sample.library.checker.AnnotationChecker;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by cz
 * @date 2020-05-17 10:01
 * @email bingo110@126.com
 */
public class AnnotationCheckerVisitor extends ClassVisitor {
    public static final String ANNOTATION_REGISTER= Register.class.getName();
    public static final String ANNOTATION_REF_REGISTER= RefRegister.class.getName();
    public static final String ANNOTATION_CATEGORY= Category.class.getName();
    public static final String ANNOTATION_REF_CATEGORY= RefCategory.class.getName();
    public static final String ANNOTATION_COMPONENT= Component.class.getName();
    public static final String ANNOTATION_FUNCTION= Function.class.getName();
    public static final String ANNOTATION_MAIN_COMPONENT= MainComponent.class.getName();
    public static final String ANNOTATION_ACTION_PROCESSOR= ActionProcessor.class.getName();
    public static final String ANNOTATION_TEST_CASE= TestCase.class.getName();


    private static final List<AnnotationChecker> ANNOTATION_CHECKER_LIST =new ArrayList<>();
    private AnnotationChecker annotationChecker=null;
    private CategoryItem categoryItem;
    private RegisterItem registerItem;
    private String className;

    static{
        List<String> annotationList=new ArrayList<>();
        annotationList.add(ANNOTATION_REGISTER);
        annotationList.add(ANNOTATION_REF_REGISTER);
        annotationList.add(ANNOTATION_CATEGORY);
        annotationList.add(ANNOTATION_REF_CATEGORY);
        annotationList.add(ANNOTATION_COMPONENT);
        annotationList.add(ANNOTATION_FUNCTION);
        annotationList.add(ANNOTATION_MAIN_COMPONENT);
        annotationList.add(ANNOTATION_ACTION_PROCESSOR);
        annotationList.add(ANNOTATION_TEST_CASE);

        annotationList.forEach(className-> ANNOTATION_CHECKER_LIST.add(new AnnotationChecker() {
            @Override
            public String getAnnotation() {
                return className;
            }
        }));
    }

    public AnnotationCheckerVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM5, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.className=name.replace("/",".");
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        for(AnnotationChecker checker:ANNOTATION_CHECKER_LIST){
            if(checker.isAnnotation(desc)){
                annotationChecker=checker;
                break;
            }
        }
        AnnotationVisitor annotationVisitor = super.visitAnnotation(desc, visible);
        if(("L"+ANNOTATION_REF_CATEGORY.replace(".","/")+";").equals(desc)){
            categoryItem = new CategoryItem();
            categoryItem.type= AndroidSampleConstant.REF_TYPE;
            return new AnnotationVisitor(Opcodes.ASM5,annotationVisitor) {
                @Override
                public void visit(String name, Object value) {
                    super.visit(name, value);
                    if("title".equals(name)){
                        categoryItem.titleRes= (int) value;
                    } else if("desc".equals(name)){
                        categoryItem.descRes= (int) value;
                    } else if("priority".equals(name)){
                        categoryItem.priority= (int) value;
                    } else if("category".equals(name)){
                        categoryItem.categoryRes= (int) value;
                    }
                }
            };
        } else if(("L"+ANNOTATION_CATEGORY.replace(".","/")+";").equals(desc)){
            categoryItem = new CategoryItem();
            categoryItem.type= AndroidSampleConstant.NORMAL_TYPE;
            return new AnnotationVisitor(Opcodes.ASM5,annotationVisitor) {
                @Override
                public void visit(String name, Object value) {
                    super.visit(name, value);
                    if("title".equals(name)){
                        categoryItem.title= (String) value;
                    } else if("desc".equals(name)){
                        categoryItem.desc= (String) value;
                    } else if("priority".equals(name)){
                        categoryItem.priority= (int) value;
                    } else if("category".equals(name)){
                        categoryItem.category= (String) value;
                    }
                }
            };
        } else if(("L"+ANNOTATION_REF_REGISTER.replace(".","/")+";").equals(desc)){
            registerItem=new RegisterItem();
            registerItem.className=className;
            registerItem.type= AndroidSampleConstant.REF_TYPE;
            return new AnnotationVisitor(Opcodes.ASM5,annotationVisitor) {
                @Override
                public void visit(String name, Object value) {
                    super.visit(name, value);
                    if("title".equals(name)){
                        registerItem.titleRes= (int) value;
                    } else if("desc".equals(name)){
                        registerItem.descRes= (int) value;
                    } else if("priority".equals(name)){
                        registerItem.priority= (int) value;
                    } else if("category".equals(name)){
                        registerItem.categoryRes= (int) value;
                    }
                }
            };
        } else if(("L"+ANNOTATION_REGISTER.replace(".","/")+";").equals(desc)){
            registerItem=new RegisterItem();
            registerItem.className=className;
            registerItem.type= AndroidSampleConstant.NORMAL_TYPE;
            return new AnnotationVisitor(Opcodes.ASM5,annotationVisitor) {
                @Override
                public void visit(String name, Object value) {
                    super.visit(name, value);
                    if("title".equals(name)){
                        registerItem.title= (String) value;
                    } else if("desc".equals(name)){
                        registerItem.desc= (String) value;
                    } else if("priority".equals(name)){
                        registerItem.priority= (int) value;
                    } else if("category".equals(name)){
                        registerItem.category= (String) value;
                    } else if("version".equals(name)){
                    }
                }
            };
        }
        return annotationVisitor;
    }

    public AnnotationChecker getAnnotationChecker() {
        return annotationChecker;
    }

    public CategoryItem getCategoryItem() {
        return categoryItem;
    }

    public RegisterItem getRegisterItem() {
        return registerItem;
    }

    public String getClassName() {
        return className;
    }
}
