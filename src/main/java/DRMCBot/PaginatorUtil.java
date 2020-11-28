package DRMCBot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PaginatorUtil extends ListenerAdapter {
    final List<EmbedBuilder> embeds;
    final int secondToReact;
    int timeLeft;
    static JDA jda;
    final Message message;
    final String FIRSTPAGE = "\u23ee\ufe0f";
    final String PREVIOUSPAGE = "\u25c0\ufe0f";
    final String STOP = "\u23f9\ufe0f";
    final String NEXTPAGE = "\u25b6\ufe0f";
    final String LASTPAGE = "\u23ed\ufe0f";
    boolean run = true;
    int nowEmbed = 0;
    Thread countdownThread;
    Thread addReaction;

    public PaginatorUtil(List<EmbedBuilder> embeds, int secondToReact, TextChannel channel) {
        this.embeds = embeds;
        this.secondToReact = secondToReact;
        this.timeLeft = secondToReact;
        this.message = channel.sendMessage(embeds.get(0).build()).complete();
        jda.addEventListener(this);
        countdownThread = new Thread(() -> {
            while (timeLeft > 0 && run) {
                timeLeft -= 1;
                System.out.println(timeLeft);
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
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        if (message.getId().equals(event.getMessageId()) && !event.getUser().isBot()) {
            boolean changepage = false;
            timeLeft = secondToReact;
            System.out.println(event.getReactionEmote().getName());
            switch (event.getReactionEmote().getName()) {
                case FIRSTPAGE -> {                                //⏮️
                    if (nowEmbed > 0) {
                        nowEmbed = 0;
                        changepage = true;
                        System.out.println("葉面已更改！");
                    }
                    message.removeReaction(FIRSTPAGE, event.getUser()).queue();
                }
                case PREVIOUSPAGE -> {                                //◀️
                    if (nowEmbed > 0) {
                        nowEmbed -= 1;
                        changepage = true;
                        System.out.println("葉面已更改！");
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
                        System.out.println("葉面已更改！");
                    }
                    message.removeReaction(NEXTPAGE, event.getUser()).queue();
                }
                case LASTPAGE -> {                                //⏭️
                    if (nowEmbed < embeds.size() - 1) {
                        nowEmbed = embeds.size() - 1;
                        changepage = true;
                        System.out.println("葉面已更改！");
                    }
                    message.removeReaction(LASTPAGE, event.getUser()).queue();
                }
            }
            if (changepage) {
                message.editMessage(embeds.get(nowEmbed).build()).queue();
            }
        }
    }
}
