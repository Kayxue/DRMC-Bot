package DRMCBot.Category.Categories;

import DRMCBot.Category.CategoryBase;
import DRMCBot.Category.ICategory;
import DRMCBot.Command.Commands.*;
import DRMCBot.Command.Commands.anime.ChinoCommand;
import DRMCBot.Command.Commands.anime.KitsuneCommand;
import DRMCBot.Command.Commands.anime.NekoCommand;
import DRMCBot.Command.ICommand;

import java.util.List;

public class EntertainmentCategory extends CategoryBase implements ICategory {
    public EntertainmentCategory() {
        addCommand(new ChinoCommand());
        addCommand(new KitsuneCommand());
        addCommand(new NekoCommand());
        addCommand(new CatCommand());
        addCommand(new DogCommand());
        addCommand(new DuncanDiscordMonsterAPICommand());
        addCommand(new EightBallCommand());
        addCommand(new JokeCommand());
        addCommand(new MemeCommand());
        addCommand(new SaltyCommand());
    }
    @Override
    public List<ICommand> getCommand() {
        return super.command;
    }

    @Override
    public String getName() {
        return "entertainment";
    }

    @Override
    public String getDescription() {
        return "娛樂類";
    }
}
