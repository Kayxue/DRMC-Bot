package DRMCBot.Command.Commands.music;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.Command.music.GuildMusicManager;
import DRMCBot.Command.music.PlayerManager;
import DRMCBot.Command.music.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.TextChannel;

public class SkipCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel=ctx.getChannel();
        PlayerManager playerManager=PlayerManager.getInstance();
        GuildMusicManager musicManager=playerManager.getGuildMusicManager(ctx.getGuild());
        TrackScheduler scheduler=musicManager.scheduler;
        AudioPlayer player=musicManager.player;

        if(player.getPlayingTrack()==null){
            channel.sendMessage("The player isn't playing anything").queue();

            return;
        }

        scheduler.nextTrack();

        channel.sendMessage("Skipping the current track").queue();
    }

    @Override
    public String getName() {
        return "skip";
    }
}
