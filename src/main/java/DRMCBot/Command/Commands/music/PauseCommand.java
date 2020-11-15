package DRMCBot.Command.Commands.music;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;

import DRMCBot.lavaplayer.GuildMusicManager;
import DRMCBot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;

public class PauseCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final PlayerManager playerManager=PlayerManager.getInstance();
        final GuildMusicManager musicManager=playerManager.getMusicManager(ctx.getGuild());

        musicManager.audioPlayer.setPaused(true);
        ctx.getChannel().sendMessage("歌曲已暫停").queue();
    }

    @Override
    public String getName() {
        return "pause";
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
