package io.github.nosequel.core.commands.info;

import io.github.nosequel.core.commands.info.menu.InfoMenu;
import io.github.nosequel.core.player.CorePlayer;
import io.github.nosequel.core.util.command.annotation.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InfoCommand {

    @Command(label="info", permission="core.info")
    public void info(CommandSender sender, CorePlayer target) {
        if(sender instanceof Player) {
            new InfoMenu((Player) sender, target).updateMenu();
        } else {

        }

    }

}
