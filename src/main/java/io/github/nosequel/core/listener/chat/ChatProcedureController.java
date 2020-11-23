package io.github.nosequel.core.listener.chat;

import io.github.nosequel.core.CorePlugin;
import io.github.nosequel.core.commands.grant.procedure.GrantDurationProcedure;
import io.github.nosequel.core.commands.grant.procedure.GrantReasonProcedure;
import io.github.nosequel.core.controller.Controller;
import io.github.nosequel.core.controller.ControllerPriority;
import io.github.nosequel.core.listener.chat.impl.MessageChatProcedure;
import io.github.nosequel.core.listener.chat.impl.MuteChatProcedure;
import io.github.nosequel.core.listener.chat.listener.ChatListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

import java.util.Arrays;
import java.util.List;

@Getter
public class ChatProcedureController implements Controller {

    private final ControllerPriority loadPriority = ControllerPriority.LOWEST;

    // the order of this is important
    private final List<ChatProcedure> chatProcedures = Arrays.asList(
            new GrantDurationProcedure(),
            new GrantReasonProcedure(),
            new MuteChatProcedure(),
            new MessageChatProcedure()
    );

    @EventHandler
    public void load() {
        Bukkit.getPluginManager().registerEvents(new ChatListener(), CorePlugin.getInstance());
    }
}