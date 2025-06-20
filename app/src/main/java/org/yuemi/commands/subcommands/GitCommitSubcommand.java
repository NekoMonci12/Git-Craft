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

            StringBuilder messageBuilder = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (arg.startsWith("--path=")) {
                    repoFolder = new File(arg.substring("--path=".length()));
                } else if (arg.startsWith("--message=")) {
                    messageBuilder.append(arg.substring("--message=".length())).append(" ");
                } else if (arg.startsWith("-m=")) {
                    messageBuilder.append(arg.substring("-m=".length())).append(" ");
                } else if (arg.equals("-m") || arg.equals("--message")) {
                    for (int j = i + 1; j < args.length; j++) {
                        messageBuilder.append(args[j]).append(" ");
                    }
                    break;
                }
            }

            if (messageBuilder.length() > 0) {
                message = messageBuilder.toString().trim();
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
