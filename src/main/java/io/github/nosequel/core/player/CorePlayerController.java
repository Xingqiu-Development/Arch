package io.github.nosequel.core.player;

import io.github.nosequel.core.CorePlugin;
import io.github.nosequel.core.controller.Controller;
import io.github.nosequel.core.controller.ControllerPriority;
import io.github.nosequel.core.player.data.*;
import io.github.nosequel.core.util.data.Data;
import io.github.nosequel.core.util.data.DataController;
import io.github.nosequel.core.util.database.DatabaseController;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Getter
public class CorePlayerController implements DataController<CorePlayer, Data>, Controller {

    private final ControllerPriority loadPriority = ControllerPriority.LOW;

    private final List<CorePlayer> players = new ArrayList<>();
    private final List<? extends Data> registeredData = new ArrayList<>(Arrays.asList(
            new PlayerGeneralData(),
            new PlayerGrantData(),
            new PlayerPunishmentData(),
            new PlayerServerData(),
            new PlayerCosmeticData()
    ));

    private DatabaseController databaseController;

    @Override
    public void load() {
        this.databaseController = CorePlugin.getInstance().getHandler().find(DatabaseController.class);
        this.databaseController.getDataHandler().loadAll(this, "profiles", CorePlayer.class);

        Bukkit.getOnlinePlayers().stream()
                .filter(player -> this.find(player) == null)
                .forEach(player -> this.players.add(new CorePlayer(player)));
    }

    @Override
    public void unload() {
        this.players.forEach(player -> databaseController.getDataHandler().save(player, "profiles"));
    }

    /**
     * Find a {@link CorePlayer} object from a {@link Player} object
     * Uses UUID from both the {@link CorePlayer} and {@link Player} to retrieve the CorePlayer object.
     *
     * @param player the player
     * @return the found CorePlayer
     */
    public CorePlayer find(Player player) {
        return this.players.stream()
                .filter(current -> current.getUuid().equals(player.getUniqueId()))
                .findFirst().orElseGet(() -> new CorePlayer(player));
    }

    /**
     * Find a {@link CorePlayer} object from a {@link String}
     *
     * @param id the name of the core player
     * @return the found CorePlayer
     */
    public CorePlayer find(String id) {
        return this.players.stream()
                .filter(current -> current.findData(PlayerGeneralData.class).getName().equalsIgnoreCase(id))
                .findFirst().orElse(null);
    }

    /**
     * Find a {@link CorePlayer} object from a {@link UUID} object
     *
     * @param id the unique identifier used to identify the player
     * @return the found CorePlayer
     */
    public CorePlayer find(UUID id) {
        return this.players.stream()
                .filter(current -> current.getUuid().equals(id))
                .findFirst().orElse(null);
    }

    /**
     * Reload a player's data from the database
     *
     * @param player the player to reload
     */
    public void reload(CorePlayer player) {
        this.players.remove(player);
        this.databaseController.getDataHandler().load(this, CorePlayer.class, player.getUuid(), "profiles");
    }

    /**
     * Method for creating a new {@link CorePlayer} object from an UUID and a name
     *
     * @param uuid the uuid of the player
     * @param name the name of the player
     *
     */
    public CorePlayer insertUser(UUID uuid, String name) {
        return new CorePlayer(name, uuid);
    }

}