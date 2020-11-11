package DRMCBot.Command.Commands.GiveawayTest;

import DRMCBot.CacheList;
import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;

public class GiveawayWaitTestCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws Exception {
        ctx.getChannel().sendMessage("抽獎開始").queue();
        new Thread(() -> {
            CacheList.TestCooldownCommandInCooldown.add(ctx.getAuthor());
            ctx.getChannel().sendMessage("抽獎等待中.....\n（此為模擬，10秒後開抽）").queue();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            CacheList.TestCooldownCommandInCooldown.remove(ctx.getAuthor());
            ctx.getChannel().sendMessage("等待完畢！").queue();
        }).start();
    }

    @Override
    public String getName() {
        return "giveawaywaittest";
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
