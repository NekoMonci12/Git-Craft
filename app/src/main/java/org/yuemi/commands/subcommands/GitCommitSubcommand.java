package org.yuemi.commands.subcommands;

import org.yuemi.commands.SubcommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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

            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (arg.startsWith("--path=")) {
                    repoFolder = new File(arg.substring("--path=".length()));
                } else if (arg.startsWith("--message=")) {
                    message = arg.substring("--message=".length());
                } else if (arg.equals("-m") && i + 1 < args.length) {
                    message = args[i + 1];
                    i++; // Skip next token
                }
            }

            if (message.length() > 50) {
                sender.sendMessage("§cCommit message must not exceed 50 characters.");
                return;
            }

            String authorName;
            String authorEmail;
            if (sender instanceof Player player) {
                authorName = player.getName();
                authorEmail = player.getName().toLowerCase() + "@server.local";
            } else {
                authorName = "Console";
                authorEmail = "console@localhost";
            }

            try {
                GitManager git = new GitManager(repoFolder);
                git.commit(message, authorName, authorEmail);
                sender.sendMessage("§aCommitted as §f" + authorName + " §a→ §f" + message);
            } catch (Exception e) {
                sender.sendMessage("§cGit commit failed: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
