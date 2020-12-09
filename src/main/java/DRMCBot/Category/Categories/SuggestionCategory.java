package DRMCBot.Category.Categories;

import DRMCBot.Category.CategoryBase;
import DRMCBot.Category.ICategory;
import DRMCBot.Command.Commands.suggestion.*;
import DRMCBot.Command.ICommand;

import java.util.List;

public class SuggestionCategory extends CategoryBase implements ICategory {
    public SuggestionCategory() {
        addCommand(new SuggestionCommand());
        addCommand(new ApproveCommand());
        addCommand(new ConsiderCommand());
        addCommand(new DenyCommand());
        addCommand(new ImplementCommand());
    }
    @Override
    public List<ICommand> getCommand() {
        return super.command;
    }

    @Override
    public String getName() {
        return "suggestion";
    }

    @Override
    public String getDescription() {
        return "建議類（目前僅限DRMC使用）";
    }
}
