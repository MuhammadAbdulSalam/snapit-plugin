# SnapIt Gradle Plugin
![Maven Central](https://img.shields.io/badge/Gradle_Plugin-1.0.5-blue)

[SnapIt Gradle Portal Link](https://plugins.gradle.org/plugin/uk.adbsalam.snapit)

The Snapit Grade plugin enables the seamless integration of the Snapit library (available at [SnapIt Maven Sonatype]( https://central.sonatype.com/namespace/uk.adbsalam.snapit)). For Snapit repository and snapit-plugin samples please visit our GitHub at [SnapIt Github Repo]( https://github.com/MuhammadAbdulSalam/snapit ).


## Features

Snapit is a powerful tool designed to automate the generation of Paparazzi tests by simple adding ```@Snapit``` Annotation on ```@Preview``` Functions, significantly reducing the time and resources required for creating snapshot tests. It leverages the ```@Preview``` Composables feature from the codebase, making it easy to implement and utilize. With Snapit, you can streamline your testing process and ensure the quality and reliability of your code.

## Implementation

Apply [Snapit-Gradle Plugin](https://plugins.gradle.org/plugin/uk.adbsalam.snapit) plugin to your module. [GithubRepo](https://github.com/MuhammadAbdulSalam/snapit-plugin).

In your ```build.gradle.kts``` apply plugin

Kotlin DSL

```kotlin
plugins {
  id ("app.cash.paparazzi") version "<LATEST-VERSION>"
  id ("uk.adbsalam.snapit") version "<LATEST-VERSION>"
}
```

OR Using plugin application:

```
buildscript {
  repositories {
    maven {
      url = uri("https://plugins.gradle.org/m2/")
    }
  }
  dependencies {
    classpath("uk.adbsalam.snapit:snapit-plugin:<LATEST_VERSION>")
    classpath ("app.cash.paparazzi:paparazzi-gradle-plugin:<LATEST_VERSION>")
  }
}

apply(plugin = "uk.adbsalam.snapit")
apply(plugin = "app.cash.paparazzi")

```


In your ```build.gradle.kts``` set snap test package location to generate tests at.
```kotlin
snapIt {
  testDir = "src/kotlin/testuk/adbsalam/snapit"
  flavor = "testFlavour" 
}
```

``testDir`` Full directory of current unit test location
```flavor``` Name of current flavour, if your app do not have multiple flavours then set value to "debug". Release flavours are not allowed in SnapIt


This will allow usage of ```@SnapIt``` in code and create following gradle tasks

Now In Compose files you can make use of annotation ```@SnapIt``` as following

- ```./gradlew :module:SnapItGenerate``` to generate test files
- ```./gradlew :module:SnapItRecord``` to record Paparazzi Snapshot tests
- ```./gradlew :module:SnapItVerify``` to run/verify generated tests




## Usage/Examples

In ```ExampleScreen.kt``` I have following code

```kotlin
@Composable
fun ExampleCompose() {
    Text(text = "Hello World")
}

@Composable
@Preview
@SnapIt
fun ExamplePreview() {
    ExampleCompose()
}

@Composable
@Preview
@SnapIt(
    name = "when in preview, should render correctly",
    preview = true,
    isScreen = true
)
fun ExamplePreview2() {
    ExampleCompose()
}
```
Run Task to genenrate test file

```./gradlew :module:snapitgenerate```

This will generate 2 Files.
- ```ExampleScreenComponentTest.kt``` to generate test for a component with no
  background of device system UI.
```kotlin
 import app.cash.paparazzi.Paparazzi
 import org.junit.Rule
 import org.junit.Test
 import org.junit.runner.RunWith
 import org.junit.runners.JUnit4
 import uk.adbsalam.snapit.testing.captureScreenshot
 import uk.adbsalam.snapit.testing.forComponent

 @RunWith(JUnit4::class)
 class ExampleScreenComponentTest {
   @get:Rule
   val paparazzi: Paparazzi = Paparazzi.forComponent()

   @Test
   fun examplePreviewSnapTest() {
     paparazzi.captureScreenshot {
         ExamplePreview()
     }
   }
 }
```

- ```ExampleScreenScreentTest.kt``` to generate test for a Screen with system UI enabled.
```kotlin 
 import androidx.compose.runtime.CompositionLocalProvider
 import androidx.compose.ui.platform.LocalInspectionMode
 import app.cash.paparazzi.Paparazzi
 import org.junit.Rule
 import org.junit.Test
 import org.junit.runner.RunWith
 import org.junit.runners.JUnit4
 import uk.adbsalam.snapit.testing.captureScreenshot
 import uk.adbsalam.snapit.testing.forScreen

 @RunWith(JUnit4::class)
 class ExampleScreenScreenTest {
   @get:Rule
   val paparazzi: Paparazzi = Paparazzi.forScreen()

   @Test
   fun `when in preview, should render correctly`() {
     paparazzi.captureScreenshot {
         CompositionLocalProvider(LocalInspectionMode provides true) {
             ExamplePreview2()
         }
     }
   }
 }
```

## Record Tests

Run task to generate snapshots
```
./gradlew :module:snapitrecord
```

This will generate following SnapShots for this example code

### Component
![Component](https://github.com/MuhammadAbdulSalam/snapit/blob/develop/sample/src/test/snapshots/images/uk.adbsalam.snapit.sample_ExampleScreenComponentTest_examplePreviewSnapTest.png)


### Screen
![Screen](https://github.com/MuhammadAbdulSalam/snapit/blob/develop/sample/src/test/snapshots/images/uk.adbsalam.snapit.sample_ExampleScreenScreenTest_when%20in%20preview%2C%20should%20render%20correctly.png)


## Verification
Run task to verify snapshot tests
```
./gradlew :module:snapitverify
```


## Acknowledgements

- Special thanks to [Ben Ezard](https://github.com/Ezard) for help and mentoring.

