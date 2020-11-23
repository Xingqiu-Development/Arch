package io.github.nosequel.core.rank;

import io.github.nosequel.core.CorePlugin;
import io.github.nosequel.core.controller.Controller;

import io.github.nosequel.core.player.grant.GrantController;
import io.github.nosequel.core.player.CorePlayer;
import io.github.nosequel.core.player.CorePlayerController;
import io.github.nosequel.core.player.data.PlayerGrantData;
import io.github.nosequel.core.rank.data.GeneralRankData;
import io.github.nosequel.core.util.SortableArrayList;
import io.github.nosequel.core.util.data.Data;
import io.github.nosequel.core.util.data.DataController;
import io.github.nosequel.core.util.database.DatabaseController;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.*;

@Getter
public class RankController implements DataController<Rank, Data>, Controller {

    private GrantController grantController;
    private CorePlayerController playerController;
    private DatabaseController databaseController;

    private SortableArrayList<Rank> ranks = new SortableArrayList<>(Comparator.comparingInt(Rank::getWeight).reversed());
    private final List<? extends Data> registeredData = new ArrayList<>(Collections.singletonList(new GeneralRankData()));

    @Override
    @SneakyThrows
    public void load() {
        this.databaseController = CorePlugin.getInstance().getHandler().find(DatabaseController.class);
        this.playerController = CorePlugin.getInstance().getHandler().find(CorePlayerController.class);
        this.grantController = CorePlugin.getInstance().getHandler().find(GrantController.class);

        this.databaseController.getDataHandler().loadAll(this, "ranks", Rank.class);

        if (this.ranks.stream().noneMatch(rank -> rank.getGeneralRankData().isDef())) {
            new Rank(UUID.fromString("94f981ac-068c-4ec6-a75a-3fb03570fa3a"), "Default").getGeneralRankData().setDefault(true);
        }
    }


    @Override
    public void unload() {
        this.ranks.forEach(rank -> databaseController.getDataHandler().save(rank, "ranks"));
    }

    /**
     * Register a new rank
     *
     * @param rank the rank
     */
    public void register(Rank rank) {
        this.ranks.add(rank);

        if (rank.getGeneralRankData() != null && rank.getGeneralRankData().isDef()) {
            grantController.registerDefaultGrant(rank);
        }
    }

    /**
     * Unregister an existing rank
     *
     * @param rank the rank
     */
    public void unregister(Rank rank) {
        this.ranks.remove(rank);
        this.databaseController.getDataHandler().delete(rank, "ranks");

        if (rank.getGeneralRankData().isDef()) {
            grantController.refreshGrants();
        }
    }

    /**
     * Refresh permissions of a rank
     *
     * @param rank the rank
     */
    public void refreshPermissions(Rank rank) {
        playerController.getPlayers().stream()
                .filter(player -> player.findData(PlayerGrantData.class).getGrants().stream().anyMatch(grant -> grant.getRank().equals(rank)))
                .forEach(CorePlayer::reloadPermissions);
    }

    /**
     * Find a rank by their corresponding identifier
     *
     * @param id the identifier
     * @return the found rank
     */
    public Rank findRank(String id) {
        return this.ranks.stream()
                .filter(rank -> rank.getGeneralRankData().getId().equals(id))
                .findFirst().orElse(null);
    }

    /**
     * Find a rank by their corresponding unique identifier
     *
     * @param uuid the unique identifier
     * @return the found rank
     */
    public Rank findRank(UUID uuid) {
        return this.ranks.stream()
                .filter(rank -> rank.getUuid().equals(uuid))
                .findFirst().orElse(null);
    }

    /**
     * Sort the ranks
     */
    public void sort() {
        final SortableArrayList<Rank> list = this.ranks;

        this.ranks = new SortableArrayList<>(this.ranks.getComparator());
        this.ranks.addAll(list);
    }

    /**
     * Reload a rank from the database
     *
     * @param rank the rank to reload
     */
    public void reload(Rank rank) {
        this.ranks.remove(rank);
        this.grantController.getDefaultGrants().removeIf(grant -> grant.getRank().equals(rank));
        this.databaseController.getDataHandler().load(this, Rank.class, rank.getUuid(), "ranks");
    }
}