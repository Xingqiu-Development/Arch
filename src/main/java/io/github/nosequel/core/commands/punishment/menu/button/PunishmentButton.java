package io.github.nosequel.core.commands.punishment.menu.button;

import io.github.nosequel.core.commands.punishment.menu.HistoryMenu;
import io.github.nosequel.core.player.CorePlayer;
import io.github.nosequel.core.player.data.PlayerPunishmentData;
import io.github.nosequel.core.player.punishments.Punishment;
import io.github.nosequel.core.util.DurationUtil;
import io.github.nosequel.core.util.MessageConstants;
import io.github.nosequel.core.util.WoolColor;
import io.github.nosequel.core.util.menu.ConfirmMenu;
import io.github.nosequel.katakuna.button.Button;
import io.github.nosequel.katakuna.button.Callback;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PunishmentButton implements Button {

    private final Punishment punishment;
    private final CorePlayer target;
    private int index;

    /**
     * Constructor for making a new PunishmentButton object
     *
     * @param punishment the punishment to get the info from
     * @param target     the player who contains the punishment
     * @param index      the index of the button in the menu
     */
    public PunishmentButton(Punishment punishment, CorePlayer target, int index) {
        this.punishment = punishment;
        this.target = target;
        this.index = index;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public void index(int i) {
        this.index = i;
    }

    @Override
    public Material getMaterial() {
        return Material.WOOL;
    }

    @Override
    public byte getData() {
        return WoolColor.getWoolColor(punishment.isActive() ? ChatColor.GREEN : ChatColor.RED);
    }

    @Override
    public Callback<ClickType, Player> getAction() {
        return (type, player) -> {
            if(type.isKeyboardClick()) {
                new ConfirmMenu(player, "Confirm Delete Punishment", (clickType, entity) -> {

                    target.findData(PlayerPunishmentData.class).getPunishments().remove(punishment);
                    new HistoryMenu(player, target).updateMenu();

                }).updateMenu();
            } else {
                new ConfirmMenu(player, "Confirm " + (punishment.isActive() ? "Unpunish" : "Repunish"), (clickType, entity) -> {

                    punishment.setActive(!punishment.isActive());
                    new HistoryMenu(player, target).updateMenu();

                }).updateMenu();
            }
        };
    }

    @Override
    public String getDisplayName() {
        return MessageConstants.PUNISHMENT_BUTTON_NAME
                .replace("%active%", punishment.isActive() ? "Active" : "Inactive")
                .replace("%id%", this.target.findData(PlayerPunishmentData.class).getPunishments().indexOf(punishment) + "");
    }

    @Override
    public List<String> getLore() {
        return Arrays.stream(punishment.isActive() ? MessageConstants.PUNISHMENT_INFO_BUTTON_ACTIVE : MessageConstants.PUNISHMENT_INFO_BUTTON_EXPIRED)
                .map(str -> str.replace("%reason%", punishment.getReason())
                        .replace("%executor%", punishment.getExecutor())
                        .replace("%issue_date%", DurationUtil.unixToDate(punishment.getStartEpoch()))
                        .replace("%expire_date%", (punishment.isActive() ? (punishment.getExpirationEpoch() == -1L ? "Never" : DurationUtil.millisToRoundedTime(punishment.getExpirationEpoch() - System.currentTimeMillis())) : DurationUtil.unixToDate(punishment.getExpirationEpoch())))
                        .replace("%reason%", punishment.getReason())
                        .replace("%active%", punishment.isActive() + "")
                        .replace("%type%", punishment.getType().name())
                ).collect(Collectors.toList());
    }

    @Override
    public int getAmount() {
        return 1;
    }
}