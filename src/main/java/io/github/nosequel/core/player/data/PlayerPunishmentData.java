package io.github.nosequel.core.player.data;

import com.google.gson.JsonObject;
import io.github.nosequel.core.commands.info.InfoableData;
import io.github.nosequel.core.commands.info.menu.InfoMenu;
import io.github.nosequel.core.commands.info.menu.InfoMenuInstance;
import io.github.nosequel.core.commands.punishment.menu.HistoryMenu;
import io.github.nosequel.core.player.CorePlayer;
import io.github.nosequel.core.player.punishments.Punishment;
import io.github.nosequel.core.player.punishments.PunishmentType;
import io.github.nosequel.core.util.MessageConstants;
import io.github.nosequel.core.util.data.impl.SaveableData;
import io.github.nosequel.core.util.json.JsonAppender;
import io.github.nosequel.core.util.json.JsonUtils;
import io.github.nosequel.katakuna.MenuHandler;
import io.github.nosequel.katakuna.button.Button;
import io.github.nosequel.katakuna.button.impl.ButtonBuilder;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PlayerPunishmentData implements InfoableData, SaveableData {

    private final String savePath = "punishments";
    private final List<Punishment> punishments = new ArrayList<>();

    public PlayerPunishmentData() { }

    /**
     * Constructor for loading the PlayerPunishmentData from a {@link JsonObject}
     *
     * @param object the json object to load the data from
     */
    public PlayerPunishmentData(JsonObject object) {
        // load punishments
        JsonUtils.getMap(JsonUtils.getJsonFromString(object.get("punishments").getAsString()))
                .forEach((string, element) -> this.punishments.add(new Punishment(JsonUtils.getJsonFromString(element.getAsString()))));
    }

    /**
     * Method to get all active punishmnets in a player's data object
     *
     * @return the list of active punishments
     */
    public List<Punishment> getActivePunishments() {
        return this.punishments.stream()
                .filter(Punishment::isActive)
                .collect(Collectors.toList());
    }

    /**
     * Method to get all active punishments in a player's data object with a specific type
     *
     * @param type the type of the punishment
     * @return the list of active punishments
     */
    public List<Punishment> getActivePunishments(PunishmentType type) {
        return this.getActivePunishments().stream()
                .filter(punishment -> punishment.getType().equals(type))
                .collect(Collectors.toList());
    }


    @Override
    public JsonObject toJson() {
        return new JsonAppender()
                .append("punishments", JsonUtils.getFromMap(punishments.stream().collect(Collectors.toMap(
                        punishment -> punishment.getUuid().toString(),
                        punishment -> punishment.toJsonObject().toString())
                )).toString()).get();
    }

    /**
     * Attempts to kick the player if the player has an active punishment
     */
    public void attemptPlayerBan(Player player) {
        if(!this.getActivePunishments(PunishmentType.BAN).isEmpty()) {
            player.kickPlayer(MessageConstants.PUNISHMENT_BAN_KICKED);
        }
    }

    @Override
    public Button getButton(CorePlayer target) {
        return new ButtonBuilder(Material.PAPER)
                .setDisplayName(ChatColor.BLUE + "Punishments")
                .setAmount(1)
                .setLore(
                        ChatColor.GREEN + "Punishments: " + ChatColor.AQUA + this.getPunishments().size(),
                        ChatColor.GREEN + "Active Punishments: " + ChatColor.AQUA + this.getActivePunishments().size()
                )
                .setIndex(10)
                .setAction((type, player) -> {
                    final InfoMenu infoMenu = (InfoMenu) MenuHandler.get().findMenu(player);
                    new InfoMenuInstance(new HistoryMenu(player, target), infoMenu).updateMenu();
                });
    }
}