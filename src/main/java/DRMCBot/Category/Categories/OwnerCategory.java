package DRMCBot.Category.Categories;

import DRMCBot.Category.CategoryBase;
import DRMCBot.Category.ICategory;
import DRMCBot.Command.Commands.Owner.CloseBotControllerCommand;
import DRMCBot.Command.Commands.Owner.EvalCommand;
import DRMCBot.Command.Commands.Owner.OpenBotControllerCommad;
import DRMCBot.Command.Commands.Owner.ReopenBotControllerCommand;
import DRMCBot.Command.ICommand;
import DRMCBot.CommandManagerV3;

import java.util.List;

public class OwnerCategory extends CategoryBase implements ICategory {
    public OwnerCategory(CommandManagerV3 managerV3) {
        addCommand(new OpenBotControllerCommad(managerV3));
        addCommand(new ReopenBotControllerCommand(managerV3));
        addCommand(new CloseBotControllerCommand(managerV3));
        addCommand(new EvalCommand());
    }

    @Override
    public List<ICommand> getCommand() {
        return super.command;
    }

    @Override
    public String getName() {
        return "owner";
    }

    @Override
    public String getDescription() {
        return "機器人擁有者類";
    }
}
