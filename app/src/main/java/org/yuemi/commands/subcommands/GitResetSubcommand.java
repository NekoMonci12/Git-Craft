package org.yuemi.commands.subcommands;

import org.yuemi.commands.SubcommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.yuemi.git.GitManager;
import org.eclipse.jgit.api.ResetCommand.ResetType;

import java.io.File;

public class GitResetSubcommand implements SubcommandExecutor {

    private final JavaPlugin plugin;

    public GitResetSubcommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            File repoFolder = new File("."); // Default path
            ResetType resetType = ResetType.MIXED; // default is soft reset (unstage)

            if (args.length >= 1 && args[0].startsWith("--path=")) {
                String path = args[0].substring("--path=".length());
                repoFolder = new File(path);
            }

            try {
                GitManager git = new GitManager(repoFolder);

                if (args.length >= 1 && args[0].equalsIgnoreCase("--hard")) {
                    resetType = ResetType.HARD;
                    git.reset(resetType);
                    sender.sendMessage("§cHard reset performed.");
                    return;
                }

                if (args.length >= 1 && !args[0].startsWith("--")) {
                    String file = args[0];
                    git.resetFile(file);
                    sender.sendMessage("§aUnstaged file: §f" + file);
                    return;
                }

                git.reset(resetType);
                sender.sendMessage("§aReset staged changes.");
            } catch (Exception e) {
                sender.sendMessage("§cGit reset failed: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
