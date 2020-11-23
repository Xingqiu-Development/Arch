package io.github.nosequel.core.util.menu;

import io.github.nosequel.core.util.WoolColor;
import io.github.nosequel.katakuna.button.Button;
import io.github.nosequel.katakuna.button.Callback;
import io.github.nosequel.katakuna.button.impl.ButtonBuilder;
import io.github.nosequel.katakuna.menu.Menu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Arrays;
import java.util.List;

public class ConfirmMenu extends Menu {

    private final Callback<ClickType, HumanEntity> action;

    public ConfirmMenu(Player player, String title, Callback<ClickType, HumanEntity> action) {
        super(player, title, 9);

        this.action = action;
    }

    @Override
    public List<Button> getButtons() {
        return Arrays.asList(
                new ButtonBuilder(Material.WOOL)
                        .setIndex(2)
                        .setData(WoolColor.getWoolColor(ChatColor.GREEN))
                        .setDisplayName(ChatColor.GREEN + "Confirm")
                        .setLore(
                                ChatColor.GRAY + "Click here to confirm your action"
                        ).setAction(this.action::accept),
                new ButtonBuilder(Material.WOOL)
                        .setIndex(6)
                        .setData(WoolColor.getWoolColor(ChatColor.RED))
                        .setDisplayName(ChatColor.RED + "Cancel")
                        .setLore(
                                ChatColor.GRAY + "Click here to cancel your action"
                        ).setAction((type, player) -> player.closeInventory())
        );
    }
}