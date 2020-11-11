package DRMCBot.Command.Commands.music;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.Command.music.GuildMusicManager;
import DRMCBot.Command.music.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;

public class ResumeCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        PlayerManager playerManager=PlayerManager.getInstance();
        GuildMusicManager musicManager=playerManager.getGuildMusicManager(ctx.getGuild());

        musicManager.player.setPaused(false);
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
