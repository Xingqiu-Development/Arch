package io.github.nosequel.core.player;

import io.github.nosequel.core.CorePlugin;
import io.github.nosequel.core.controller.Controller;
import io.github.nosequel.core.controller.ControllerPriority;
import io.github.nosequel.core.player.data.PlayerGeneralData;
import io.github.nosequel.core.player.data.PlayerGrantData;
import io.github.nosequel.core.player.data.PlayerPunishmentData;
import io.github.nosequel.core.player.data.PlayerServerData;
import io.github.nosequel.core.util.data.Data;
import io.github.nosequel.core.util.data.DataController;
import io.github.nosequel.core.util.database.DatabaseController;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class CorePlayerController implements DataController<CorePlayer, Data>, Controller {

    private final ControllerPriority loadPriority = ControllerPriority.LOW;

    private final List<CorePlayer> players = new ArrayList<>();
    private final List<? extends Data> registeredData = new ArrayList<>(Arrays.asList(
            new PlayerGeneralData(),
            new PlayerGrantData(),
            new PlayerPunishmentData(),
            new PlayerServerData()
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
     * Get all online players sorted by their rank's weight
     *
     * @return the list of players
     */
    public List<CorePlayer> getOnlineSortedPlayers() {
        return Bukkit.getOnlinePlayers().stream()
                .map(this::find)
                .sorted(Comparator.comparingInt(profile -> profile.findData(PlayerGrantData.class).getGrant().getRank().getWeight())).sorted(Collections.reverseOrder())
                .collect(Collectors.toList());
    }

    /**
     * Filter all data of a provided list by players and return a list of them
     *
     * @param players the list of players
     * @param clazz   the class of the data
     * @param <T>     the type of the data
     * @return the list of filtered data
     */
    public <T extends Data> List<T> filterData(List<CorePlayer> players, Class<T> clazz) {
        return players.stream()
                .map(player -> player.findData(clazz))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
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