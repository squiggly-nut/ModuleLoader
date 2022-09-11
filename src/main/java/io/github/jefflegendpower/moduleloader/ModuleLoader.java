package io.github.jefflegendpower.moduleloader;

import org.bukkit.plugin.java.JavaPlugin;

public final class ModuleLoader extends JavaPlugin {

    private static ModuleLoader instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        if (!getDataFolder().exists())
            getDataFolder().mkdir();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
