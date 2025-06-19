package org.yuemi.commands.subcommands;

import org.yuemi.commands.SubcommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.yuemi.git.GitManager;

import java.io.File;

public class GitPullSubcommand implements SubcommandExecutor {

    private final JavaPlugin plugin;

    public GitPullSubcommand(JavaPlugin plugin) {
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
                sender.sendMessage("§cUsage: /git pull --username=... --token=...");
                return;
            }

            try {
                GitManager git = new GitManager(repoFolder);
                git.pull(username, token);
                sender.sendMessage("§aPulled and merged from origin.");
            } catch (Exception e) {
                sender.sendMessage("§cGit pull failed: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
