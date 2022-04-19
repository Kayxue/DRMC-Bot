package DRMCBot.Category.Categories;

import DRMCBot.Category.CategoryBase;
import DRMCBot.Category.ICategory;
import DRMCBot.Command.Commands.*;
import DRMCBot.Command.Commands.GiveawayTest.GiveawayWaitTestCommand;
import DRMCBot.Command.Commands.GiveawayTest.RegularExpressionTestCommand;
import DRMCBot.Command.ICommand;
import DRMCBot.CommandManagerV3;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import java.util.List;

public class NoCategory extends CategoryBase implements ICategory {

    public NoCategory(EventWaiter waiter) {
        addCommand(new SayCommand());
        addCommand(new GetMongoDbCollectionCommand());
        addCommand(new EventWaiterTestCommand(waiter));
        addCommand(new RegularExpressionTestCommand());
        addCommand(new CooldownTestCommand());
        addCommand(new GiveawayWaitTestCommand());
        addCommand(new GetEmojiNameCommand());
        addCommand(new GetMessageReactionCommand());
        addCommand(new RemoveBackgroundCommand());
        addCommand(new LinguistParseJsonTestCommand());
        addCommand(new TestPaginatorIWroteCommand());
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
        return "未分類";
    }
}
