package com.fcl.time

import org.gradle.api.Plugin
import org.gradle.api.Project

public class TimePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.gradle.addListener(new TimeListener())
    }
}
