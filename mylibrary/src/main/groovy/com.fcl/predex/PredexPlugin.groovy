package com.fcl.predex

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

public class PredexPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        println("apply predex")
        def android = project.extensions.getByType(AppExtension)
        android.registerTransform(new PredexTransform(project))
    }
}
