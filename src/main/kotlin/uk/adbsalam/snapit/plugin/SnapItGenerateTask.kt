package uk.adbsalam.snapit.plugin

import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.configurationcache.extensions.capitalized
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.register
import java.lang.Exception

/**
 * Generate Gradle tasks needed for Code generation and running paparazzi tests
 */
internal fun Project.snapItGenerateTask() {

    /**
     * Generate task will complete following steps
     * - Copy generated test files from snapit build folder
     * - Add these test files to test directory
     * - Once done this task will then replace comment regex and make test executable
     */
    this.tasks.register<Copy>("snapItGenerate") {

        try {
            val testDirExt = this.project.extensions[PATH_EXTENSION] as SnapPath
            val flavourExt = this.project.extensions[DEBUG_FLAVOUR] as SnapFlavour

            if (testDirExt.testDir.get().isEmpty()) {
                throw snapItExtentionException
            }

            if (flavourExt.flavour.get().isEmpty()) {
                throw snapItExtentionException
            }

            val flavour = getFlavour(flavourExt.flavour.get())
            dependsOn(getAssembleTask(flavour))

            from("build/generated/ksp/${flavour}/kotlin/uk/adbsalam/snapit/")
            into(testDirExt.testDir.get())
            filter { line -> line.replace("//", "") }
        } catch (e: Exception) {
            snapItExtentionException.printStackTrace()
        }
    }

    /**
     * task to record paparazzi tests
     */
    this.tasks.register("snapItRecord") {
        dependsOn("recordPaparazzi")
    }

    /**
     * task to verify paparazzi tests
     */
    this.tasks.register("snapItVerify") {
        dependsOn("verifyPaparazzi")
    }
}

fun getFlavour(flavour: String): String {
    return if (flavour == "debug" || flavour == "Debug") "debug"
    else "${flavour}Debug"
}

fun Project.getAssembleTask(flavour: String): String {
    return ":${project.name}:assemble${flavour.capitalized()}"
}