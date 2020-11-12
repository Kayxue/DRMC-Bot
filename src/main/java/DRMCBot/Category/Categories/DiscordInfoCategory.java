package DRMCBot.Category.Categories;

import DRMCBot.Category.CategoryBase;
import DRMCBot.Category.ICategory;
import DRMCBot.Command.Commands.PingCommand;
import DRMCBot.Command.Commands.ServerinfoCommand;
import DRMCBot.Command.ICommand;

import java.util.LinkedList;
import java.util.List;

public class DiscordInfoCategory extends CategoryBase implements ICategory {
    public final LinkedList<ICommand> command = new LinkedList<>();

    public DiscordInfoCategory() {
        addCommand(new PingCommand());
        addCommand(new ServerinfoCommand());
    }

    @Override
    public List<ICommand> getCommand() {
        return super.command;
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
