package DRMCBot.Category.Categories;

import DRMCBot.Category.CategoryBase;
import DRMCBot.Category.ICategory;
import DRMCBot.Command.Commands.ChannelAllPinCommand;
import DRMCBot.Command.Commands.admin.*;
import DRMCBot.Command.ICommand;

import java.util.List;

public class ManagementCategory extends CategoryBase implements ICategory {
    public ManagementCategory() {
        addCommand(new BanCommand());
        addCommand(new ClearCommand());
        addCommand(new KickCommand());
        addCommand(new PinMessageCommand());
        addCommand(new SetPrefixCommand());
        addCommand(new UnPinMessageCommand());
        addCommand(new ChannelAllPinCommand());
        addCommand(new ClearUserAllMessageCommand());
    }
    @Override
    public List<ICommand> getCommand() {
        return super.command;
    }

    @Override
    public String getName() {
        return "management";
    }

    @Override
    public String getDescription() {
        return "管理類";
    }
}
