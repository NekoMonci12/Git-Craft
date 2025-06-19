package org.yuemi;

import org.bukkit.plugin.java.JavaPlugin;
import org.yuemi.commands.CommandRegistrar;

public class App extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("Yuemi Git Plugin enabled.");
        new CommandRegistrar(this).registerAll();
    }

    @Override
    public void onDisable() {
        getLogger().info("Yuemi Git Plugin disabled.");
    }
}
