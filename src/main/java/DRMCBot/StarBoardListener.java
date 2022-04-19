package DRMCBot;


import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import org.jetbrains.annotations.NotNull;

public class StarBoardListener extends ListenerAdapter {
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        if (event.getUser().isBot()||!event.isFromGuild()) {
            return;
        }

        if (event.getReactionEmote().getName().equals("\u2B50")) {

        }
    }
}
