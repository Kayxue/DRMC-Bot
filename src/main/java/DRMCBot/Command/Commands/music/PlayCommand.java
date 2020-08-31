package DRMCBot.Command.Commands.music;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.Command.music.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class PlayCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel=ctx.getChannel();
        List<String> args=ctx.getArgs();
        GuildVoiceState memberVoiceState = ctx.getMember().getVoiceState();
        GuildVoiceState selfVoiceState = ctx.getSelfMember().getVoiceState();

        if (args.isEmpty()){
            channel.sendMessage("請輸入參數！").queue();
            return;
        }

        if (!selfVoiceState.inVoiceChannel()) {
            if (memberVoiceState.inVoiceChannel()){
                final AudioManager audioManager = ctx.getGuild().getAudioManager();
                final VoiceChannel memberchannel = memberVoiceState.getChannel();

                audioManager.openAudioConnection(memberchannel);
                channel.sendMessageFormat("連線至 `\uD83D\udd0a %s`",memberchannel.getName()).queue();
                memberVoiceState = ctx.getMember().getVoiceState();
                selfVoiceState = ctx.getSelfMember().getVoiceState();
            }
        }

        if (!memberVoiceState.inVoiceChannel()){
            channel.sendMessage("你必須加入一個語音頻道！").queue();
            return;
        }

        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            ctx.getChannel().sendMessage("您跟我在不同頻道！").queue();
            return;
        }

        String input=String.join(" ",args);

        if (!isUrl(input)&&!input.startsWith("ytsearch:")){
            channel.sendMessage("請輸入有效的Youtube、soundcloud或bandcamp連結").queue();

            return;
        }



        PlayerManager manager=PlayerManager.getInstance();

        manager.loadAndPlay(ctx.getChannel(),input);
    }

    private boolean isUrl(String input){
        try{
            new URL(input);

            return true;
        }
        catch (MalformedURLException ignored){
            return false;
        }
    }

    @Override
    public String getName() {
        return "play";
    }
}
