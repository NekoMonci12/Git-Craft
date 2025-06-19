package org.yuemi.commands;

import org.bukkit.plugin.java.JavaPlugin;
import org.yuemi.git.H2PasswordProvider;
import org.yuemi.commands.GitRootCommand;

public class CommandRegistrar {

    private final JavaPlugin plugin;

    public CommandRegistrar(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void registerAll() {
        try {
            H2PasswordProvider provider = new H2PasswordProvider(plugin.getDataFolder());
            String[] passwords = provider.getOrGeneratePasswords();
            String filePassword = passwords[0];
            String dbPassword = passwords[1];

            plugin.getCommand("git").setExecutor(new GitRootCommand(plugin, filePassword, dbPassword));

        } catch (Exception e) {
            plugin.getLogger().severe("§cFailed to initialize Git command: " + e.getMessage());
        }
    }
}
