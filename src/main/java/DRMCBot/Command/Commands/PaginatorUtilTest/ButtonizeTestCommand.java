package DRMCBot.Command.Commands.PaginatorUtilTest;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import com.github.ygimenez.method.Pages;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import java.util.Collections;
import java.util.function.BiConsumer;

public class ButtonizeTestCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws Exception {
        //THE BICONSUMER IS A CALLBACK FUNCTION THAT USES TWO ARGUMENTS INSTEAD OF ONE
        //HERE, THE MEMBER IS THE ONE THAT PRESSED THE BUTTON, AND MESSAGE IS THE BUTTONIZED MESSAGE ITSELF
        BiConsumer<Member, Message> customFunction = (mb, ms) -> {
            //EXAMPLE OF GIVING A ROLE TO ANYONE WHO PRESSES THIS BUTTON
            ctx.getChannel().sendMessage("test").queue();
        };

        ctx.getChannel().sendMessage("This is a sample message").queue(success -> {
            //SAME ARUMENTS, EXCEPT THE SECOND THAT MUST EXTEND Map Collection
            //THE LAST ARGUMENT DEFINES WHETHER TO SHOW CANCEL BUTTON OR NOT
            Pages.buttonize(success, Collections.singletonMap("✅", customFunction), false);
        });
    }

    @Override
    public String getName() {
        return "buttontest";
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
