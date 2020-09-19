package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;

import java.util.Arrays;
import java.util.Collections;
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
}
