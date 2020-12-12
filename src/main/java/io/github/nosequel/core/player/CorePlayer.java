package io.github.nosequel.core.player;

import io.github.nosequel.core.CorePlugin;
import io.github.nosequel.core.player.data.*;
import io.github.nosequel.core.player.sync.CorePlayerSynchronizationHandler;
import io.github.nosequel.core.util.PlayerUtil;
import io.github.nosequel.core.util.data.Data;
import io.github.nosequel.core.util.data.Loadable;
import io.github.nosequel.core.util.json.JsonAppender;
import io.github.nosequel.core.util.synchronize.SynchronizeController;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
public class CorePlayer implements Loadable<Data> {

    private final SynchronizeController synchronizeController = CorePlugin.getInstance().getHandler().find(SynchronizeController.class);
    private final CorePlayerController playerController = CorePlugin.getInstance().getHandler().find(CorePlayerController.class);

    private List<Data> data = new ArrayList<>();
    private UUID uuid;

    /**
     * Constructor for creating a new CorePlayer object
     *
     * @param name the name of the player object
     * @param uuid the uuid of the player object
     */
    public CorePlayer(String name, UUID uuid) {
        this.uuid = uuid;

        this.addData(new PlayerGeneralData(name));
        this.loadData();

        playerController.getPlayers().add(this);
    }

    /**
     * Constructor for creating a new CorePlayer object from a bukkit Player object
     *
     * @param player the player
     */
    public CorePlayer(Player player) {
        this(player.getName(), player.getUniqueId());
    }

    /**
     * Constructor for loading a CorePlayer object from the database
     *
     * @param uuid the unique identifier of the player
     * @param data the data of the player
     */
    public CorePlayer(UUID uuid, List<Data> data) {
        this.uuid = uuid;
        this.playerController.getPlayers().add(this);
        this.data = data;

        this.addData(new PlayerGrantProcedureData());

        if (Bukkit.getPlayer(this.uuid) != null) {
            this.findData(PlayerPunishmentData.class).attemptPlayerBan(Bukkit.getPlayer(this.uuid));
        }
    }

    /**
     * Reload a player's permissions
     */
    public void reloadPermissions() {
        final PlayerGrantData grantData = this.findData(PlayerGrantData.class);
        final Player player = Bukkit.getPlayer(this.uuid);

        if (grantData != null && player != null) {
            grantData.getGrants().stream()
                    .map(grant -> grant.getRank().getGeneralRankData().getPermissions())
                    .forEach(list -> list.forEach(permission -> PlayerUtil.addPermission(player, permission)));
        }
    }

    /**
     * Synchronize a {@link CorePlayer}'s data with other connected servers
     */
    public void sync() {
        playerController.getDatabaseController().getDataHandler().save(this, "profiles");
        synchronizeController.synchronize(
                CorePlayerSynchronizationHandler.class,
                new JsonAppender()
                        .append("uuid", this.getUuid().toString())
                        .get()
        );

    }

    public String getDisplayName() {
        return this.findData(PlayerGrantData.class).getGrant().getRank().getGeneralRankData().getColor() + this.findData(PlayerGeneralData.class).getName();
    }

    /**
     * Load the data objects of a player
     */
    private void loadData() {
        this.addData(new PlayerGrantData());
        this.addData(new PlayerGrantProcedureData());
        this.addData(new PlayerPunishmentData());
        this.addData(new PlayerServerData());
    }
}