package io.github.jefflegendpower.moduleloader.loader

import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels
import java.util.logging.Logger


class Downloader(private val logger: Logger, private val moduleFolder: File) {

    init {
        if (!moduleFolder.exists()) {
            moduleFolder.mkdir()
        }
    }

    /**
     * Downloads a file from a URL and puts it in the module folder
     * @param url The URL to download from
     * @sample download("example.com/example.jar")
     */
    fun downloadModule(url: String) {
        logger.info("Downloading module")
        val moduleFile = File(moduleFolder, url.substringAfterLast("/"))
        if (moduleFile.exists()) {
            logger.warning("Module already exists")
            return
        }

        val website = URL(url)
        val rbc = Channels.newChannel(website.openStream())
        val fos = FileOutputStream(moduleFile)
        fos.channel.transferFrom(rbc, 0, Long.MAX_VALUE)
        logger.info("Downloaded module")
    }
}