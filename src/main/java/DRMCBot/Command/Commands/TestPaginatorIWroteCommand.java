package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.Utils.Paginator;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.LinkedList;

public class TestPaginatorIWroteCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws Exception {
        LinkedList<EmbedBuilder> embeds = new LinkedList<>();
        for (int i = 0; i <= 10; i++) {
            embeds.add(EmbedUtils.embedMessage(String.valueOf(i)));
        }
        new Paginator(embeds, 20, ctx.getChannel()).start();
    }

    @Override
    public String getName() {
        return "mypaginator";
    }

    @Override
    public String getCategory() {
        return "nocategory";
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
