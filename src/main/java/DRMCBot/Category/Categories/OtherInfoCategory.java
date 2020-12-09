package DRMCBot.Category.Categories;

import DRMCBot.Category.CategoryBase;
import DRMCBot.Category.ICategory;
import DRMCBot.Command.Commands.*;
import DRMCBot.Command.Commands.Hypixel.GetHypixelServerBoosterCommand;
import DRMCBot.Command.ICommand;

import java.util.List;

public class OtherInfoCategory extends CategoryBase implements ICategory {
    public OtherInfoCategory() {
        addCommand(new InstagramCommand());
        addCommand(new MinecraftCommand());
        addCommand(new CoronaVirusDataCommand());
        addCommand(new CouponLeftCommand());
        addCommand(new AccuWeatherCommand());
        addCommand(new GetHypixelServerBoosterCommand());
        addCommand(new JLyricCommand());
        addCommand(new ExchangeCommand());
        addCommand(new OsuCommand());
    }
    @Override
    public List<ICommand> getCommand() {
        return super.command;
    }

    @Override
    public String getName() {
        return "otherinfo";
    }

    @Override
    public String getDescription() {
        return "其他資訊類";
    }
}
