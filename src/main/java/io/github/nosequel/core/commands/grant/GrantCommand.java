package io.github.nosequel.core.commands.grant;

import io.github.nosequel.core.commands.grant.menu.GrantMenu;
import io.github.nosequel.core.commands.grant.menu.GrantsMenu;
import io.github.nosequel.core.player.CorePlayer;
import io.github.nosequel.core.util.command.annotation.Command;
import org.bukkit.entity.Player;

public class GrantCommand {

    @Command(label = "grant", permission = "core.grant")
    public void grant(Player player, CorePlayer target) {
        new GrantMenu(player, target).updateMenu();
    }

    @Command(label = "grants", permission = "core.grants")
    public void grants(Player player, CorePlayer target) {
        new GrantsMenu(player, target).updateMenu();
    }
}
