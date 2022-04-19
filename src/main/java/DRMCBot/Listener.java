package DRMCBot;

import DRMCBot.Database.DatabaseManager;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Listener extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    ScheduledExecutorService executor;
    private final CommandManagerV3 manager;
    //private final CommandManagerV2 manager;
    //private final CommandManager manager;

    public Listener(EventWaiter waiter) {
        manager = new CommandManagerV3(waiter);
        //manager = new CommandManagerV2(waiter);
        //manager = new CommandManager(waiter);
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        LOGGER.info(event.getJDA().getSelfUser().getAsTag()+" is ready");

        manager.setJda(event.getJDA());

        executor = Executors.newScheduledThreadPool(1);

        Runnable task = () -> {
            event.getJDA().getPresence().setActivity(Activity.watching("Welcome to New DL/RS/MC Chatroom"));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignore) {

            }
            event.getJDA().getPresence().setActivity(Activity.watching(event.getJDA().getGuilds().size() + "個伺服器")); //How many guilds the bot joined
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignore) {

            }
            event.getJDA().getPresence().setActivity(Activity.watching(event.getJDA().getUsers().size() + "位使用者"));//How many user the bot can see
        };

        executor.scheduleWithFixedDelay(task, 0, 5, TimeUnit.SECONDS);

    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        User user = event.getAuthor();

        if (user.isBot() || event.isWebhookMessage() || !event.isFromGuild()){
            return;
        }

        final long guildID = event.getGuild().getIdLong();
        String prefix = VeryBadDesign.PREFIXES.computeIfAbsent(guildID, DatabaseManager.INSTANCE::getPrefix);
        String raw = event.getMessage().getContentRaw();

        if (raw.equalsIgnoreCase(prefix + "shutdown")) {
            LOGGER.info("Shutting Down");
            BotCommons.shutdown(event.getJDA());
            System.exit(0);
        }
        if (raw.startsWith(prefix)){
            try {
                manager.handle(event, prefix);
            } catch (Exception e) {
                e.printStackTrace();
                event.getChannel().sendMessage(e.getLocalizedMessage()).queue();
            }
        }
    }
}
