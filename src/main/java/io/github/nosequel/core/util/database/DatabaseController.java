package io.github.nosequel.core.util.database;

import io.github.nosequel.core.controller.Controller;
import io.github.nosequel.core.controller.ControllerPriority;
import io.github.nosequel.core.util.database.handler.DataHandler;
import io.github.nosequel.core.util.database.options.DatabaseOption;
import io.github.nosequel.core.util.database.type.DataType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatabaseController implements Controller {

    private final DataType<?, ?> type;
    private final DatabaseOption option;

    private DataHandler dataHandler;

    private ControllerPriority loadPriority = ControllerPriority.HIGH;
    private ControllerPriority unloadPriority = ControllerPriority.LOW;

    /**
     * Constructor for creating a new DatabaseController
     *
     * @param option      the options of the database
     * @param type        the type of the data
     */
    public DatabaseController(DatabaseOption option, DataType<?, ?> type) {
        this.type = type;
        this.option = option;
    }
}