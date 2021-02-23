package DRMCBot.Command.Commands.music;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.lavaplayer.GuildMusicManager;
import DRMCBot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.List;

public class StopCommand implements ICommand {
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

        final GuildMusicManager manager=PlayerManager.getInstance().getMusicManager(ctx.getChannel());

        manager.scheduler.player.stopTrack();
        manager.scheduler.queue.clear();
        manager.scheduler.controller.removeController();

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
