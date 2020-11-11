package DRMCBot.Category.Categories;

import DRMCBot.Category.ICategory;
import DRMCBot.Command.Commands.PingCommand;
import DRMCBot.Command.Commands.ServerinfoCommand;
import DRMCBot.Command.ICommand;

import java.util.LinkedList;
import java.util.List;

public class DiscordInfoCategory implements ICategory {
    public final LinkedList<ICommand> command = new LinkedList<>();
    public DiscordInfoCategory() {
        addCommand(new PingCommand());
        addCommand(new ServerinfoCommand());
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
        return "discordinfo";
    }

    @Override
    public String getDescription() {
        return "Discord與機器人資訊類";
    }
}
