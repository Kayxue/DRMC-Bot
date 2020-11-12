package DRMCBot.Category.Categories;

import DRMCBot.Category.CategoryBase;
import DRMCBot.Category.ICategory;
import DRMCBot.Command.Commands.DiscordJSDocumentationCommand;
import DRMCBot.Command.Commands.HelpCommandV3;
import DRMCBot.Command.Commands.SayCommand;
import DRMCBot.Command.ICommand;
import DRMCBot.CommandManagerV3;

import java.util.LinkedList;
import java.util.List;

public class NoCategory extends CategoryBase implements ICategory {

    public NoCategory(CommandManagerV3 commandManagerV3) {
        addCommand(new SayCommand());
        addCommand(new HelpCommandV3(commandManagerV3));
        addCommand(new DiscordJSDocumentationCommand());
    }


    @Override
    public List<ICommand> getCommand() {
        return super.command;
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
