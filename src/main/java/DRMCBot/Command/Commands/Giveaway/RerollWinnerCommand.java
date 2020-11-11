package DRMCBot.Command.Commands.Giveaway;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;

public class RerollWinnerCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws Exception {

    }

    @Override
    public String getName() {
        return "rerollwinner";
    }

    @Override
    public String getCategory() {
        return null;
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
