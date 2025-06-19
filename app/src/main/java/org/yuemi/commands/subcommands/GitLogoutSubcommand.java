package org.yuemi.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.yuemi.commands.SubcommandExecutor;
import org.yuemi.git.GitCredentialDatabaseManager;

import java.io.File;

public class GitLogoutSubcommand implements SubcommandExecutor {

    private final JavaPlugin plugin;
    private final GitCredentialDatabaseManager credentialDB;

    public GitLogoutSubcommand(JavaPlugin plugin, String filePassword, String dbPassword) {
        this.plugin = plugin;
        this.credentialDB = new GitCredentialDatabaseManager(plugin.getDataFolder(), filePassword, dbPassword);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                boolean deleted = credentialDB.deleteCredentials();
                sender.sendMessage(deleted ? "§aLogged out successfully." : "§eNo credentials to delete.");
            } catch (Exception e) {
                sender.sendMessage("§cFailed to delete credentials: " + e.getMessage());
            }
        });
    }
}
