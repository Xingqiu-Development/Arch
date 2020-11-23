package io.github.nosequel.core.player.punishments;

import com.google.gson.JsonObject;
import io.github.nosequel.core.util.Expirable;
import io.github.nosequel.core.util.json.JsonAppender;
import io.github.nosequel.core.util.json.JsonSerializable;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Punishment extends Expirable {

    private final UUID uuid;
    private final PunishmentType type;
    private final String reason;
    private final String executor;

    /**
     * Constructor for creating a new punishment
     *
     * @param uuid   the uuid of the punishment
     * @param type   the type of the punishment
     * @param reason the reason why the punishment had been executed
     */
    public Punishment(UUID uuid, PunishmentType type, String reason, String executor) {
        super(System.currentTimeMillis());

        this.uuid = uuid == null ? UUID.randomUUID() : uuid;
        this.type = type;
        this.reason = reason;
        this.executor = executor;
    }

    /**
     * Constructor for creating a new punishment with a set expiration date
     *
     * @param uuid            the uuid of the punishment
     * @param type            the type of the punishment
     * @param expirationEpoch the expiration date of the punishment
     * @param reason          the reason why the punishment had been executed
     */
    public Punishment(UUID uuid, PunishmentType type, long expirationEpoch, String reason, String executor) {
        this(uuid, type, reason, executor);

        this.setExpirationEpoch((expirationEpoch == -1L ? -1L : this.getStartEpoch() + expirationEpoch));
    }

    /**
     * Constructor for loading a Punishment from a {@link JsonObject}
     *
     * @param object the json object to load it from
     */
    public Punishment(JsonObject object) {
        super(object);

        this.uuid = UUID.fromString(object.get("uuid").getAsString());
        this.type = PunishmentType.valueOf(object.get("type").getAsString());
        this.reason = object.get("reason").getAsString();
        this.executor = object.get("executor").getAsString();
    }

    @Override
    public JsonObject toJsonObject() {
        return new JsonAppender(super.toJsonObject())
                .append("uuid", this.uuid.toString())
                .append("type", this.type.name())
                .append("reason", this.reason)
                .append("executor", this.executor).get();
    }
}