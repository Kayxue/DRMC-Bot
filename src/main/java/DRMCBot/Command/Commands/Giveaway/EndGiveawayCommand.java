package DRMCBot.Command.Commands.Giveaway;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;

import java.util.HashMap;
import java.util.List;

public class EndGiveawayCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {

    }

    @Override
    public String getName() {
        return "endgiveaway";
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
