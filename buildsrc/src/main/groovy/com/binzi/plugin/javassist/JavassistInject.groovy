package com.binzi.plugin.javassist

import com.binzi.plugin.AOPConfig
import javassist.ClassPool
import javassist.CtClass
import javassist.CtField
import javassist.CtMethod
import javassist.Modifier

public class JavassistInject {

    private static ClassPool pool = ClassPool.getDefault()
    /**
     * 添加classPath到ClassPool
     * @param libPath
     */
    public static void appendClassPath(String libPath) {
        pool.appendClassPath(libPath)
        pool.appendClassPath("/Users/huangyoubin/AndroidStudioProjects/AOP/app/build/intermediates/javac/debug/compileDebugJavaWithJavac/classes")
    }

    /**
     * 判断某个类是否需要注入代码
     * @param className 该类的绝对路径  如：d:\aitsuki\project\demo.class
     * @return
     */
    public static boolean needInject(String className) {
        def flag = true
        for (String noInjectClass : AOPConfig.noInjectClasses) {
            if (className.endsWith(noInjectClass)) {
                flag = false
                break
            }
        }

        if (flag) {
            for (String noInjectClass : AOPConfig.noInjectKeyword) {
                if (className.contains(noInjectClass)) {
                    flag = false
                    break
                }
            }
        }
        return flag
    }

    /**
     * 遍历该目录下的所有class，对所有class进行代码注入。
     * 其中以下class是不需要注入代码的：
     * --- 1. R文件相关
     * --- 2. 配置文件相关（BuildConfig）
     * @param path 目录的路径
     */
    public static void injectDir(String path) {
        if (path.endsWith(".class") && needInject(path)) {
            // 判断当前目录是否是在我们的应用包里面
            String packageName = AOPConfig.appPackageName.replace(".", "/")
            int index = path.indexOf(packageName)
            boolean isMyPackage = index != -1
            if (isMyPackage) {

                int end = path.length() - 6 // .class = 6
                String className = path.substring(index, end).replace('\\', '.').replace('/', '.')

                //开始修改class文件
                CtClass ctc = pool.getCtClass(className)

                if (ctc.isFrozen()) {
                    ctc.defrost()
                }
                pool.importPackage("com.binzi.aop.patch")
                //给类添加$patch变量，即补丁变量
                CtField patch = new CtField(pool.get("com.binzi.aop.patch.Patch"), "\$patch", ctc)
                patch.setModifiers(Modifier.STATIC)
                ctc.addField(patch)

                //遍历类的所有方法
                CtMethod[] methods = ctc.getDeclaredMethods()
                for (CtMethod method : methods) {
                    if (!method.empty) {

                        //在每个方法之前插入判断语句，判断类的补丁实例是否存在
                        StringBuilder injectStr = new StringBuilder();
                        injectStr.append("if(\$patch!=null){\n")
                        String javaThis = "null,"
                        if (!Modifier.isStatic(method.getModifiers())) {
                            javaThis = "this,"
                        }
                        String runStr = "\$patch.dispatchMethod(" + javaThis + "\""+method.getName() + "." + method.getSignature() + "\" ,\$args)"
                        injectStr.append(addReturnStr(method, runStr))
                        injectStr.append("}")

                        println("插入：" + injectStr.toString() + "语句")
                        method.insertBefore(injectStr.toString())
                    }

                }
                ctc.writeFile("/Users/huangyoubin/AndroidStudioProjects/AOP/app/build/intermediates/javac/debug/compileDebugJavaWithJavac/classes")
                ctc.detach()
            }
        }
    }

    //解析方法签名，获取方法放回值类型
    public static String getReturnType(String methodSign) {
        String type = ""
        int index = methodSign.indexOf(")L");
        String jType = methodSign.substring(index + 2, methodSign.length() - 1)
        type = jType.replace("/", ".")
        return type
    }
    //给非void方法加入return语句，并处理基本类型返回值
    public static String addReturnStr(CtMethod method, String runStr) {
        String returnStr = ""
        String typeStr = ""
        switch (method.getReturnType()) {
            case CtClass.voidType:
                return runStr + ";\n return;"
                break
            case CtClass.booleanType:
                returnStr = "return ((Boolean)"
                typeStr = ".booleanValue()"
                break
            case CtClass.byteType:
                returnStr = "return ((byte)"
                typeStr = ".byteValue()"
                break
            case CtClass.charType:
                returnStr = "return ((char)"
                typeStr = ".charValue()"
                break
            case CtClass.doubleType:
                returnStr = "return ((Number)"
                typeStr = ".doubleValue()"
                break
            case CtClass.floatType:
                returnStr = "return ((Number)"
                typeStr = ".floatValue()"
                break
            case CtClass.intType:
                returnStr = "return ((Number)"
                typeStr = ".intValue()"
                break
            case CtClass.longType:
                returnStr = "return ((Number)"
                typeStr = ".longValue()"
                break
            case CtClass.shortType:
                returnStr = "return ((Number)"
                typeStr = ".shortValue()"
                break
            default:
                returnStr = "return((" + getReturnType(method.getSignature()) + ")"
                break
        }
        return returnStr + "(" + runStr + "))" + typeStr + ";\n"
    }

}