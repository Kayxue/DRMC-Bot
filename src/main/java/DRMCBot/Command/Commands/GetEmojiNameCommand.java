package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;

import java.util.HashMap;
import java.util.List;

public class GetEmojiNameCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws Exception {
        ctx.getChannel().retrieveMessageById(ctx.getArgs().get(0)).queue(
                message -> {
                    ctx.getChannel().sendMessage(message.getReactions().get(0).getReactionEmote().getName()).queue();
                }
        );
    }

    @Override
    public String getName() {
        return "getemojiname";
    }

    @Override
    public String getCategory() {
        return "nocategory";
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
