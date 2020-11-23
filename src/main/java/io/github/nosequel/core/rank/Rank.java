package io.github.nosequel.core.rank;

import io.github.nosequel.core.CorePlugin;
import io.github.nosequel.core.rank.data.GeneralRankData;
import io.github.nosequel.core.rank.sync.RankSynchronizationHandler;
import io.github.nosequel.core.util.data.Data;
import io.github.nosequel.core.util.data.Loadable;
import io.github.nosequel.core.util.json.JsonAppender;
import io.github.nosequel.core.util.synchronize.SynchronizeController;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Rank implements Loadable<Data> {

    private final RankController controller = CorePlugin.getInstance().getHandler().find(RankController.class);
    private final SynchronizeController synchronizeController = CorePlugin.getInstance().getHandler().find(SynchronizeController.class);

    private List<Data> data = new ArrayList<>();

    private GeneralRankData generalRankData;
    private UUID uuid;


    /**
     * Constructor for creating a new rank
     *
     * @param uuid the unique identifier of the rank
     * @param id   the string identifier of the rank
     */
    public Rank(UUID uuid, String id) {
        final GeneralRankData rankData = new GeneralRankData(id);

        this.uuid = uuid == null ? UUID.randomUUID() : uuid;
        this.generalRankData = rankData;
        this.addData(rankData);

        controller.register(this);
        controller.refreshPermissions(this);
    }

    /**
     * Constructor for creating a new Rank
     *
     * @param uuid the unique identifier of the rank
     */
    public Rank(UUID uuid, List<Data> data) {
        this.uuid = uuid;
        this.data = data;
        this.controller.register(this);
    }

    /**
     * Get a GeneralRankData object from the rank
     *
     * @return the data object
     */
    public GeneralRankData getGeneralRankData() {
        return this.generalRankData == null ? this.findData(GeneralRankData.class) : this.generalRankData;
    }

    /**
     * Get the weight of the rank object
     *
     * @return the weight
     */
    public int getWeight() {
        return this.getGeneralRankData().getWeight();
    }

    /**
     * Synchronize the rank with other servers
     */
    public void sync() {
        controller.getDatabaseController().getDataHandler().save(this, "ranks");
        synchronizeController.synchronize(RankSynchronizationHandler.class, new JsonAppender().append("uuid", this.getUuid().toString()).get());
    }
}