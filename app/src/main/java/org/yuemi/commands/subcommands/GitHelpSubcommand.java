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
        sender.sendMessage("§a/git remote --add|--set-url|--remove --url=<url> §7[--path=dir]");
        sender.sendMessage("§a/git add [file1 file2 ...] §7or §a/git add . §7[--path=dir]");
        sender.sendMessage("§a/git reset [--hard|file] §7[--path=dir]");
        sender.sendMessage("§a/git commit -m=<msg> §7[--path=dir]");
        sender.sendMessage("§a/git status §7[--path=dir]");
        sender.sendMessage("§a/git fetch --username=<u> --token=<t> §7[--path=dir]");
        sender.sendMessage("§a/git pull --username=<u> --token=<t> §7[--path=dir]");
        sender.sendMessage("§a/git push --username=<u> --token=<t> §7[--path=dir]");
        sender.sendMessage("§a/git help §8→ Shows this message");
    }
}
