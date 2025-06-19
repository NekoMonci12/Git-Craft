package org.yuemi.commands.subcommands;

import org.yuemi.commands.SubcommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.yuemi.git.GitManager;
import org.eclipse.jgit.api.Status;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GitStatusSubcommand implements SubcommandExecutor {

    private final JavaPlugin plugin;

    public GitStatusSubcommand(JavaPlugin plugin) {
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
                Status status = git.getStatus();

                Stream<String> changes = Stream.concat(
                        status.getUncommittedChanges().stream().map(f -> "Uncommitted: " + f),
                        Stream.concat(
                            status.getUntracked().stream().map(f -> "Untracked: " + f),
                            status.getModified().stream().map(f -> "Modified: " + f)
                        )
                );

                List<String> messages = changes.collect(Collectors.toList());

                if (messages.isEmpty()) {
                    sender.sendMessage("§aClean working directory.");
                    return;
                }

                if (sender instanceof Player) {
                    messages.stream().limit(10).forEach(msg -> sender.sendMessage("§e" + msg));
                    if (messages.size() > 10) {
                        sender.sendMessage("§7... and " + (messages.size() - 10) + " more.");
                    }
                } else {
                    messages.forEach(msg -> sender.sendMessage("[GitStatus] " + msg));
                }

            } catch (Exception e) {
                sender.sendMessage("§cGit status failed: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
