# Moon FE

This is the UI code of moon project.

## Structure
```
moon
├── data
│  ├── model -> Model class, usually data class to hold data
│  │  └── Song.kt
│  └── repository -> class to add, find, remove model. Take a look at repository pattern
├── di -> custom di code with hilt
├── MoonApplication.kt -> DI entry point
└── ui -> ui code, each component have a directory with UI and ViewModel code, e.g player
├── player
│  ├── Player.kt
│  └── PlayerViewModel.kt
├── screens -> if we have multiple screen
│  └── activity -> main activity but i'm lazy
└── theme 
    ├── Color.kt
    ├── Theme.kt
    └── Type.kt
```
## Build

Just open it with android studio, you may have some error about jvm version. Install JVM 17 in your
android studio and change config in build.gradle
```kotlin
compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
}
kotlinOptions {
    jvmTarget = "17"
}
```
