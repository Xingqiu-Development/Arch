package io.github.nosequel.core.commands.info.menu;

import io.github.nosequel.core.commands.info.InfoableData;
import io.github.nosequel.core.player.CorePlayer;
import io.github.nosequel.core.player.data.PlayerGeneralData;
import io.github.nosequel.core.player.data.PlayerGrantData;
import io.github.nosequel.core.player.data.PlayerPunishmentData;
import io.github.nosequel.core.player.data.PlayerServerData;
import io.github.nosequel.katakuna.button.Button;
import io.github.nosequel.katakuna.button.impl.ButtonBuilder;
import io.github.nosequel.katakuna.menu.Menu;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public class InfoMenu extends Menu {

    private final CorePlayer target;

    public InfoMenu(Player player, CorePlayer target) {
        super(player, "Info of a Player", 18);
        this.target = target;
    }

    @Override
    public List<Button> getButtons() {
        final List<Button> buttons = new ArrayList<>();
        final PlayerGeneralData playerData = target.findData(PlayerGeneralData.class);
        final PlayerGrantData grantData = target.findData(PlayerGrantData.class);
        final PlayerPunishmentData punishmentData = target.findData(PlayerPunishmentData.class);

        buttons.add(new ButtonBuilder(Material.NETHER_STAR)
                .setDisplayName(ChatColor.BLUE + playerData.getName())
                .setAmount(1)
                .setLore(
                        ChatColor.GREEN + "Grants: " + ChatColor.AQUA + grantData.getGrants().size(),
                        ChatColor.GREEN + "Rank: " + ChatColor.AQUA + ChatColor.translateAlternateColorCodes('&', grantData.getGrant().getRank().getGeneralRankData().getDisplayName()),
                        "",
                        ChatColor.GREEN + "Punishments: " + ChatColor.AQUA + punishmentData.getPunishments().size(),
                        ChatColor.GREEN + "Active Punishments: " + ChatColor.AQUA + punishmentData.getActivePunishments().size(),
                        "",
                        ChatColor.GREEN + "UUID: " + ChatColor.AQUA + target.getUuid().toString(),
                        ChatColor.GREEN + "Server: " + ChatColor.AQUA + target.findData(PlayerServerData.class).getServer(),
                        ChatColor.GREEN + "Status: " + (Bukkit.getPlayer(target.getUuid()) == null ? ChatColor.RED + "Offline" : ChatColor.DARK_GREEN + "Online")
                )
                .setIndex(4));

        target.getData().stream()
                .filter(data -> data instanceof InfoableData)
                .forEach(data -> buttons.add(((InfoableData) data).getButton(target)));

        return buttons;
    }
}