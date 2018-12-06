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
                def processResourceTask = project.task.findByName("process${variant.name.capitalize()}Resources")
                def tinyPlugin = "tinyPlugin${variant.name.capitalize()}"
                project.task(tinyPlugin) << {
                    def apiKey = tinyInfo.apiKey
                    Tinify.setKey(apiKey)
                    Tinify.validate()

                    String resPath = "${project.projectDir}/src/main/res/"
                    def dir = new File(resPath)
                    dir.eachDirMatch(~/mipmap[a-z-]*/) {
                        def mipmapDir = new File(it)
                        mipmapDir.eachFile { file->
                            def fis = new FileInputStream(file)
                            println("beforeSize:${fis.available()}")
                            Tinify.fromFile(file).toFile(file)
                            println("afterSize:${fis.available()}")
                        }
                    }
                }
                project.tasks.findByName(tinyPlugin).dependsOn processResourceTask.taskDependencies.getDependencies(processResourceTask)
                processResourceTask.dependsOn project.tasks.findByName(tinyPlugin)
            }
        }
    }
}
