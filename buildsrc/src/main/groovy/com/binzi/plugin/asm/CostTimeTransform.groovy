package com.binzi.plugin.asm

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.io.FileUtils
import org.apache.commons.codec.digest.DigestUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

/**
 * @Author: huangyoubin
 * @Create: 2019/3/31 9:53 PM
 * @Description:
 */
class CostTimeTransform extends Transform {
    @Override
    String getName() {
        return "cost_time"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }


    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
        println '//===============asm visit start===============//'

        transformInvocation.inputs.each {
            TransformInput input ->
                input.directoryInputs.each {
                    DirectoryInput directoryInput ->
                        if (directoryInput.file.isDirectory()) {
                            directoryInput.file.eachFileRecurse { File file ->
                                def name = file.name
                                if (name.endsWith(".class") && !name.startsWith("R\$") &&
                                        !"R.class".equals(name) && !"BuildConfig.class".equals(name)) {

                                    println name + ' is changing...'

                                    ClassReader cr = new ClassReader(file.bytes)
                                    ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS)
                                    ClassVisitor cv = new CostClassVisitor(cw)

                                    cr.accept(cv, ClassReader.EXPAND_FRAMES)

                                    byte[] code = cw.toByteArray()

                                    FileOutputStream fos = new FileOutputStream(
                                            file.parentFile.absolutePath + File.separator + name)
                                    fos.write(code)
                                    fos.close()
                                }
                            }
                        }

                        def dest = transformInvocation.outputProvider.getContentLocation(directoryInput.name,
                                directoryInput.contentTypes, directoryInput.scopes,
                                Format.DIRECTORY)


                        FileUtils.copyDirectory(directoryInput.file, dest)
                }

                input.jarInputs.each { JarInput jarInput ->
                    def jarName = jarInput.name
                    def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
                    if (jarName.endsWith(".jar")) {
                        jarName = jarName.substring(0, jarName.length() - 4)
                    }

                    def dest = transformInvocation.outputProvider.getContentLocation(jarName + md5Name,
                            jarInput.contentTypes, jarInput.scopes, Format.JAR)

                    FileUtils.copyFile(jarInput.file, dest)
                }
        }

    }
}
