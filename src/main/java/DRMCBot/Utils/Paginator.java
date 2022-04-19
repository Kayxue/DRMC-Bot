package DRMCBot.Utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Paginator extends ListenerAdapter {
    final List<EmbedBuilder> embeds;
    final int secondToReact;
    public static JDA jda;
    private final Message message;
    private final String FIRSTPAGE = "\u23ee\ufe0f";
    private final String PREVIOUSPAGE = "\u25c0\ufe0f";
    private final String STOP = "\u23f9\ufe0f";
    private final String NEXTPAGE = "\u25b6\ufe0f";
    private final String LASTPAGE = "\u23ed\ufe0f";
    AtomicInteger timeLeft;
    boolean run = true;
    int nowEmbed = 0;
    private Thread countdownThread;
    private Thread addReaction;

    public Paginator(List<EmbedBuilder> embeds, int secondToReact, MessageChannel channel) {
        this.embeds = embeds;
        this.secondToReact = secondToReact;
        this.timeLeft = new AtomicInteger(secondToReact);
        this.message = channel.sendMessageEmbeds(embeds.get(0).build()).complete();
    }

    public void start() {
        jda.addEventListener(this);
        countdownThread = new Thread(() -> {
            while (timeLeft.get() > 0 && run) {
                timeLeft.addAndGet(-1);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignore) {

                }
            }
            message.clearReactions().queue();
            jda.removeEventListener(this);
        });
        countdownThread.start();
        addReaction = new Thread(() -> {
            message.addReaction(FIRSTPAGE).queue();
            message.addReaction(PREVIOUSPAGE).queue();
            message.addReaction(STOP).queue();
            message.addReaction(NEXTPAGE).queue();
            message.addReaction(LASTPAGE).queue();
        });
        addReaction.start();
    }

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        if (message.getId().equals(event.getMessageId()) && !event.getUser().isBot()) {
            boolean changepage = false;
            timeLeft = new AtomicInteger(secondToReact);
            System.out.println(event.getReactionEmote().getName());
            switch (event.getReactionEmote().getName()) {
                case FIRSTPAGE -> {                                //⏮️
                    if (nowEmbed > 0) {
                        nowEmbed = 0;
                        changepage = true;
                    }
                    message.removeReaction(FIRSTPAGE, event.getUser()).queue();
                }
                case PREVIOUSPAGE -> {                                //◀️
                    if (nowEmbed > 0) {
                        nowEmbed -= 1;
                        changepage = true;
                    }
                    message.removeReaction(PREVIOUSPAGE, event.getUser()).queue();
                }
                case STOP -> {                                         //⏹️
                    addReaction.stop();
                    run = false;
                    countdownThread.interrupt();
                    return;
                }
                case NEXTPAGE -> {                                //▶️
                    if (nowEmbed < embeds.size() - 1) {
                        nowEmbed += 1;
                        changepage = true;
                    }
                    message.removeReaction(NEXTPAGE, event.getUser()).queue();
                }
                case LASTPAGE -> {                                //⏭️
                    if (nowEmbed < embeds.size() - 1) {
                        nowEmbed = embeds.size() - 1;
                        changepage = true;
                    }
                    message.removeReaction(LASTPAGE, event.getUser()).queue();
                }
            }
            if (changepage) {
                message.editMessageEmbeds(embeds.get(nowEmbed).build()).queue();
            }
        }
    }
}
