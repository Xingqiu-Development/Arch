package io.github.nosequel.core.commands.cosmetic.menu;

import io.github.nosequel.core.CorePlugin;
import io.github.nosequel.core.player.CorePlayer;
import io.github.nosequel.core.player.CorePlayerController;
import io.github.nosequel.core.player.data.PlayerCosmeticData;
import io.github.nosequel.core.util.WoolColor;
import io.github.nosequel.katakuna.button.Button;
import io.github.nosequel.katakuna.button.impl.ButtonBuilder;
import io.github.nosequel.katakuna.menu.Menu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class NameColorMenu extends Menu {

    private final CorePlayerController playerController = CorePlugin.getInstance().getHandler().find(CorePlayerController.class);

    public NameColorMenu(Player player) {
        super(player, "Select a Color", 27);
    }

    @Override
    public List<Button> getButtons() {
        final AtomicInteger integer = new AtomicInteger();
        final CorePlayer player = playerController.find(this.getPlayer());
        final PlayerCosmeticData cosmeticData = player.findData(PlayerCosmeticData.class);

        final List<Button> buttons = Arrays.stream(ChatColor.values())
                .filter(ChatColor::isColor)

                .map(color -> new ButtonBuilder(Material.WOOL).setIndex(integer.getAndIncrement())
                        .setDisplayName(color + color.name()).setData(WoolColor.getWoolColor(color))
                        .setLore(
                                ChatColor.YELLOW + "Click to select the " + color + color.name() + ChatColor.YELLOW + " color"
                        )
                        .setAction((type, entity) -> {
                            cosmeticData.setNameColor(color);
                            entity.closeInventory();
                        })

                ).collect(Collectors.toList());

        buttons.add(new ButtonBuilder(Material.WOOL)
                .setIndex(26)
                .setData(WoolColor.getWoolColor(ChatColor.RED))
                .setDisplayName(ChatColor.RED + "Reset your name color")
                .setAction((type, entity) -> cosmeticData.setNameColor(null))
        );

        return buttons;
    }
}