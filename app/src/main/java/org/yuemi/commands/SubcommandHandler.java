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
import org.yuemi.commands.subcommands.GitLoginSubcommand;
import org.yuemi.commands.subcommands.GitWhoamiSubcommand;
import org.yuemi.commands.subcommands.GitLogoutSubcommand;
import java.util.HashMap;
import java.util.Map;

public class SubcommandHandler {

    private final Map<String, SubcommandExecutor> commands = new HashMap<>();
    private final String filePassword;
    private final String dbPassword;
    private final Map<String, String> permissions = new HashMap<>();
    
    public SubcommandHandler(JavaPlugin plugin, String filePassword, String dbPassword) {
        this.filePassword = filePassword;
        this.dbPassword = dbPassword;
        register("add", new GitAddSubcommand(plugin));
        register("commit", new GitCommitSubcommand(plugin));
        register("reset", new GitResetSubcommand(plugin));
        register("init", new GitInitSubcommand(plugin));
        register("remote", new GitRemoteSubcommand(plugin));
        register("status", new GitStatusSubcommand(plugin));
        register("help", new GitHelpSubcommand(plugin));
        register("push", new GitPushSubcommand(plugin, filePassword, dbPassword));
        register("fetch", new GitFetchSubcommand(plugin, filePassword, dbPassword));
        register("pull", new GitPullSubcommand(plugin, filePassword, dbPassword));
        register("login", new GitLoginSubcommand(plugin, filePassword, dbPassword));
        register("whoami", new GitWhoamiSubcommand(plugin, filePassword, dbPassword));
        register("logout", new GitLogoutSubcommand(plugin, filePassword, dbPassword));
    }

    public void handle(CommandSender sender, String name, String[] args) {
        SubcommandExecutor executor = commands.get(name.toLowerCase());
        if (executor == null) {
            sender.sendMessage("§cUnknown subcommand: " + name);
            return;
        }

        String permission = permissions.get(name.toLowerCase());
        if (permission != null && !sender.hasPermission(permission)) {
            sender.sendMessage("§cYou do not have permission to run /git " + name);
            return;
        }

        executor.execute(sender, args);
    }

    private void register(String name, SubcommandExecutor executor) {
        commands.put(name, executor);
        permissions.put(name, "gitcraft.command." + name);
    }
}
