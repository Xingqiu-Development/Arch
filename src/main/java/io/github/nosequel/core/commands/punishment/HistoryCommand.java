package io.github.nosequel.core.commands.punishment;

import io.github.nosequel.core.commands.punishment.menu.HistoryMenu;
import io.github.nosequel.core.player.CorePlayer;
import io.github.nosequel.core.util.command.annotation.Command;
import org.bukkit.entity.Player;

public class HistoryCommand {

    @Command(label="history", permission="core.history")
    public void history(Player player, CorePlayer target) {
        new HistoryMenu(player, target).updateMenu();
    }

}
