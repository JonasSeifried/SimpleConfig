# Simple Config

Simple Config is a Kotlin Fabric Minecraft mod that allows you to easily manage your mod's configuration. It provides a simple and intuitive API to read and write configuration data. The data will be serialized with the `JSON`format.

## Usage

Here's a basic example of how to use Simple Config:
``` kotlin
object TutorialMod : ModInitializer {
    private val logger = LoggerFactory.getLogger("tutorialmod")

	override fun onInitialize() {

    //Access Data
    MyConfig.data.someNestedObject.someData
    MyConfig.data.someString // returns "hello"

    //write Data
    MyConfig.patch {
      someString = "Hello World!"
      someNumber = 0.07
    }

    MyConfig.data.someString // returns "Hello World!"

    logger.info("Hello I am using Simple Config!")
  }
}
```
When you write data using the `patch` method, it will automatically be written to the file.

## Configuration

To create a config, you need to create an `object` that extends `SimpleConfig<T>`. `T` can be any class as long as it has the `@Serializable` annotation. You must override `fileName`!

Here's an example:
``` kotlin
object MyConfig: SimpleConfig<SomeDataClass>(defaultValue = SomeDataClass()) {
	override var fileName = "my_config"
	// Optional: `.json` is the default file extension
	override val fileEnding = ".json"
	// Optional: `FabricLoader.getInstance().configDir` is the default path
	override val path: Path = FabricLoader.getInstance().configDir
}

@Serializable
data class SomeDataClass(
	var someNumber: Double = 2.0,
	var someString: String = "hello",
	var someNestedObject: SomeNestedClass = SomeNestedClass()
)

@Serializable
class SomeNestedClass {
	var someData = LocalDate.parse("2024-01-01")
}
```

## Gradle Setup  
To set up Simple Config via Gradle, add the following to your `build.gradle` file:
``` gradle

plugins {
  [...]
  id 'org.jetbrains.kotlin.plugin.serialization' version '1.9.22'
}

repositories {
  [...]
  maven { url 'https://jitpack.io' }
}

dependencies {
  [...]
  modImplementation 'com.github.JonasSeifried:SimpleConfig:c12ca16a20'
}
```

## Troubleshooting
If you encounter any issues while using Simple Config, please check the [GitHub issues](https://github.com/JonasSeifried/SimpleConfig/issues) page. If your issue is not listed there, feel free to open a new issue.

## License
Simple Config is licensed under the [MIT License](https://github.com/JonasSeifried/SimpleConfig/blob/main/LICENSE).
