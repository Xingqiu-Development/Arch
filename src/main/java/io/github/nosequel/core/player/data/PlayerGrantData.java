package io.github.nosequel.core.player.data;

import com.google.gson.JsonObject;
import io.github.nosequel.core.CorePlugin;
import io.github.nosequel.core.commands.grant.menu.GrantsMenu;
import io.github.nosequel.core.commands.info.InfoableData;
import io.github.nosequel.core.commands.info.menu.InfoMenu;
import io.github.nosequel.core.commands.info.menu.InfoMenuInstance;
import io.github.nosequel.core.player.CorePlayer;
import io.github.nosequel.core.player.grant.Grant;
import io.github.nosequel.core.player.grant.GrantController;
import io.github.nosequel.core.rank.RankController;
import io.github.nosequel.core.util.Expirable;
import io.github.nosequel.core.util.SortableArrayList;
import io.github.nosequel.core.util.data.impl.SaveableData;
import io.github.nosequel.core.util.json.JsonAppender;
import io.github.nosequel.core.util.json.JsonUtils;
import io.github.nosequel.katakuna.MenuHandler;
import io.github.nosequel.katakuna.button.Button;
import io.github.nosequel.katakuna.button.impl.ButtonBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class PlayerGrantData implements SaveableData, InfoableData {

    private final String savePath = "grants";

    private final RankController rankController = CorePlugin.getInstance().getHandler().find(RankController.class);
    private final GrantController grantController = CorePlugin.getInstance().getHandler().find(GrantController.class);

    private SortableArrayList<Grant> grants = new SortableArrayList<>(Comparator.comparingInt(grant -> -grant.getRank().getWeight()));

    /**
     * Create a new PlayerGrantData object
     * Automatically adds default grants to the player's grants
     */
    public PlayerGrantData() {
        grantController.getDefaultGrants().stream()
                .filter(grant -> !grants.contains(grant))
                .forEach(this.grants::add);
    }

    /**
     * Create a new PlayerGrantData object with a set list of grants
     *
     * @param grants the list of grants to add to the player
     */
    public PlayerGrantData(List<Grant> grants) {
        this.grants.addAll(grants);
    }

    /**
     * Load a PlayerGrantData object from a {@link JsonObject}
     *
     * @param object the object to load the PlayerGrantData object from.
     */
    public PlayerGrantData(JsonObject object) {
        // load grants
        JsonUtils.getMap(JsonUtils.getJsonFromString(object.get("grants").getAsString()))
                .forEach((string, element) -> this.grants.add(new Grant(JsonUtils.getJsonFromString(element.getAsString()))));
    }

    /**
     * Get a list of active grants which have not expired yet.
     *
     * @return the list of grants
     */
    public List<Grant> getActiveGrants() {
        return this.grants.stream()
                .filter(Expirable::isActive)
                .collect(Collectors.toList());
    }

    /**
     * Get the main grant of a player
     *
     * @return the main grant
     */
    public Grant getGrant() {
        return this.getActiveGrants().get(0);
    }

    /**
     * Fix the grants in a PlayerGrantData object
     */
    public void fixGrants() {
        this.grants.stream()
                .filter(grant -> rankController.findRank(grant.getRank().getUuid()) == null)
                .forEach(this.grants::remove);
    }

    @Override
    public Button getButton(CorePlayer target) {
        return new ButtonBuilder(Material.PAPER)
                .setDisplayName(ChatColor.BLUE + "Grants")
                .setAmount(1)
                .setLore(
                        ChatColor.GREEN + "Amount of Grants: " + ChatColor.AQUA + this.getGrants().size(),
                        ChatColor.GREEN + "Rank: " + ChatColor.AQUA + ChatColor.translateAlternateColorCodes('&', this.getGrant().getRank().getGeneralRankData().getDisplayName())
                )
                .setIndex(9)
                .setAction((type, player) -> {
                    final InfoMenu infoMenu = (InfoMenu) MenuHandler.get().findMenu(player);
                    new InfoMenuInstance(new GrantsMenu(player, target), infoMenu).updateMenu();
                });
    }

    @Override
    public JsonObject toJson() {
        return new JsonAppender()
                .append("grants", JsonUtils.getFromMap(grants.stream().collect(Collectors.toMap(
                        grant -> grant.getUuid().toString(),
                        grant -> grant.toJsonObject().toString())
                )).toString()).get();
    }

}