package com.binzi.plugin.asm;

import com.binzi.aop.utils.Cost;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * @Author: huangyoubin
 * @Create: 2019/3/31 9:59 PM
 * @Description:
 */
public class CostClassVisitor extends ClassVisitor {

    public CostClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM5, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature,
                                     String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        mv = new AdviceAdapter(Opcodes.ASM5, mv, access, name, desc) {

            private boolean inject = false;

            @Override
            public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                if (Type.getDescriptor(Cost.class).equals(desc)) {
                    inject = true;
                }
                return super.visitAnnotation(desc, visible);
            }

            @Override
            protected void onMethodEnter() {
                if (inject) {
                    //Log.d("CostTime", "========start=========");
                    //TimeCache.setStartTime("newFunc", System.nanoTime());
                    mv.visitLdcInsn("CostTime");
                    mv.visitLdcInsn("========start=========");
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "d", "(Ljava/lang/String;Ljava/lang/String;)V", false);
                    mv.visitLdcInsn(name);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/binzi/plugin/asm/TimeCache", "setStartTime", "(Ljava/lang/String;J)V", false);

                }
            }

            @Override
            protected void onMethodExit(int opcode) {
                // TimeCache.setEndTime("newFunc", System.nanoTime());
                // Log.d("CostTime", TimeCache.getCostTime("newFunc"));
                // Log.d("CostTime", "========end=========");
                if (inject) {
                    mv.visitLdcInsn(name);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/binzi/plugin/asm/TimeCache", "setEndTime", "(Ljava/lang/String;J)V", false);
                    mv.visitLdcInsn("CostTime");
                    mv.visitLdcInsn(name);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/binzi/plugin/asm/TimeCache", "getCostTime", "(Ljava/lang/String;)Ljava/lang/String;", false);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "d", "(Ljava/lang/String;Ljava/lang/String;)V", false);
                    mv.visitLdcInsn(name);
                    mv.visitLdcInsn("========end=========");
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "d", "(Ljava/lang/String;Ljava/lang/String;)V", false);
                }
            }
        };
        return mv;
    }
}
