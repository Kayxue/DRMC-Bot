package DRMCBot.Command.Commands.music;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.Command.music.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class PlayCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel=ctx.getChannel();
        List<String> args=ctx.getArgs();

        if (args.isEmpty()){
            channel.sendMessage("請輸入參數！").queue();
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
