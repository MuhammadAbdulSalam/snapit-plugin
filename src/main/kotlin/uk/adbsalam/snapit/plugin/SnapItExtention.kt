package uk.adbsalam.snapit.plugin

import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.the

/**
 * test source path
 */
const val srcTestPath = "src/test/java/"

/**
 * extension name to be created
 */
const val PATH_EXTENSION = "testDirPath"

/**
 * flavour name of current project
 */
const val DEBUG_FLAVOR = "debugFlavor"

/**
 * interface for SnapPath extension
 */
interface SnapPath {
    val testDir: Property<String>
}

/**
 * interface for SnapPath extension
 */
interface SnapFlavour {
    val flavor: Property<String>
}

/**
 * function to allow apply property value
 */
fun Project.snapIt(func: SnapPathTask.() -> Unit) = SnapPathTask().apply {
    func()
    onInit()
}


/**
 * SnapPathTask to generate and execute gradle extension value
 */
class SnapPathTask {
    var testDir = ""
    var flavor = ""

    fun Project.onInit() {
        the<SnapPath>().testDir.set(testDir)
        the<SnapFlavour>().flavor.set(flavor)
    }
}