package DRMCBot.Category.Categories;

import DRMCBot.Category.CategoryBase;
import DRMCBot.Category.ICategory;
import DRMCBot.Command.Commands.music.*;
import DRMCBot.Command.ICommand;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import java.util.List;

public class MusicCategory extends CategoryBase implements ICategory {
    public MusicCategory(EventWaiter waiter) {
        addCommand(new PlayCommand(waiter));
        addCommand(new StopCommand());
        addCommand(new LeaveCommand());
        addCommand(new JoinCommand());
        addCommand(new PauseCommand());
        addCommand(new ResumeCommand());
        addCommand(new QueueCommand());
        addCommand(new SkipCommand());
        addCommand(new NowPlayingCommand());
        addCommand(new RepeatCommand());
        addCommand(new SetVolumeCommand());
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
        return "音樂播放類";
    }
}
