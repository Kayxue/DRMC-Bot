package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;

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
}
