package org.yuemi;

import org.bukkit.plugin.java.JavaPlugin;
import org.yuemi.commands.CommandRegistrar;
import org.bstats.bukkit.Metrics;

public class App extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("Git Craft Plugin enabled.");
        new CommandRegistrar(this).registerAll();
        setupMetrics();
    }

    @Override
    public void onDisable() {
        getLogger().info("Git Craft Plugin disabled.");
    }

    private void setupMetrics() {
        int pluginId = 26264;
        Metrics metrics = new Metrics(this, pluginId);
    }
}
