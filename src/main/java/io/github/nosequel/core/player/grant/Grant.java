package io.github.nosequel.core.player.grant;

import com.google.gson.JsonObject;
import io.github.nosequel.core.CorePlugin;
import io.github.nosequel.core.rank.Rank;
import io.github.nosequel.core.rank.RankController;
import io.github.nosequel.core.util.Expirable;
import io.github.nosequel.core.util.json.JsonAppender;
import io.github.nosequel.core.util.json.JsonSerializable;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Grant extends Expirable {

    private final UUID uuid;
    private final Rank rank;

    private final String executor;
    private final String reason;

    // controller fields
    private final RankController rankController = CorePlugin.getInstance().getHandler().find(RankController.class);

    /**
     * Constructor for creating a new Grant object with all specified parameters
     *
     * @param uuid                the unique identifier of the grant
     * @param rank                the rank the grant will be granting to the player
     * @param executor            the executor of the grant
     * @param reason              the specified reason why the grant has been given
     * @param expirationDateEpoch the date the grant expires
     */
    public Grant(UUID uuid, Rank rank, String executor, String reason, long expirationDateEpoch) {
        super(System.currentTimeMillis(), expirationDateEpoch == -1L ? -1L : System.currentTimeMillis() + expirationDateEpoch, true);

        this.uuid = uuid == null ? UUID.randomUUID() : uuid;
        this.rank = rank;
        this.executor = executor;
        this.reason = reason;
    }

    /**
     * Constructor for creating a new Grant object with default values
     *
     * @param uuid     the unique identifier of the grant
     * @param rank     the rank the grant will be granting to the player
     * @param executor the executor of the grant
     */
    public Grant(UUID uuid, Rank rank, String executor) {
        this(uuid, rank, executor, "Not specified", -1L);
    }

    /**
     * Constructor for loading a grant from a {@link JsonObject}
     *
     * @param object the object to load the grant from
     */
    public Grant(JsonObject object) {
        super(object);

        this.rank = rankController.findRank(UUID.fromString(object.get("rank").getAsString()));

        if (rank == null) {
            throw new IllegalArgumentException("Rank with UUID " + object.get("rank") + " does not exist.");
        }

        this.uuid = UUID.fromString(object.get("uuid").getAsString());
        this.executor = object.get("executor").getAsString();
        this.reason = object.get("reason").getAsString();
    }

    @Override
    public JsonObject toJsonObject() {
        return new JsonAppender(super.toJsonObject())
                .append("uuid", uuid.toString())
                .append("rank", rank.getUuid().toString())
                .append("reason", reason)
                .append("reason", this.reason)
                .append("executor", this.executor).get();
    }
}