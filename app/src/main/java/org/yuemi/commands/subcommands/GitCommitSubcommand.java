package org.yuemi.commands.subcommands;

import org.yuemi.commands.SubcommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.yuemi.git.GitManager;

import java.io.File;

public class GitCommitSubcommand implements SubcommandExecutor {

    private final JavaPlugin plugin;

    public GitCommitSubcommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            File repoFolder = new File(".");
            String message = "Commit from Minecraft";

            for (String arg : args) {
                if (arg.startsWith("--path=")) {
                    repoFolder = new File(arg.substring("--path=".length()));
                } else if (arg.startsWith("--message=") || arg.startsWith("-m=")) {
                    message = arg.contains("=") ? arg.split("=", 2)[1] : message;
                }
            }

            try {
                GitManager git = new GitManager(repoFolder);
                git.commit(message);
                sender.sendMessage("§aCommitted: §f" + message);
            } catch (Exception e) {
                sender.sendMessage("§cGit commit failed: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
