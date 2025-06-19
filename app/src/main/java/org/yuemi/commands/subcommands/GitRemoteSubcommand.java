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

                boolean allowUnsafe = plugin.getConfig().getBoolean("disable-unsafe-warning", false);

                if ((mode.equals("add") || mode.equals("set")) && url != null) {
                    if (!allowUnsafe && (url.contains("github.com") || url.contains("gitlab.com"))) {
                        sender.sendMessage("§cRemote URL rejected: Public Git hosts like GitHub or GitLab are disallowed.");
                        sender.sendMessage("§7To allow them, set §fdisable-unsafe-warning: true §7in config.yml");
                        return;
                    }
                }

                switch (mode) {
                    case "add" -> {
                        if (url == null) throw new IllegalArgumentException("Missing --url");
                        git.addRemote(url);
                        sender.sendMessage("§aAdded remote origin: " + url);
                    }
                    case "set" -> {
                        if (url == null) throw new IllegalArgumentException("Missing --url");
                        git.setRemoteUrl(url);
                        sender.sendMessage("§aUpdated remote origin URL to: " + url);
                    }
                    case "remove" -> {
                        git.removeRemote();
                        sender.sendMessage("§aRemoved remote origin.");
                    }
                }

                if ((mode.equals("add") || mode.equals("set")) && url != null && !allowUnsafe) {
                    sender.sendMessage("§eWarning: Using public git hosts is discouraged.");
                    sender.sendMessage("§7Please use a self-hosted alternative like §fGitea§7, §fGogs§7, or §fForgejo§7.");
                }

            } catch (Exception e) {
                sender.sendMessage("§cGit remote failed: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
