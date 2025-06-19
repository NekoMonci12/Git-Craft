package org.yuemi.commands.subcommands;

import org.yuemi.commands.SubcommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.yuemi.git.GitManager;
import org.eclipse.jgit.api.Status;
import java.util.ArrayList;
import java.io.File;
import java.util.List;

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

                List<String> changes = new ArrayList<>();

                status.getUncommittedChanges().forEach(file ->
                        changes.add("§cUnstaged: §7" + file));
                status.getModified().forEach(file ->
                        changes.add("§6Modified: §7" + file));
                status.getAdded().forEach(file ->
                        changes.add("§aStaged: §7" + file));
                status.getUntracked().forEach(file ->
                        changes.add("§fUntracked: §7" + file));

                if (changes.isEmpty()) {
                    sender.sendMessage("§aClean working directory.");
                    return;
                }

                if (sender instanceof Player) {
                    changes.stream().limit(10).forEach(sender::sendMessage);
                    if (changes.size() > 10) {
                        sender.sendMessage("§7... and " + (changes.size() - 10) + " more.");
                    }
                } else {
                    changes.forEach(sender::sendMessage);
                }

            } catch (Exception e) {
                sender.sendMessage("§cGit status failed: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
