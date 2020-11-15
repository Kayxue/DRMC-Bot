package DRMCBot.Command.Commands.music;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.lavaplayer.GuildMusicManager;
import DRMCBot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;

public class ResumeCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final PlayerManager playerManager=PlayerManager.getInstance();
        final GuildMusicManager musicManager=playerManager.getMusicManager(ctx.getGuild());

        musicManager.audioPlayer.setPaused(false);
        ctx.getChannel().sendMessage("歌曲已繼續播放").queue();
    }

    @Override
    public String getName() {
        return "resume";
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
