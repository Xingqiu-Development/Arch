package io.github.nosequel.core.commands.cosmetic;

import io.github.nosequel.core.commands.cosmetic.menu.ChatColorMenu;
import io.github.nosequel.core.commands.cosmetic.menu.NameColorMenu;
import io.github.nosequel.core.util.command.annotation.Command;
import org.bukkit.entity.Player;

public class ColorCommands {

    @Command(label="chatcolor")
    public void chatcolor(Player player) {
        new ChatColorMenu(player).updateMenu();
    }

    @Command(label="namecolor")
    public void namecolor(Player player) { new NameColorMenu(player).updateMenu(); }

}
