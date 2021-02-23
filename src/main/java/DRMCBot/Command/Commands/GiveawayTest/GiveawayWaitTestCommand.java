package DRMCBot.Command.Commands.GiveawayTest;

import DRMCBot.Cache;
import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;

import java.util.HashMap;
import java.util.List;

public class GiveawayWaitTestCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws Exception {
        ctx.getChannel().sendMessage("抽獎開始").queue();
        new Thread(() -> {
            Cache.TestCooldownCommandInCooldown.add(ctx.getAuthor());
            ctx.getChannel().sendMessage("抽獎等待中.....\n（此為模擬，10秒後開抽）").queue();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Cache.TestCooldownCommandInCooldown.remove(ctx.getAuthor());
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
    public List<String> getUsages() {
        return null;
    }

    @Override
    public List<String> getExamples() {
        return null;
    }

    @Override
    public HashMap<String, HashMap<String, String>> getArguments() {
        return null;
    }
}
