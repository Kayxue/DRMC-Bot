package DRMCBot;

import DRMCBot.Database.DatabaseManager;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class Listener extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    ScheduledExecutorService executor;
    //private final CommandManagerV3 manager;
    private final CommandManagerV2 manager;
    //private final CommandManager manager;

    public Listener(EventWaiter waiter) {
        //manager = new CommandManagerV3(waiter);
        manager = new CommandManagerV2(waiter);
        //manager = new CommandManager(waiter);
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        LOGGER.info(event.getJDA().getSelfUser().getAsTag()+" is ready");

        executor = Executors.newScheduledThreadPool(2);

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
        TextChannel channel = event.getJDA().getTextChannelById("772839257819447306");

        ZonedDateTime anniversary = ZonedDateTime.of(2020, 12, 30, 0, 0, 0, 0, ZoneId.of("Asia/Taipei"));
        Runnable countdown = () -> {
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Taipei"));
            double hour = Math.ceil(Duration.between(now, anniversary).getSeconds() / 3600.0);
            if (hour > 0) {
                long day = (long) Math.ceil(Duration.between(now, anniversary).getSeconds() / 86400.0);
                channel.getManager().setName("距離伺服兩週年：" + day + "天").queue();
            } else {
                channel.getManager().setName("\uD83C\uDF89伺服器兩週年快樂！").queue();
                channel.sendMessage(":tada:伺服器兩週年快樂！").queue();
                executor.shutdown();
            }
        };

        executor.scheduleWithFixedDelay(task, 0, 5, TimeUnit.SECONDS);
        executor.scheduleWithFixedDelay(countdown, 0, 5, TimeUnit.SECONDS);

    }

    @Override
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
        super.onGuildMemberJoin(event);
    }

    @Override
    public void onGuildMemberRemove(@Nonnull GuildMemberRemoveEvent event) {
        super.onGuildMemberRemove(event);
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        User user = event.getAuthor();

        if (user.isBot() || event.isWebhookMessage()){
            return;
        }

        final long guildID = event.getGuild().getIdLong();
        String prefix = VeryBadDesign.PREFIXES.computeIfAbsent(guildID, DatabaseManager.INSTANCE::getPrefix);
        String raw = event.getMessage().getContentRaw();

        if (raw.equalsIgnoreCase(prefix + "shutdown")) {
            LOGGER.info("Shutting Down");
            BotCommons.shutdown(event.getJDA());
            System.exit(0);
            return;
        }
        if (raw.startsWith(prefix)){
            try {
                manager.handle(event, prefix);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
