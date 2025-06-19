package org.yuemi.commands.subcommands;

import org.yuemi.commands.SubcommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.yuemi.git.GitManager;

import java.io.File;

public class GitFetchSubcommand implements SubcommandExecutor {

    private final JavaPlugin plugin;

    public GitFetchSubcommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            File repoFolder = new File(".");
            String username = null;
            String token = null;

            for (String arg : args) {
                if (arg.startsWith("--path=")) {
                    repoFolder = new File(arg.substring("--path=".length()));
                } else if (arg.startsWith("--username=")) {
                    username = arg.substring("--username=".length());
                } else if (arg.startsWith("--token=")) {
                    token = arg.substring("--token=".length());
                }
            }

            if (username == null || token == null) {
                sender.sendMessage("§cUsage: /git fetch --username=... --token=...");
                return;
            }

            try {
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
