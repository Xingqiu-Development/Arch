package io.github.nosequel.core;

import io.github.nosequel.core.commands.cosmetic.ColorCommands;
import io.github.nosequel.core.commands.staff.StaffChatCommand;
import io.github.nosequel.core.commands.user.InsertUserCommand;
import io.github.nosequel.core.commands.ListCommand;
import io.github.nosequel.core.commands.grant.GrantCommand;
import io.github.nosequel.core.commands.RankCommand;
import io.github.nosequel.core.commands.info.InfoCommand;
import io.github.nosequel.core.commands.punishment.BanCommand;
import io.github.nosequel.core.commands.punishment.HistoryCommand;
import io.github.nosequel.core.commands.punishment.MuteCommand;
import io.github.nosequel.core.commands.user.ResetUserCommand;
import io.github.nosequel.core.controller.ControllerHandler;
import io.github.nosequel.core.player.grant.GrantController;
import io.github.nosequel.core.listener.PlayerListener;
import io.github.nosequel.core.listener.chat.ChatProcedureController;
import io.github.nosequel.core.player.CorePlayerController;
import io.github.nosequel.core.rank.RankController;
import io.github.nosequel.core.util.command.CommandController;
import io.github.nosequel.core.util.database.DatabaseController;
import io.github.nosequel.core.util.database.handler.data.MongoDataHandler;
import io.github.nosequel.core.util.database.options.impl.MongoDatabaseOption;
import io.github.nosequel.core.util.database.type.mongo.MongoDataType;
import io.github.nosequel.core.util.synchronize.SynchronizeController;
import io.github.nosequel.core.util.synchronize.options.RedisDatabaseOption;
import io.github.nosequel.core.util.synchronize.type.impl.JedisSynchronizationType;
import io.github.nosequel.katakuna.MenuHandler;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class CorePlugin extends JavaPlugin {

    @Getter
    private static CorePlugin instance;

    private final ControllerHandler handler = new ControllerHandler();

    @Override
    public void onEnable() {
        instance = JavaPlugin.getPlugin(CorePlugin.class);

        this.registerHandlers();
        this.handler.load();

        // register listeners
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);

    }

    @Override
    public void onDisable() {
        this.handler.unload();
    }

    /**
     * Method for registering {@link io.github.nosequel.core.controller.Controller}s
     */
    private void registerHandlers() {
        this.handler.register(new RankController());
        this.handler.register(new GrantController());
        this.handler.register(new CorePlayerController());

        this.handler.register(new CommandController("arch").registerCommand(
                new RankCommand(),
                new GrantCommand(),
                new ListCommand(),
                new InfoCommand(),
                new BanCommand(),
                new MuteCommand(),
                new HistoryCommand(),
                new InsertUserCommand(),
                new ResetUserCommand(),
                new StaffChatCommand(),
                new ColorCommands()
                )
        );

        // setup database controller shit
        final MongoDatabaseOption option = new MongoDatabaseOption(
                "127.0.0.1",
                "",
                "",
                "arch",
                27017
        );

        final RedisDatabaseOption syncOption = new RedisDatabaseOption(
                "127.0.0.1",
                "",
                "",
                6379
        );

        final DatabaseController databaseController = new DatabaseController(option, new MongoDataType());

        databaseController.setDataHandler(new MongoDataHandler(databaseController));

        this.handler.register(databaseController);
        this.handler.register(new SynchronizeController(new JedisSynchronizationType(), syncOption));
        this.handler.register(new ChatProcedureController());

        new MenuHandler(this);
    }
}