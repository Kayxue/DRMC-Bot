package DRMCBot.Command.Commands.music;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;

import DRMCBot.lavaplayer.GuildMusicManager;
import DRMCBot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.List;

public class PauseCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel=ctx.getChannel();
        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if (!selfVoiceState.inVoiceChannel()) {
            ctx.getChannel().sendMessage("我不在語音頻道內！").queue();
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()){
            channel.sendMessage("你必須加入一個語音頻道！").queue();
            return;
        }


        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            ctx.getChannel().sendMessage("您跟我在不同頻道！").queue();
            return;
        }

        final GuildMusicManager manager= PlayerManager.getInstance().getMusicManager(ctx.getChannel());
        final AudioPlayer audioPlayer = manager.audioPlayer;

        if (audioPlayer.getPlayingTrack() == null) {
            ctx.getChannel().sendMessage("目前沒有音樂正在播放！").queue();
            return;
        }
        if (manager.audioPlayer.isPaused()) {
            ctx.getChannel().sendMessage("目前已經為為暫停狀態！").queue();
            return;
        }
        manager.audioPlayer.setPaused(true);
        manager.scheduler.controller.editPausing(true);
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
