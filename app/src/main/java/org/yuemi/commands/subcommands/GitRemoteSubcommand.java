package org.yuemi.commands.subcommands;

import org.yuemi.commands.SubcommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.yuemi.git.GitManager;

import java.io.File;

public class GitRemoteSubcommand implements SubcommandExecutor {

    private final JavaPlugin plugin;

    public GitRemoteSubcommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            File repoFolder = new File(".");
            String url = null;
            String mode = "add";

            for (String arg : args) {
                if (arg.startsWith("--path=")) {
                    repoFolder = new File(arg.substring("--path=".length()));
                } else if (arg.startsWith("--url=")) {
                    url = arg.substring("--url=".length());
                } else if (arg.equalsIgnoreCase("--add")) {
                    mode = "add";
                } else if (arg.equalsIgnoreCase("--set-url")) {
                    mode = "set";
                } else if (arg.equalsIgnoreCase("--remove")) {
                    mode = "remove";
                }
            }

            try {
                GitManager git = new GitManager(repoFolder);

                switch (mode) {
                    case "add":
                        if (url == null) throw new IllegalArgumentException("Missing --url");
                        git.addRemote(url);
                        sender.sendMessage("§aAdded remote origin: " + url);
                        break;
                    case "set":
                        if (url == null) throw new IllegalArgumentException("Missing --url");
                        git.setRemoteUrl(url);
                        sender.sendMessage("§aUpdated remote origin URL to: " + url);
                        break;
                    case "remove":
                        git.removeRemote();
                        sender.sendMessage("§aRemoved remote origin.");
                        break;
                }

            } catch (Exception e) {
                sender.sendMessage("§cGit remote failed: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
