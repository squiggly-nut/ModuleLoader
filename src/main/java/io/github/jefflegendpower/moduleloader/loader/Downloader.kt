package io.github.jefflegendpower.moduleloader.loader

import io.github.jefflegendpower.moduleloader.ModuleLoader
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels
import java.util.logging.Logger


class Downloader(private val logger: Logger) {

    private fun modulesFolder(): File = ModuleLoader.getInstance().modulesFolder

    init {
        if (!modulesFolder().exists()) {
            modulesFolder().mkdir()
        }
    }

    /**
     * Downloads a file from a URL and puts it in the module folder
     * Overwrites the file if it already exists
     * @param url The URL to download from
     * @sample download("example.com/example.jar")
     */
    fun downloadModule(url: String) {
        logger.info("Downloading module")
        val moduleFile = File(modulesFolder(), url.substringAfterLast("/"))
        if (moduleFile.exists()) {
            logger.warning("Module already exists, overwriting...")
            moduleFile.delete()
            moduleFile.createNewFile()
        }

        val website = URL(url)
        val rbc = Channels.newChannel(website.openStream())
        val fos = FileOutputStream(moduleFile)
        fos.channel.transferFrom(rbc, 0, Long.MAX_VALUE)
        logger.info("Downloaded module")
    }
}