package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;

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

    @Override
    public String getCategory() {
        return "nocategory";
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
