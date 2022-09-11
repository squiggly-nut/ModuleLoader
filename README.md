# ModuleLoader
Load jars into the classpath as an alternative to shading and creating massive jars

# Dependencies
https://www.spigotmc.org/resources/kotlin-stdlib.80808/

# Usage
* In the plugin.yml, add ModuleLoader to 'depend'

### Loading modules
* To load a jar into the classpath, you must first add it to the modules folder
* This can either be done by either manually adding the jar to the folder, or adding it via code
![Code example of loading a module](https://user-images.githubusercontent.com/80235181/189542342-a77e6d6f-ddf3-40f7-877b-72237a58f314.png)
* The module is now loaded and you don't need to shade it into your project!

### Downloading modules
* To download a jar from the internet, you only need to put the url where the file is located
![Code example of downloading a module](https://user-images.githubusercontent.com/80235181/189542578-c2f6f313-7a65-4450-9c69-24abd24dd466.png)
* Note: it will overwrite the old file
