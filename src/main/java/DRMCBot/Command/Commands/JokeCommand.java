package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import com.fasterxml.jackson.databind.JsonNode;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class JokeCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel=ctx.getChannel();
        WebUtils.ins.getJSONObject("https://apis.duncte123.me/joke").async((json)->{
            if(!json.get("success").asBoolean()){
                channel.sendMessage("有地方出錯了，請稍後再試一次").queue();
                System.out.println(json);
                return;
            }

            final JsonNode data=json.get("data");
            final String title=data.get("title").asText();
            final String url=data.get("url").asText();
            final String body=data.get("body").asText();
            final EmbedBuilder embed= EmbedUtils.defaultEmbed()
                    .setTitle(title,url)
                    .setDescription(body);

            channel.sendMessage(embed.build()).queue();
        });
    }

    @Override
    public String getName() {
        return "joke";
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
