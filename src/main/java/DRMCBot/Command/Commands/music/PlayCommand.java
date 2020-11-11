package DRMCBot.Command.Commands.music;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.Command.music.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;

import java.net.URI;
import java.net.URISyntaxException;
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
            ctx.getChannel().sendMessage("我不在語音頻道內！").queue();
            return;
        }



        if (!memberVoiceState.inVoiceChannel()){
            channel.sendMessage("你必須加入一個語音頻道！").queue();
            return;
        }

        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            ctx.getChannel().sendMessage("您跟我在不同頻道！").queue();
            return;
        }

        String link=String.join(" ",args);

        if (!isUrl(link)){
            link = "ytsearch:" + link;

            return;
        }

        DRMCBot.Command.music.PlayerManager manager = PlayerManager.getInstance();

        manager.loadAndPlay(ctx.getChannel(),link);
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
