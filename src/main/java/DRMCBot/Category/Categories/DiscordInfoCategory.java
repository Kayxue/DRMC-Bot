package DRMCBot.Category.Categories;

import DRMCBot.Category.CategoryBase;
import DRMCBot.Category.ICategory;
import DRMCBot.Command.Commands.*;
import DRMCBot.Command.ICommand;
import DRMCBot.CommandManagerV3;

import java.util.LinkedList;
import java.util.List;

public class DiscordInfoCategory extends CategoryBase implements ICategory {
    public final LinkedList<ICommand> command = new LinkedList<>();

    public DiscordInfoCategory(CommandManagerV3 managerV3) {
        addCommand(new PingCommand());
        addCommand(new ServerinfoCommand());
        addCommand(new BotInfoCommand(managerV3));
        addCommand(new CommandRunLengthCommandV3(managerV3));
        addCommand(new HelpCommandV3(managerV3));
        addCommand(new UserinfoCommand());
        addCommand(new UptimeCommand());
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
