package DRMCBot.Command.Commands.PaginatorUtilTest;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import com.github.ygimenez.method.Pages;
import com.github.ygimenez.model.Page;
import com.github.ygimenez.type.PageType;
import net.dv8tion.jda.api.MessageBuilder;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class CategoryTestCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws Exception {
        HashMap<String, Page> pages = new HashMap<>();
        MessageBuilder mb = new MessageBuilder();

//MANUALLY ADDING 3 CATEGORIES TO THE MAP, YOU COULD USE SOME KIND OF ITERATION TO FILL IT (Map key must be a emoji's unicode or emote name - See https://emojipedia.org/ for unicodes)
        mb.setContent("This is category 1");
        pages.put("\u26f3", new Page(PageType.TEXT, mb.build()));

        mb.setContent("This is category 2");
        pages.put("\u26bd", new Page(PageType.TEXT, mb.build()));

        mb.setContent("This is category 3");
        pages.put("\u270f", new Page(PageType.TEXT, mb.build()));

        ctx.getChannel().sendMessage("This is a menu message").queue(success -> {
            Pages.categorize(success, pages, 60, TimeUnit.SECONDS);
        });
    }

    @Override
    public String getName() {
        return "categorytest";
    }
}
