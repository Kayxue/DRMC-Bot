package DRMCBot.Command.Commands.music;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.Command.music.GuildMusicManager;
import DRMCBot.Command.music.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;

public class PauseCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        PlayerManager playerManager=PlayerManager.getInstance();
        GuildMusicManager musicManager=playerManager.getGuildMusicManager(ctx.getGuild());

        musicManager.player.setPaused(true);
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
