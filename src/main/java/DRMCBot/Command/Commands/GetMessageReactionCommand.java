package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;

public class GetMessageReactionCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws Exception {
        if (ctx.getArgs().isEmpty()) {
            return;
        }

        System.out.println(ctx.getChannel().retrieveMessageById(ctx.getArgs().get(0)).complete().getReactions());

    }

    @Override
    public String getName() {
        return "getmessagereaction";
    }
}
