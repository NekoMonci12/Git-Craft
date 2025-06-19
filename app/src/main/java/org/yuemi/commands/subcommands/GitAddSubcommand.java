package org.yuemi.commands.subcommands;

import org.yuemi.commands.SubcommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.yuemi.git.GitManager;
import java.util.List;
import java.util.ArrayList;
import java.io.File;

public class GitAddSubcommand implements SubcommandExecutor {

    private final JavaPlugin plugin;

    public GitAddSubcommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            File repoFolder = new File("."); // Default to root
            List<String> filesToAdd = new ArrayList<>();

            // Parse args
            for (String arg : args) {
                if (arg.equals(".")) {
                    // Use server root, add everything
                    repoFolder = new File(".");
                } else if (arg.startsWith("--path=")) {
                    repoFolder = new File(arg.substring("--path=".length()));
                } else if (arg.startsWith("--include=")) {
                    filesToAdd.add(arg.substring("--include=".length()));
                } else if (!arg.startsWith("--")) {
                    // Add as literal file path
                    filesToAdd.add(arg);
                }
            }

            try {
                if (!repoFolder.exists() || !repoFolder.isDirectory()) {
                    sender.sendMessage("§cInvalid path: " + repoFolder.getAbsolutePath());
                    return;
                }

                GitManager git = new GitManager(repoFolder);

                if (filesToAdd.isEmpty()) {
                    git.addAll();
                    sender.sendMessage("§aStaged ALL files in: §f" + repoFolder.getCanonicalPath());
                } else {
                    git.addFiles(filesToAdd);
                    sender.sendMessage("§aStaged specific files in: §f" + repoFolder.getCanonicalPath());
                    filesToAdd.forEach(f -> sender.sendMessage("§7- §f" + f));
                }
            } catch (Exception e) {
                sender.sendMessage("§cGit add failed: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

}
