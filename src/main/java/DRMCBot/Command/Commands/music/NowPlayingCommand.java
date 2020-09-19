package DRMCBot.Command.Commands.music;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.Command.music.GuildMusicManager;
import DRMCBot.Command.music.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NowPlayingCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel=ctx.getChannel();
        PlayerManager playerManager=PlayerManager.getInstance();
        GuildMusicManager musicManager=playerManager.getGuildMusicManager(ctx.getGuild());
        AudioPlayer player=musicManager.player;

        if (player.getPlayingTrack()==null){
            channel.sendMessage("沒在播放任何東西").queue();
            return;
        }

        AudioTrackInfo info=player.getPlayingTrack().getInfo();

        channel.sendMessage(EmbedUtils.embedMessage(String.format(
                "**正在播放：** [%s] (%s)\n%s %s － %s",
                info.title,
                info.uri,
                player.isPaused()?"\u23F8":"▶",
                formatTime(player.getPlayingTrack().getPosition()),
                formatTime(player.getPlayingTrack().getDuration())
        )).build()).queue();
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("np");
    }

    @Override
    public String getName() {
        return "nowplaying";
    }

    private String formatTime(long timeInMillis){
        final long hours=timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes=timeInMillis/TimeUnit.MINUTES.toMillis(1);
        final long seconds=timeInMillis%TimeUnit.MINUTES.toMillis(1)/TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d",hours,minutes,seconds);
    }
}
