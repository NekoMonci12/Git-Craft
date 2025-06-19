package org.yuemi.commands.subcommands;

import org.yuemi.commands.SubcommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.yuemi.git.GitManager;
import org.yuemi.git.GitCredentialDatabaseManager;

import java.io.File;

public class GitFetchSubcommand implements SubcommandExecutor {

    private final JavaPlugin plugin;
    private final GitCredentialDatabaseManager credentialDB;

    public GitFetchSubcommand(JavaPlugin plugin, String filePassword, String dbPassword) {
        this.plugin = plugin;
        this.credentialDB = new GitCredentialDatabaseManager(plugin.getDataFolder(), filePassword, dbPassword);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            File repoFolder = new File(".");

            for (String arg : args) {
                if (arg.startsWith("--path=")) {
                    repoFolder = new File(arg.substring("--path=".length()));
                }
            }

            try {
                String[] creds = credentialDB.getCredentials();
                if (creds == null) {
                    sender.sendMessage("§cNo stored credentials found. Use §e/git login§c to set them.");
                    return;
                }

                String username = creds[0];
                String token = creds[1];

                GitManager git = new GitManager(repoFolder);
                git.fetch(username, token);
                sender.sendMessage("§aFetched remote updates.");
            } catch (Exception e) {
                sender.sendMessage("§cGit fetch failed: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
