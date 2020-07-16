package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.menudocs.paste.PasteClient;
import org.menudocs.paste.PasteClientBuilder;
import org.menudocs.paste.PasteHost;

import java.util.List;

public class PasteCommand implements ICommand {
    private final PasteClient client = new PasteClientBuilder()
            .setUserAgent("The Bot Of New DL/RS/MC Chatroom")
            .setDefaultExpiry("10m")
            .build();

    @Override
    public void handle(CommandContext ctx) {
        final List<String> args=ctx.getArgs();
        final TextChannel channel=ctx.getChannel();

        if(args.size()<2){
            channel.sendMessage("缺少參數！").queue();
            return;
        }

        final String language = args.get(0);
        final String contentRaw = ctx.getMessage().getContentRaw();
        final int index=contentRaw.indexOf(language)+language.length();
        final String body = contentRaw.substring(index).trim();

        client.createPaste(language,body).async(
                (id)->client.getPaste(id).async((paste)-> {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setTitle("Paste "+id,paste.getPasteUrl())
                            .setColor(0x01afef)
                            .setDescription("```")
                            .appendDescription(paste.getLanguage().getId())
                            .appendDescription("\n")
                            .appendDescription(paste.getBody())
                            .appendDescription("```");

                    channel.sendMessage(builder.build()).queue();
                })
        );
    }

    @Override
    public String getName() {
        return "paste";
    }
}
