package DRMCBot.Command.Commands.PaginatorUtilTest;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import com.github.ygimenez.method.Pages;
import com.github.ygimenez.model.Page;
import com.github.ygimenez.type.PageType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PaginatorTestCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws Exception {
        ArrayList<Page> pages = new ArrayList<>();
        MessageBuilder mb = new MessageBuilder();

//ADDING 10 PAGES TO THE LIST
        for (int i = 0; i < 10; i++) {
            mb.clear();
            mb.setContent("This is entry Nº " + i);
            pages.add(new Page(PageType.TEXT,mb.build()));
        }

        ctx.getChannel().sendMessage((Message) pages.get(0).getContent()).queue(success -> {
            Pages.paginate(success, pages, 60, TimeUnit.SECONDS);
        });
    }

    @Override
    public String getName() {
        return "testpage";
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
