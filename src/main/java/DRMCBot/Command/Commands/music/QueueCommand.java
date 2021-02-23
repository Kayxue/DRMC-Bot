package DRMCBot.Command.Commands.music;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.lavaplayer.GuildMusicManager;
import DRMCBot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class QueueCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel=ctx.getChannel();
        final PlayerManager playerManager=PlayerManager.getInstance();
        final GuildMusicManager musicManager=playerManager.getMusicManager(ctx.getChannel());
        BlockingQueue<AudioTrack> queue=musicManager.scheduler.queue;

        if (queue.isEmpty()){
            channel.sendMessage("The queue is empty").queue();
            return;
        }

        int trackCount=Math.min(queue.size(),20);
        List<AudioTrack> tracks=new ArrayList<>(queue);
        EmbedBuilder builder= EmbedUtils.getDefaultEmbed()
                .setTitle("Current Queue (Total: "+queue.size()+")");

        for (int i=0;i<trackCount;i++){
            AudioTrack track=tracks.get(i);
            AudioTrackInfo info = track.getInfo();
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
    public List<String> getUsages() {
        return null;
    }

    @Override
    public List<String> getExamples() {
        return null;
    }

    @Override
    public HashMap<String, HashMap<String, String>> getArguments() {
        return null;
    }
}
