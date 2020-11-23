package io.github.nosequel.core.util;

import com.google.gson.JsonObject;
import io.github.nosequel.core.util.json.JsonAppender;
import io.github.nosequel.core.util.json.JsonSerializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Expirable extends JsonSerializable {

    private final long startEpoch;

    private long expirationEpoch;
    private boolean active;

    /**
     * Constructor for making a new instance of a Expirable
     *
     * @param startEpoch      the epoch when the expirable object was created
     * @param expirationEpoch the epoch when the expirable object will be expired
     * @param active          whether the expirable object is active
     */
    public Expirable(long startEpoch, long expirationEpoch, boolean active) {
        super(null);

        this.startEpoch = startEpoch;
        this.expirationEpoch = expirationEpoch;
        this.active = active;
    }

    /**
     * Constructor for making a new instance of a Expirable
     *
     * @param startEpoch the epoch when the expirable object was created
     */
    public Expirable(long startEpoch) {
        super(null);

        this.startEpoch = startEpoch;
        this.expirationEpoch = -1L;
        this.active = true;
    }

    /**
     * Constructor for deserializing an Expirable object from a {@link JsonObject}
     *
     * @param object the json object
     */
    public Expirable(JsonObject object) {
        super(object);

        this.startEpoch = object.get("startEpoch").getAsLong();
        this.expirationEpoch = object.get("expirationEpoch").getAsLong();
        this.active = object.get("active").getAsBoolean();
    }

    /**
     * Check if the punishment is active
     *
     * @return whether the punishment is active or not
     */
    public boolean isActive() {
        if(this.getExpirationEpoch() != -1L && this.getExpirationEpoch() - System.currentTimeMillis() <= 0) {
            this.setActive(false);
        }

        return this.active;
    }

    public void setActive(boolean active) {
        if(!active) {
            this.setExpirationEpoch(System.currentTimeMillis());
        }

        this.active = active;
    }

    @Override
    public JsonObject toJsonObject() {
        return new JsonAppender()
                .append("startEpoch", this.startEpoch)
                .append("expirationEpoch", this.expirationEpoch)
                .append("active", this.active).get();
    }

}