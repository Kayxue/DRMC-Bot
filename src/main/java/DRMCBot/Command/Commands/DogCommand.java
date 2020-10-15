package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import com.github.natanbc.reliqua.util.StatusCodeValidator;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;

public class DogCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        WebUtils.ins.getJSONObject(
                "https://random.dog/woof.json",
                builder -> builder.setStatusCodeValidator(StatusCodeValidator.ACCEPT_200)
        ).async(
                json -> {
                    String url = json.get("url").asText();
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
        return "dog";
    }
}
