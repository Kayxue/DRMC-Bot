package DRMCBot;

import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class StarBoardListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        if (event.getUser().isBot()) {
            return;
        }
        if (event.getReactionEmote().getName().equals("\u2B50")) {

        }
    }
}
