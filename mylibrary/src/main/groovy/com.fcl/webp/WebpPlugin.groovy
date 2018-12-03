package com.fcl.webp

import com.android.build.gradle.AppPlugin
import com.fcl.time.TimeListener
import org.gradle.api.Plugin
import org.gradle.api.Project

public class WebpPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println("-------------apply webp plugin start--------------")
        def hasApp = project.plugins.withType(AppPlugin)
        def variants = hasApp ? project.android.applicationVariants : project.android.libraryVariants
        def config = project.extensions.create("webpinfo", WebpInfo)
        project.afterEvaluate {
            variants.all { variant->
                def flavor = variant.getVariantData().getVariantConfiguration().getFlavorName()
                def buildType = variant.getVariantData().getVariantConfiguration().getBuildType().name
                println("flavor:${flavor},buildType:${buildType}")
                if (config.skipDebug == true && "${buildType}".contains("debug")) {
                    println("skipdebug webpConvertPlugin Task!!!")
                    return
                }
                def dx = project.tasks.findByName("process${variant.name.capitalize()}Resources")
                def webpConvertPlugin = "webpConvertPlugin${variant.name.capitalize()}"
                project.task(webpConvertPlugin) << {
                    println("task run")
                    def resPath = "${project.buildDir}/intermediates/res/${buildType}"
                    println("respath:${resPath}")
                    def dir = new File("${resPath}")
                    dir.eachDirMatch(~/drawable[a-z0-9-]*/) {drawDir->
                        println("drawableDir:${drawDir}")
                        def file = new File("${drawDir}")
                        def whiteListFile = new File("${project.projectDir}/webp_white_list.txt")
                        def whiteList = ArrayList.newInstance()
                        if (whiteListFile.exists()) {
                            whiteListFile.eachLine {name->
                                whiteList.add(name)
                            }
                        }
                        file.eachFile {
                            def name = it.name
                            if (whiteList.contains(name)) {
                                return
                            }
                            if ((name.endsWith(".jpg") || name.endsWith(".png")) && !name.contains(".9")) {
                                def picName = name.split('\\.')[0]
                                def suffix = name.split('\\.')[1]
                                println("picname:${picName}, name:${name}")

                            }
                        }
                    }
                }

                project.tasks.findByName(webpConvertPlugin).dependsOn dx.taskDependencies.getDependencies(dx)
                dx.dependsOn project.tasks.findByName(webpConvertPlugin)
            }
        }
        println("-------------apply webp plugin end--------------")
    }
}

class WebpInfo {
    boolean skipDebug
}
