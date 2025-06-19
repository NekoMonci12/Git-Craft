package org.yuemi.commands.subcommands;

import org.yuemi.commands.SubcommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.yuemi.commands.SubcommandExecutor;

public class GitHelpSubcommand implements SubcommandExecutor {

    private final JavaPlugin plugin;

    public GitHelpSubcommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("§2==== GitCraft Command Help ====");
        sender.sendMessage("§a/git init §7--path=<dir> §8→ Initialize repo in folder");
        sender.sendMessage("§a/git remote --add|--set-url|--remove --url=<url> §7[--path=<dir>]");
        sender.sendMessage("§8  §7(§cDisallows public Git hosts unless §fdisable-unsafe-warning=true§7 in config.yml§7)");
        sender.sendMessage("§a/git add [file1 file2 ...] §7or §a/git add . §7[--path=<dir>]");
        sender.sendMessage("§a/git reset [--hard|file] §7[--path=<dir>]");
        sender.sendMessage("§a/git commit -m=<msg> §7[--path=<dir>] §8(≤ 50 characters)");
        sender.sendMessage("§a/git status §7[--path=<dir>]");
        sender.sendMessage("§a/git fetch §7[--path=<dir>] §8→ Uses stored credentials");
        sender.sendMessage("§a/git pull §7[--path=<dir>] §8→ Uses stored credentials");
        sender.sendMessage("§a/git push §7[--path=<dir>] §8→ Uses stored credentials");
        sender.sendMessage("§a/git login --username=<u> --token=<t> §8→ Stores credentials securely");
        sender.sendMessage("§a/git whoami §8→ Shows stored username");
        sender.sendMessage("§a/git logout §8→ Deletes stored credentials");
        sender.sendMessage("§a/git help §8→ Shows this help message");
    }
}
