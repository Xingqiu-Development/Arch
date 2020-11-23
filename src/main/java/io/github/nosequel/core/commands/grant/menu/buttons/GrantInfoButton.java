package io.github.nosequel.core.commands.grant.menu.buttons;

import io.github.nosequel.core.commands.grant.menu.GrantsMenu;
import io.github.nosequel.core.player.grant.Grant;
import io.github.nosequel.core.player.CorePlayer;
import io.github.nosequel.core.player.data.PlayerGrantData;
import io.github.nosequel.core.util.DurationUtil;
import io.github.nosequel.core.util.MessageConstants;
import io.github.nosequel.core.util.WoolColor;
import io.github.nosequel.core.util.menu.ConfirmMenu;
import io.github.nosequel.katakuna.button.Button;
import io.github.nosequel.katakuna.button.Callback;
import io.github.nosequel.katakuna.menu.Menu;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GrantInfoButton implements Button {

    private int index;

    private final Grant grant;
    private final CorePlayer target;

    /**
     * Constructor for creating a new GrantInfoButton object
     *
     * @param grant  the grant to get the info from
     * @param target the user who has the grant
     * @param index  the index of the button
     */
    public GrantInfoButton(Grant grant, CorePlayer target, int index) {
        this.grant = grant;
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
        return WoolColor.getWoolColor(grant.isActive() ? ChatColor.GREEN : ChatColor.RED);
    }

    @Override
    public Callback<ClickType, Player> getAction() {
        return (type, player) -> {
            final PlayerGrantData grantData = this.target.findData(PlayerGrantData.class);
            final Map<Callback<ClickType, HumanEntity>, String> actions = new HashMap<>();

            if (grantData.getActiveGrants().size() == 1 && grant.isActive()) {
                player.sendMessage(ChatColor.RED + "Failed removing grant, that is their only grant");
                return;

            } else if (type.isKeyboardClick()) {
                actions.put((clickType, entity) -> {

                    grantData.getGrants().remove(grant);
                    new GrantsMenu(player, target).updateMenu();

                }, "Delete Grant");
            } else {
                actions.put((clickType, entity) -> {

                    grant.setActive(!grant.isActive());

                    if (grant.isActive()) {
                        grant.setExpirationEpoch(-1L);
                    }

                    new GrantsMenu(player, target).updateMenu();

                }, "Expire Grant");
            }

            actions.forEach((callback, title) -> new ConfirmMenu(player, title, callback).updateMenu());
        };
    }

    @Override
    public String getDisplayName() {
        return MessageConstants.GRANT_INFO_BUTTON_NAME
                .replace("%id%", this.target.findData(PlayerGrantData.class).getGrants().indexOf(grant) + "")
                .replace("%active%", !grant.isActive() ? "Active" : "Inactive");
    }

    @Override
    public List<String> getLore() {
        return Arrays.stream(grant.isActive() ? MessageConstants.GRANT_INFO_BUTTON_ACTIVE : MessageConstants.GRANT_INFO_BUTTON_EXPIRED)
                .map(str ->
                        str.replace("%rank%", ChatColor.translateAlternateColorCodes('&', grant.getRank().getGeneralRankData().getDisplayName()))
                        .replace("%executor%", grant.getExecutor())
                        .replace("%reason%", grant.getReason())
                        .replace("%default%", grant.getRank().getGeneralRankData().isDef() + "")
                        .replace("%expire_date%", (grant.isActive() ? (grant.getExpirationEpoch() == -1L ? "Never" : DurationUtil.millisToRoundedTime(grant.getExpirationEpoch() - System.currentTimeMillis())) : DurationUtil.unixToDate(grant.getExpirationEpoch())))
                        .replace("%active%", grant.isActive() + "")
                ).collect(Collectors.toList());
    }

    @Override
    public int getAmount() {
        return 1;
    }
}