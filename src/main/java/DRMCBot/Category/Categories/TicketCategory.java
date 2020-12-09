package DRMCBot.Category.Categories;

import DRMCBot.Category.CategoryBase;
import DRMCBot.Category.ICategory;
import DRMCBot.Command.Commands.Ticket.CloseTicketCommand;
import DRMCBot.Command.Commands.Ticket.OpenTicketCommand;
import DRMCBot.Command.ICommand;

import java.util.List;

public class TicketCategory extends CategoryBase implements ICategory {
    public TicketCategory() {
        addCommand(new OpenTicketCommand());
        addCommand(new CloseTicketCommand());
    }
    @Override
    public List<ICommand> getCommand() {
        return super.command;
    }

    @Override
    public String getName() {
        return "ticket";
    }

    @Override
    public String getDescription() {
        return "管理員私訊窗口類（目前僅限DRMC使用）";
    }
}
