package org.yuemi.commands.subcommands;

import org.yuemi.commands.SubcommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.yuemi.git.GitManager;

import java.io.File;

public class GitInitSubcommand implements SubcommandExecutor {

    private final JavaPlugin plugin;

    public GitInitSubcommand(JavaPlugin plugin) {
        this.plugin = plugin;
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
                GitManager git = new GitManager(repoFolder);
                git.initRepo(plugin);
                sender.sendMessage("§aInitialized Git repository at: §f" + repoFolder.getAbsolutePath());
            } catch (Exception e) {
                sender.sendMessage("§cGit init failed: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
