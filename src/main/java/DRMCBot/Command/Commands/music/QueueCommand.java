package DRMCBot.Command.Commands.music;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.Command.music.GuildMusicManager;
import DRMCBot.Command.music.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class QueueCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel=ctx.getChannel();
        PlayerManager playerManager=PlayerManager.getInstance();
        GuildMusicManager musicManager=playerManager.getGuildMusicManager(ctx.getGuild());
        BlockingQueue<AudioTrack> queue=musicManager.scheduler.getQueue();

        if (queue.isEmpty()){
            channel.sendMessage("The queue is empty").queue();

            return;
        }

        int trackCount=Math.min(queue.size(),20);
        List<AudioTrack> tracks=new ArrayList<>(queue);
        EmbedBuilder builder= EmbedUtils.defaultEmbed()
                .setTitle("Current Queue (Total: "+queue.size()+")");

        for (int i=0;i<trackCount;i++){
            AudioTrack track=tracks.get(i);
            AudioTrackInfo info=track.getInfo();

            builder.appendDescription(String.format(
                    "%s - %s \n",
                    info.title,
                    info.author
            ));
        }

        channel.sendMessage(builder.build()).queue();
    }

    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public String getCategory() {
        return "music";
    }

    @Override
    public String getdescription() {
        return null;
    }

    @Override
    public EmbedBuilder gethelpembed() {
        return null;
    }
}
