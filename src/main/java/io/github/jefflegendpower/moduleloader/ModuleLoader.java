package io.github.jefflegendpower.moduleloader;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class ModuleLoader extends JavaPlugin {

    private static ModuleLoader instance;

    private final File modulesFolder = new File(getDataFolder(), "modules");

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        if (!getDataFolder().exists())
            getDataFolder().mkdir();
        if (!modulesFolder.exists())
            modulesFolder.mkdir();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static ModuleLoader getInstance() {
        return instance;
    }

    public File getModulesFolder() {
        return modulesFolder;
    }
}
