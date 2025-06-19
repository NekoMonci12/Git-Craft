package org.yuemi.commands;

import org.bukkit.plugin.java.JavaPlugin;

public class CommandRegistrar {

    private final JavaPlugin plugin;

    public CommandRegistrar(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void registerAll() {
        plugin.getCommand("git").setExecutor(new GitRootCommand(plugin));
    }
}
