package DRMCBot.Category.Categories;

import DRMCBot.Category.CategoryBase;
import DRMCBot.Category.ICategory;
import DRMCBot.Command.Commands.HowToBullShitCommand;
import DRMCBot.Command.Commands.NewMainQRCodeCommand;
import DRMCBot.Command.Commands.QRCodeCommand2;
import DRMCBot.Command.Commands.reurl.ReurlCommand;
import DRMCBot.Command.Commands.reurl.TinyurlCommand;
import DRMCBot.Command.ICommand;

import java.util.List;

public class GenerationCategory extends CategoryBase implements ICategory {
    public GenerationCategory() {
        addCommand(new QRCodeCommand2());
        addCommand(new HowToBullShitCommand());
        addCommand(new ReurlCommand());
        addCommand(new TinyurlCommand());
        addCommand(new NewMainQRCodeCommand());
    }
    @Override
    public List<ICommand> getCommand() {
        return super.command;
    }

    @Override
    public String getName() {
        return "generation";
    }

    @Override
    public String getDescription() {
        return "生成工具類";
    }
}
