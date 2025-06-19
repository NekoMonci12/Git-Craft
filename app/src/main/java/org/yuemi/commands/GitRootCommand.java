package org.yuemi.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.yuemi.commands.SubcommandHandler;
import org.yuemi.commands.subcommands.GitHelpSubcommand;

public class GitRootCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private final SubcommandHandler handler;

    public GitRootCommand(JavaPlugin plugin, String filePassword, String dbPassword) {
        this.plugin = plugin;
        this.handler = new SubcommandHandler(plugin, filePassword, dbPassword);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            new GitHelpSubcommand(plugin).execute(sender, args);
            return true;
        }


        String subcommand = args[0].toLowerCase();
        String[] subArgs = java.util.Arrays.copyOfRange(args, 1, args.length);

        handler.handle(sender, subcommand, subArgs);
        return true;
    }
}
