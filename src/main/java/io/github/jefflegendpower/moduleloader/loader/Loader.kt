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
 * @param classLoader The plugin's class loader (the classloader can be found from plugin.getClassLoader())
 */
class Loader(private val logger: Logger, private val classLoader: ClassLoader) {

    private val loadedModules = mutableListOf<String>()
    private fun modulesFolder(): File = ModuleLoader.getInstance().modulesFolder

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
        if (!modulesFolder().exists()) {
            modulesFolder().mkdir()
        }
    }

    /**
     * Adds a module to the module folder
     * overwrites if the module already exists
     */
    fun newModule(file: File) {
        if (file.parentFile != modulesFolder()) {
            logger.info("Moving module ${file.name} to the ${modulesFolder().name} directory")
            val module = File(modulesFolder(), file.name)
            if (module.exists()) logger.info("Module ${file.name} already exists in the ${modulesFolder().name} directory, overwriting...")
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
        val modules = modulesFolder().listFiles() ?: return null
        for (module in modules) {
            if (module.isFile && module.name == name) {
                return module
            }
        }

        return null
    }
}


