package DRMCBot.Command.Commands.admin;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class ClearUserAllMessageCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws Exception {
        TextChannel channel = ctx.getChannel();
        List<Message> Messages = channel.getHistory().getRetrievedHistory();
        channel.sendMessage(Messages.size() + "").queue();

    }

    @Override
    public String getName() {
        return "clearuserallmessage";
    }

    @Override
    public String getCategory() {
        return "management";
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
