package com.binzi.plugin.aspectj

import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main
import org.gradle.api.tasks.compile.JavaCompile

public class AspectJWork {
    JavaCompile javaCompile

    public AspectJWork(JavaCompile javaCompile) {
        this.javaCompile = javaCompile;
    }
    //http://www.eclipse.org/aspectj/doc/released/devguide/ajc-ref.html
    //
    // -sourceRoots:
    //  Find and build all .java or .aj source files under any directory listed in DirPaths. DirPaths, like classpath, is a single argument containing a list of paths to directories, delimited by the platform- specific classpath delimiter. Required by -incremental.
    // -inPath:
    //  Accept as source bytecode any .class files in the .jar files or directories on Path. The output will include these classes, possibly as woven with any applicable aspects. Path is a single argument containing a list of paths to zip files or directories, delimited by the platform-specific path delimiter.
    // -classpath:
    //  Specify where to find user class files. Path is a single argument containing a list of paths to zip files or directories, delimited by the platform-specific path delimiter.
    // -aspectPath:
    //  Weave binary aspects from jar files and directories on path into all sources. The aspects should have been output by the same version of the compiler. When running the output classes, the run classpath should contain all aspectPath entries. Path, like classpath, is a single argument containing a list of paths to jar files, delimited by the platform- specific classpath delimiter.
    // -bootclasspath:
    //  Override location of VM's bootclasspath for purposes of evaluating types when compiling. Path is a single argument containing a list of paths to zip files or directories, delimited by the platform-specific path delimiter.
    // -d:
    //  Specify where to place generated .class files. If not specified, Directory defaults to the current working dir.
    // -preserveAllLocals:
    //  Preserve all local variables during code generation (to facilitate debugging).
    public void doWork() {

        javaCompile.doLast {
            println "aspect do work.........."
            final def log = project.logger
            String[] args = [
                    "-showWeaveInfo",
                    "-1.5",
                    "-inpath", javaCompile.destinationDir.toString(),
                    "-aspectpath", javaCompile.classpath.asPath,
                    "-d", javaCompile.destinationDir.toString(),
                    "-classpath", javaCompile.classpath.asPath,
                    "-bootclasspath", project.android.bootClasspath.join(File.pathSeparator)
            ]
//            println "ajc args: " + Arrays.toString(args)

            MessageHandler handler = new MessageHandler(true)
            new Main().run(args, handler)
            for (IMessage message : handler.getMessages(null, true)) {
                switch (message.getKind()) {
                    case IMessage.ABORT:
                    case IMessage.ERROR:
                    case IMessage.FAIL:
                        log.error message.message, message.thrown
                        break
                    case IMessage.WARNING:
                        log.warn message.message, message.thrown
                        break
                    case IMessage.INFO:
                        log.info message.message, message.thrown
                        break
                    case IMessage.DEBUG:
                        log.debug message.message, message.thrown
                        break
                }
            }
        }
    }

}