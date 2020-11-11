package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;
import java.util.List;

public class BotInfoCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {

    }

    @Override
    public List<String> getAliases() {
        String[] strings = {"binfo", "bi"};
        return Arrays.asList(strings);
    }

    @Override
    public String getName() {
        return "botinfo";
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
