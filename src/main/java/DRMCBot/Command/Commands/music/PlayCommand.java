package DRMCBot.Command.Commands.music;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class PlayCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel=ctx.getChannel();
        List<String> args = ctx.getArgs();

        if (args.isEmpty()){
            channel.sendMessage("請輸入參數！").queue();
            return;
        }

        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()){
            channel.sendMessage("你必須加入一個語音頻道！").queue();
            return;
        }

        while (true) {
            if (!selfVoiceState.inVoiceChannel()) {
                if (memberVoiceState.inVoiceChannel()) {
                    AudioManager manager = ctx.getGuild().getAudioManager();
                    manager.openAudioConnection(memberVoiceState.getChannel());
                    break;
                } else {
                    ctx.getChannel().sendMessage("請加入一個語音頻道！").queue();
                    return;
                }
            }



            if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
                ctx.getChannel().sendMessage("您跟我在不同頻道！").queue();
                return;
            }
            break;
        }

        String link=String.join(" ",ctx.getArgs());

        if (!isUrl(link)){
            link = "ytsearch:" + link;
        }

        PlayerManager manager = PlayerManager.getInstance();

        manager.loadAndPlay(channel,link);
    }

    private boolean isUrl(String input){
        try{
            new URI(input);
            return true;
        }
        catch (URISyntaxException ignored){
            return false;
        }
    }

    @Override
    public String getName() {
        return "play";
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
