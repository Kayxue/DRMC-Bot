package DRMCBot.Category.Categories;

import DRMCBot.Category.ICategory;
import DRMCBot.Command.Commands.HelpCommandV3;
import DRMCBot.Command.Commands.PingCommand;
import DRMCBot.Command.Commands.SayCommand;
import DRMCBot.Command.ICommand;
import DRMCBot.CommandManagerV3;

import java.util.LinkedList;
import java.util.List;

public class NoCategory implements ICategory {
    public final LinkedList<ICommand> command = new LinkedList<>();

    public NoCategory(CommandManagerV3 commandManagerV3) {
        addCommand(new SayCommand());
        addCommand(new HelpCommandV3(commandManagerV3));
    }

    @Override
    public void addCommand(ICommand cmd) {
        boolean nameFoundInCategory = command.stream().anyMatch(it -> it.getName().equalsIgnoreCase(cmd.getName()));
        if (nameFoundInCategory) {
            throw new IllegalArgumentException("A Command with this name is already present");
        }
        command.add(cmd);
    }

    @Override
    public List<ICommand> getCommand() {
        return command;
    }

    @Override
    public String getName() {
        return "nocategory";
    }

    @Override
    public String getDescription() {
        return null;
    }
}
