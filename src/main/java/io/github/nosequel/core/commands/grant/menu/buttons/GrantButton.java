package io.github.nosequel.core.commands.grant.menu.buttons;

import io.github.nosequel.core.CorePlugin;
import io.github.nosequel.core.player.CorePlayer;
import io.github.nosequel.core.player.CorePlayerController;
import io.github.nosequel.core.player.data.PlayerGrantProcedureData;
import io.github.nosequel.core.player.data.PlayerGrantData;
import io.github.nosequel.core.rank.Rank;
import io.github.nosequel.core.util.MessageConstants;
import io.github.nosequel.core.util.WoolColor;
import io.github.nosequel.katakuna.button.Button;
import io.github.nosequel.katakuna.button.Callback;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Arrays;
import java.util.List;

public class GrantButton implements Button {

    private int index;

    private final Rank rank;
    private final CorePlayer target;
    private final CorePlayerController playerController = CorePlugin.getInstance().getHandler().find(CorePlayerController.class);

    /**
     * Constructor for creating a new GrantButton object
     *
     * @param rank   the rank to grant
     * @param target the target to grant the rank to
     * @param index  the index of the button in the menu
     */
    public GrantButton(Rank rank, CorePlayer target, int index) {
        this.rank = rank;
        this.target = target;
        this.index = index;
    }

    @Override
    public int getIndex() {
        return this.index;
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
        return WoolColor.getWoolColor(rank.getGeneralRankData().getColor());
    }

    @Override
    public Callback<ClickType, Player> getAction() {
        return (type, player) -> {
            player.sendMessage(MessageConstants.GRANT_SETTING_REASON);
            player.closeInventory();

            final PlayerGrantData grantData = target.findData(PlayerGrantData.class);
            final CorePlayer corePlayer = playerController.find(player);
            final PlayerGrantProcedureData data = corePlayer.findData(PlayerGrantProcedureData.class);

            if (grantData != null && data != null) {
                data.setup(rank, target);
            }
        };
    }

    @Override
    public String getDisplayName() {
        return ChatColor.translateAlternateColorCodes('&', rank.getGeneralRankData().getDisplayName());
    }

    @Override
    public List<String> getLore() {
        return Arrays.asList(
                ChatColor.BLUE + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat("-", 27),
                ChatColor.YELLOW + "Click here to grant the " + ChatColor.translateAlternateColorCodes('&', rank.getGeneralRankData().getDisplayName()) + ChatColor.YELLOW + " rank",
                ChatColor.BLUE + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat("-", 27)
        );
    }

    @Override
    public int getAmount() {
        return 1;
    }
}
