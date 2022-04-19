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

public class SetVolumeCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {
        final List<String> args = ctx.getArgs();
        final TextChannel channel=(TextChannel) ctx.getChannel();
        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();
        int volume;

        if (args.isEmpty()) {
            ctx.getChannel().sendMessage("請輸入參數！").queue();
            return;
        }

        String volumeString = args.get(0);
        if (volumeString.contains("%")) {
            volumeString = volumeString.substring(0, volumeString.indexOf("%"));
        }

        try {
            volume = Integer.parseInt(volumeString);
        } catch (Exception e) {
            ctx.getChannel().sendMessage("請輸入數字！").queue();
            return;
        }

        if (volume > 100 || volume < 0) {
            ctx.getChannel().sendMessage("音量數值必須在0~100之間！").queue();
            return;
        }

        if (!selfVoiceState.inAudioChannel()) {
            ctx.getChannel().sendMessage("我不在語音頻道內！").queue();
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inAudioChannel()){
            channel.sendMessage("你必須加入一個語音頻道！").queue();
            return;
        }


        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            ctx.getChannel().sendMessage("您跟我在不同頻道！").queue();
            return;
        }

        final GuildMusicManager manager= PlayerManager.getInstance().getMusicManager((TextChannel) ctx.getChannel());
        manager.audioPlayer.setVolume(volume);
        ctx.getChannel().sendMessage("音量已設定成" + volume + "%").queue();
    }

    @Override
    public String getName() {
        return "volume";
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

    @Override
    public List<String> getAliases() {
        return List.of("setvolume");
    }
}
