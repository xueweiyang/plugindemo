package com.fcl.tiny

import com.android.build.gradle.AppPlugin
import com.tinify.Tinify
import org.gradle.api.Plugin
import org.gradle.api.Project

public class TinyPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def hasApp = project.plugins.withType(AppPlugin)
        def variants = hasApp ? project.android.applicationVariants : project.android.libraryVariants
        def tinyInfo = project.extensions.create("tinyinfo", TinyInfo)

        project.afterEvaluate {
            variants.all { variant ->
                def processResourceTask = project.tasks.findByName("process${variant.name.capitalize()}Resources")
                def tinyPlugin = "tinyPlugin${variant.name.capitalize()}"
                project.task(tinyPlugin) << {
                    //                    def apiKey = tinyInfo.apiKey
                    def tinyFilePath = project.rootDir.path+"/tiny.txt"
                    println("tinyfilepath:${tinyFilePath}")
                    def tinyFile = new File(tinyFilePath)
                    def apiKey = "PDII5xGelp6UwOeFZEoIevDdS8lSfGKF"
                    Tinify.setKey(apiKey)
                    Tinify.validate()

                    String resPath = "${project.projectDir}/src/main/res/"
                    def dir = new File(resPath)
//                    dir.eachDirMatch(~/mipmap-xhdpi/) {
//
//                        it.eachFile { file ->
//                            println("tiny file:${file}")
//                            def fis = new FileInputStream(file)
//                            println("beforeSize:${fis.available()}")
//                            def source = Tinify.fromFile(file.path)
//                            println("get source")
//                            source.toFile(file.path)
//                            println("afterSize:${fis.available()}")
//                        }
//                    }
                }
                project.tasks.findByName(tinyPlugin).dependsOn processResourceTask.taskDependencies.getDependencies(
                    processResourceTask)
                processResourceTask.dependsOn project.tasks.findByName(tinyPlugin)
            }
        }
    }
}
