package org.yuemi.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.yuemi.commands.subcommands.GitAddSubcommand;
import org.yuemi.commands.subcommands.GitCommitSubcommand;
import org.yuemi.commands.subcommands.GitPushSubcommand;
import org.yuemi.commands.subcommands.GitResetSubcommand;
import org.yuemi.commands.subcommands.GitInitSubcommand;
import org.yuemi.commands.subcommands.GitRemoteSubcommand;
import org.yuemi.commands.subcommands.GitFetchSubcommand;
import org.yuemi.commands.subcommands.GitPullSubcommand;
import org.yuemi.commands.subcommands.GitStatusSubcommand;
import org.yuemi.commands.subcommands.GitHelpSubcommand;
import java.util.HashMap;
import java.util.Map;

public class SubcommandHandler {

    private final Map<String, SubcommandExecutor> commands = new HashMap<>();

    public SubcommandHandler(JavaPlugin plugin) {
        commands.put("add", new GitAddSubcommand(plugin));
        commands.put("commit", new GitCommitSubcommand(plugin));
        commands.put("push", new GitPushSubcommand(plugin));
        commands.put("reset", new GitResetSubcommand(plugin));
        commands.put("init", new GitInitSubcommand(plugin));
        commands.put("remote", new GitRemoteSubcommand(plugin));
        commands.put("fetch", new GitFetchSubcommand(plugin));
        commands.put("pull", new GitPullSubcommand(plugin));
        commands.put("status", new GitStatusSubcommand(plugin));
        commands.put("help", new GitHelpSubcommand(plugin));
    }

    public void handle(CommandSender sender, String name, String[] args) {
        SubcommandExecutor executor = commands.get(name.toLowerCase());
        if (executor == null) {
            sender.sendMessage("§cUnknown subcommand: " + name);
            return;
        }

        executor.execute(sender, args);
    }
}
