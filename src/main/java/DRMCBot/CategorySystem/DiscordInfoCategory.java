package DRMCBot.CategorySystem;

import DRMCBot.Command.Commands.PingCommand;
import DRMCBot.Command.Commands.ServerinfoCommand;

public class DiscordInfoCategory implements ICategory{
    public DiscordInfoCategory() {
        addCommand(new PingCommand());
        addCommand(new ServerinfoCommand());
    }

    @Override
    public String getName() {
        return "discordinfo";
    }

    @Override
    public String getDescription() {
        return "Discord與機器人資訊類";
    }
}
