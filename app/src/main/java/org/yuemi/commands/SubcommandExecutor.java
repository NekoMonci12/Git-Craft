package org.yuemi.commands;

import org.bukkit.command.CommandSender;

public interface SubcommandExecutor {
    void execute(CommandSender sender, String[] args);
}
