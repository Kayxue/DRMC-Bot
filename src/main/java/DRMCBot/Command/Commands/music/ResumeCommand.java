package DRMCBot.Command.Commands.music;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.lavaplayer.GuildMusicManager;
import DRMCBot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class ResumeCommand implements ICommand {
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

        final GuildMusicManager manager= PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        final AudioPlayer audioPlayer = manager.audioPlayer;

        if (audioPlayer.getPlayingTrack() == null) {
            ctx.getChannel().sendMessage("目前沒有音樂正在播放！").queue();
            return;
        }
        manager.audioPlayer.setPaused(false);
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
