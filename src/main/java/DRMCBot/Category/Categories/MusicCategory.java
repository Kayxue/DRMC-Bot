package DRMCBot.Category.Categories;

import DRMCBot.Category.CategoryBase;
import DRMCBot.Category.ICategory;
import DRMCBot.Command.Commands.music.JoinCommand;
import DRMCBot.Command.Commands.music.LeaveCommand;
import DRMCBot.Command.Commands.music.PlayCommand;
import DRMCBot.Command.Commands.music.StopCommand;
import DRMCBot.Command.ICommand;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import java.util.List;

public class MusicCategory extends CategoryBase implements ICategory {
    public MusicCategory(EventWaiter waiter) {
        addCommand(new PlayCommand(waiter));
        addCommand(new StopCommand());
        addCommand(new LeaveCommand());
        addCommand(new JoinCommand());
    }

    @Override
    public List<ICommand> getCommand() {
        return super.command;
    }

    @Override
    public String getName() {
        return "music";
    }

    @Override
    public String getDescription() {
        return null;
    }
}
