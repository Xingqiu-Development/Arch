package io.github.nosequel.core.util.synchronize;

import com.google.gson.JsonObject;
import io.github.nosequel.core.commands.sync.MessageSynchronizationHandler;
import io.github.nosequel.core.controller.Controller;
import io.github.nosequel.core.controller.ControllerPriority;
import io.github.nosequel.core.player.sync.CorePlayerSynchronizationHandler;
import io.github.nosequel.core.rank.sync.RankSynchronizationHandler;
import io.github.nosequel.core.util.database.options.DatabaseOption;
import io.github.nosequel.core.util.synchronize.type.SynchronizationType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class SynchronizeController implements Controller {

    private final List<? extends SynchronizationHandler> handlers = new ArrayList<>(Arrays.asList(
            new CorePlayerSynchronizationHandler(),
            new RankSynchronizationHandler(),
            new MessageSynchronizationHandler()
    ));

    private final SynchronizationType type;
    private final DatabaseOption option;
    private final ControllerPriority loadPriority = ControllerPriority.LOWEST;

    /**
     * Constructor for registering a new SynchronizeController
     *
     * @param type   the synchronization type
     * @param option the settings the database uses
     */
    public SynchronizeController(SynchronizationType type, DatabaseOption option) {
        this.type = type;
        this.option = option;
    }

    @Override
    public void load() {
        this.type.setup();
    }

    /**
     * Method for synchronizing a JsonObject through a channel
     *
     * @param handlerClass the class to handle it through
     * @param object       the object to synchronize
     */
    public void synchronize(Class<? extends SynchronizationHandler> handlerClass, JsonObject object) {
        final SynchronizationHandler handler = this.find(handlerClass);

        if (handler == null) {
            throw new IllegalArgumentException("No synchronization handler found with class " + handlerClass.getName());
        }

        object.addProperty("channel", handler.getType());
        this.type.publish(object);
    }

    /**
     * Method for finding a {@link SynchronizationHandler} object with a {@link Class}
     *
     * @param clazz the class
     * @param <T>   the type of the handler
     * @return the handler
     */
    private <T extends SynchronizationHandler> T find(Class<T> clazz) {
        return clazz.cast(this.handlers.stream()
                .filter(handler -> handler.getClass().equals(clazz))
                .findFirst().orElse(null));
    }
}