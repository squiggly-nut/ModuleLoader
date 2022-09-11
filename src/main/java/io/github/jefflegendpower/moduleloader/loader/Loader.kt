package io.github.jefflegendpower.moduleloader.loader

import com.google.common.base.Supplier
import com.google.common.base.Suppliers
import io.github.jefflegendpower.moduleloader.ModuleLoader
import java.io.File
import java.lang.reflect.Method
import java.net.URL
import java.net.URLClassLoader
import java.util.logging.Logger

/**
 * @author JeffLegendPower
 * @param logger The logger to use (preferably the plugin's logger)
 * @paramm moduleFolder
 */
class Loader(private val logger: Logger, private val classLoader: ClassLoader) {

    private val loadedModules = mutableListOf<String>()
    private val moduleFolder: File = File(ModuleLoader.getPlugin(ModuleLoader::class.java).dataFolder, "modules")

    private val ADD_URL_METHOD: Supplier<Method> = Suppliers.memoize {
        try {
            val addUrlMethod: Method = URLClassLoader::class.java.getDeclaredMethod("addURL", URL::class.java)
            addUrlMethod.isAccessible = true
            return@memoize addUrlMethod
        } catch (e: NoSuchMethodException) {
            throw RuntimeException(e)
        }
    }

    init {
        if (!moduleFolder.exists()) {
            moduleFolder.mkdir()
        }
    }

    /**
     * Adds a module to the module folder
     * overwrites if the module already exists
     */
    fun newModule(file: File) {
        if (file.parentFile != moduleFolder) {
            logger.info("Moving module ${file.name} to the ${moduleFolder.name} directory")
            val module = File(moduleFolder, file.name)
            if (module.exists()) logger.info("Module ${file.name} already exists in the ${moduleFolder.name} directory, overwriting...")
            file.copyTo(module, overwrite = true)
        }
    }

    /**
     * Loads a module from the module folder
     */
    fun loadModule(name: String) =
        loadModule(getModule(name)
            ?: throw IllegalArgumentException("Module $name does not exist"))

    private fun loadModule(file: File) {
        logger.info("Loading Module: " + file.name)

        if (file.isDirectory) {
            logger.warning("${file.name} is not a file")
            return
        } else if (!file.exists()) {
            logger.warning("${file.name} does not exist")
            return
        }

        if (loadedModules.contains(file.name)) {
            logger.warning("Module ${file.name} is already loaded")
            return
        }

        ADD_URL_METHOD.get().invoke(classLoader, URL("jar:file:${file.path}!/"))
        loadedModules.add(file.name)
        logger.info("Loaded Module: " + file.name)
    }

    private fun getModule(name: String): File? {
        val modules = moduleFolder.listFiles() ?: return null
        for (module in modules) {
            if (module.isFile && module.name == name) {
                return module
            }
        }

        return null
    }
}


