package DRMCBot.Command.Commands;

import DRMCBot.Category.ICategory;
import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.CommandManagerV2;
import DRMCBot.CommandManagerV3;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

public class HelpCommandV3 implements ICommand {
    private final CommandManagerV3 commandManager;

    public HelpCommandV3(CommandManagerV3 commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void handle(CommandContext ctx) {
        User botowner = ctx.getJDA().getUserById("470516498050580480");
        String output = "";
        ICategory category = commandManager.getCategory("nocategory");
        for (ICommand cmd : category.getCommand()) {
            ctx.getChannel().sendMessage(cmd.getName()).queue();
        }
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getCategory() {
        return "discordinfo";
    }

    @Override
    public String getdescription() {
        return null;
    }

    @Override
    public EmbedBuilder gethelpembed() {
        return null;
    }

}
