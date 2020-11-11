package DRMCBot.Command.Commands.music;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.Command.music.GuildMusicManager;
import DRMCBot.Command.music.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;

public class StopCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        PlayerManager playerManager=PlayerManager.getInstance();
        GuildMusicManager musicManager=playerManager.getGuildMusicManager(ctx.getGuild());

        musicManager.scheduler.getQueue().clear();
        musicManager.player.stopTrack();
        musicManager.player.setPaused(false);

        ctx.getChannel().sendMessage("已停止播放並清空音樂列表").queue();
    }

    @Override
    public String getName() {
        return "stop";
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
