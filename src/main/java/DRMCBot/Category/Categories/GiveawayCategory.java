package DRMCBot.Category.Categories;

import DRMCBot.Category.CategoryBase;
import DRMCBot.Category.ICategory;
import DRMCBot.Command.Commands.Giveaway.EndGiveawayCommand;
import DRMCBot.Command.Commands.Giveaway.RerollWinnerCommand;
import DRMCBot.Command.Commands.Giveaway.StartGiveawayCommand;
import DRMCBot.Command.ICommand;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import java.util.List;

public class GiveawayCategory extends CategoryBase implements ICategory {
    public GiveawayCategory(EventWaiter waiter) {
        addCommand(new EndGiveawayCommand());
        addCommand(new RerollWinnerCommand());
        addCommand(new StartGiveawayCommand(waiter));
    }

    @Override
    public List<ICommand> getCommand() {
        return super.command;
    }

    @Override
    public String getName() {
        return "giveaway";
    }

    @Override
    public String getDescription() {
        return "抽獎類（目前暫停使用）";
    }
}
