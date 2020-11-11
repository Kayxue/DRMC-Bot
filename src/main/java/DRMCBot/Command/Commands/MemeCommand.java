package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import com.fasterxml.jackson.databind.JsonNode;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class MemeCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel=ctx.getChannel();
        WebUtils.ins.getJSONObject("https://apis.duncte123.me/meme").async((json)->{
            if(!json.get("success").asBoolean()){
                channel.sendMessage("有地方出錯了，請稍後再試一次").queue();
                System.out.println(json);
                return;
            }

            final JsonNode data=json.get("data");
            final String title=data.get("title").asText();
            final String url=data.get("url").asText();
            final String image=data.get("image").asText();
            final EmbedBuilder embed=EmbedUtils.embedImageWithTitle(title,url,image);

            channel.sendMessage(embed.build()).queue();
        });
    }

    @Override
    public String getName() {
        return "meme";
    }

    @Override
    public String getCategory() {
        return "entertainment";
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
