package com.binzi.plugin

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.AppExtension
import com.binzi.plugin.asm.CostTimeTransform
import com.binzi.plugin.aspectj.AspectJWork
import com.binzi.plugin.javassist.JavassistInject
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile

class AopPlugin implements Plugin<Project> {

    def aspectjOn = true
    def patchOn = false
    def asmOn = false

    @Override
    void apply(Project project) {
        def hasApp = project.plugins.withType(AppPlugin)
        if (!hasApp) {
            throw new IllegalStateException("'android' plugin required.")
        }
        if (asmOn) {
            def android = project.extensions.getByType(AppExtension)
            android.registerTransform(new CostTimeTransform())
        }
        project.dependencies {
            implementation 'org.aspectj:aspectjrt:1.8.9'
        }
        println "aspectjOn:" + aspectjOn

        project.afterEvaluate {
            project.android.applicationVariants.all { variant ->
                if (aspectjOn) {
                    JavaCompile javaCompile = variant.javaCompile
                    AspectJWork aspectJWork = new AspectJWork(javaCompile)
                    aspectJWork.doWork()
                }
            }


            project.android.applicationVariants.each { variant ->
                println("variant:" + variant.name)
                println("variant:" + variant.getDirName())
                if (patchOn) {
                    def prepareTaskName = "check${variant.name.capitalize()}Manifest"
                    def prepareTask = project.tasks.findByName(prepareTaskName)
                    if (prepareTask) {
                        prepareTask.doFirst({
                            prepareBuild(project, variant)
                        })
                    } else {
                        println("not found task ${prepareTaskName}")
                    }

                    def dexTaskName = "transformClassesWithDexBuilderFor${variant.name.capitalize()}"
                    def dexTask = project.tasks.findByName(dexTaskName)
                    if (dexTask) {
                        def aopProcessBeforeDex = "aopProcessBeforeDex${variant.name.capitalize()}"
                        project.task(aopProcessBeforeDex) << {
                            aopProcess(project, variant, dexTask)
                        }
                        //insert task
                        def patchProcessBeforeDexTask = project.project.tasks.findByName(aopProcessBeforeDex)
                        patchProcessBeforeDexTask.dependsOn dexTask.taskDependencies.getDependencies(dexTask)
                        dexTask.dependsOn patchProcessBeforeDexTask
                    } else {
                        println("not found task:${dexTaskName}")
                    }
                }

            }
        }


    }

    void prepareBuild(def project, def variant) {
        //proguard map
        println("prepareBuild")
        def projectDir = project.projectDir.absolutePath
        projectDir = projectDir.subSequence(0, projectDir.lastIndexOf('/'))
    }

    void aopProcess(def project, def variant, def dexTask) {
        println("aopBeforeDex")
        JavassistInject.appendClassPath(AOPConfig.androidJar)
        Set<File> inputFiles = dexTask.inputs.files.files
        inputFiles.each { inputFile ->
            def path = inputFile.absolutePath
            JavassistInject.appendClassPath(path)
        }

        inputFiles.each { inputFile ->
            def path = inputFile.absolutePath
            if (path.endsWith(".class")) {
                JavassistInject.injectDir(inputFile.absolutePath)
            } else if (path.endsWith(".jar")) {
                println inputFile.absolutePath
            }

        }
    }
}
