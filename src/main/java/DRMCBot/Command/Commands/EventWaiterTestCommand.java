package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

public class EventWaiterTestCommand implements ICommand {

    private EventWaiter waiter;
    private static final String EMOTE = "\uD83C\uDD95";

    public EventWaiterTestCommand(EventWaiter waiter) {
        this.waiter = waiter;
    }

    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel= ctx.getChannel();
        long channelid = channel.getIdLong();

        channel.sendMessage("Please react with "+EMOTE).queue(
                message -> {
                    message.addReaction(EMOTE).queue();
                    initWaiter(message.getIdLong(), ctx.getChannel());
                }
        );
    }

    private void initWaiter(long messageid, TextChannel channel) {
        waiter.waitForEvent(
                GuildMessageReactionAddEvent.class,
                event -> {
                    MessageReaction.ReactionEmote emote = event.getReactionEmote();
                    User user = event.getUser();

                    return !user.isBot() && event.getMessageIdLong() == messageid && !emote.isEmote() && EMOTE.equals(emote.getName());
                },
                event -> {
                    User user = event.getUser();

                    channel.sendMessage(user.getAsMention() + "表情已附加").queue();
                },
                10, TimeUnit.SECONDS,
                () -> {
                    channel.sendMessage("10秒超過！").queue();
                }
        );
    }

    @Override
    public String getName() {
        return "eventwaitertest";
    }
}
