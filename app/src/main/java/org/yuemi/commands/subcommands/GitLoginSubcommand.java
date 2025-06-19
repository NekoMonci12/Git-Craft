package org.yuemi.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.yuemi.commands.SubcommandExecutor;
import org.yuemi.git.GitCredentialDatabaseManager;

import java.io.File;

public class GitLoginSubcommand implements SubcommandExecutor {

    private final JavaPlugin plugin;
    private final GitCredentialDatabaseManager credentialDB;

    public GitLoginSubcommand(JavaPlugin plugin, String filePassword, String dbPassword) {
        this.plugin = plugin;
        File pluginFolder = plugin.getDataFolder();
        this.credentialDB = new GitCredentialDatabaseManager(pluginFolder, filePassword, dbPassword);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            String username = null;
            String token = null;

            for (String arg : args) {
                if (arg.startsWith("--username=")) {
                    username = arg.substring("--username=".length());
                } else if (arg.startsWith("--token=")) {
                    token = arg.substring("--token=".length());
                }
            }

            if (username == null || token == null) {
                sender.sendMessage("§cUsage: /git login --username=<user> --token=<token>");
                return;
            }

            try {
                boolean replaced = credentialDB.saveCredentials(username, token, true);
                sender.sendMessage("§aGit credentials " + (replaced ? "updated" : "saved") + " securely.");
            } catch (Exception e) {
                sender.sendMessage("§cFailed to save credentials: " + e.getMessage());
                plugin.getLogger().warning("Git login error: " + e.getMessage()); // No token or username in logs
            }
        });
    }
}
