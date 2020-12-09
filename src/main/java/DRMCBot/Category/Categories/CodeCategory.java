package DRMCBot.Category.Categories;

import DRMCBot.Category.CategoryBase;
import DRMCBot.Category.ICategory;
import DRMCBot.Command.Commands.code.*;
import DRMCBot.Command.ICommand;

import java.util.List;

public class CodeCategory extends CategoryBase implements ICategory {
    public CodeCategory() {
        addCommand(new PasteCommand());
        addCommand(new PastemystCommand());
        addCommand(new SourceBinCommand());
        addCommand(new HasteCommand());
        addCommand(new DiscordJSDocumentationCommand());
    }
    @Override
    public List<ICommand> getCommand() {
        return super.command;
    }

    @Override
    public String getName() {
        return "code";
    }

    @Override
    public String getDescription() {
        return "程式碼桶子工具類";
    }
}
