package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import com.github.natanbc.reliqua.util.StatusCodeValidator;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.HashMap;
import java.util.List;

public class DogCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        WebUtils.ins.getJSONObject(
                "https://dog.ceo/api/breeds/image/random",
                builder -> builder.setStatusCodeValidator(StatusCodeValidator.ACCEPT_200)
        ).async(
                json -> {
                    if ("success".equals(json.get("status").asText())) {
                        String url = json.get("message").asText();
                        EmbedBuilder embedBuilder = EmbedUtils.embedImage(url);
                        ctx.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
                    } else {
                        ctx.getChannel().sendMessage("API出錯了！").queue();
                    }
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

    @Override
    public String getCategory() {
        return "entertainment";
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
}
