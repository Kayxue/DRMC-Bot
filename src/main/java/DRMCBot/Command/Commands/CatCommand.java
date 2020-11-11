package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import com.github.natanbc.reliqua.util.StatusCodeValidator;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;

public class CatCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        WebUtils.ins.getJSONArray(
                "https://api.thecatapi.com/api/images/get?format=json&results_per_page=1",
                builder -> builder.setStatusCodeValidator(StatusCodeValidator.ACCEPT_200)
        ).async(
                json -> {
                    String url = json.get(0).get("url").asText();
                    EmbedBuilder embedBuilder = EmbedUtils.embedImage(url);
                    ctx.getChannel().sendMessage(embedBuilder.build()).queue();
                },
                error -> {
                    ctx.getChannel().sendMessage(error.getLocalizedMessage()).queue();
                }
        );
    }

    @Override
    public String getName() {
        return "cat";
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
