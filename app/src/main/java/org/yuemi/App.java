package org.yuemi;

import org.bukkit.plugin.java.JavaPlugin;

public class App extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("Yuemi Git Plugin enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Yuemi Git Plugin disabled.");
    }
}
