package org.yuemi.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.yuemi.commands.SubcommandExecutor;
import org.yuemi.git.GitCredentialDatabaseManager;

import java.io.File;

public class GitWhoamiSubcommand implements SubcommandExecutor {

    private final JavaPlugin plugin;
    private final GitCredentialDatabaseManager credentialDB;

    public GitWhoamiSubcommand(JavaPlugin plugin, String filePassword, String dbPassword) {
        this.plugin = plugin;
        this.credentialDB = new GitCredentialDatabaseManager(plugin.getDataFolder(), filePassword, dbPassword);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                String[] creds = credentialDB.getCredentials();
                if (creds != null) {
                    sender.sendMessage("§aLogged in as: §f" + creds[0]);
                } else {
                    sender.sendMessage("§eNo Git credentials saved.");
                }
            } catch (Exception e) {
                sender.sendMessage("§cError fetching credentials: " + e.getMessage());
            }
        });
    }
}
