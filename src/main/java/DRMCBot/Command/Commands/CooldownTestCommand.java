package DRMCBot.Command.Commands;

import DRMCBot.CacheList;
import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;

public class CooldownTestCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        if (CacheList.TestCooldownCommandInCooldown.contains(ctx.getAuthor())) {
            ctx.getChannel().sendMessage("您正在冷卻中！").queue();
            return;
        }
        new Thread(() -> {
            CacheList.TestCooldownCommandInCooldown.add(ctx.getAuthor());
            ctx.getChannel().sendMessage("冷卻中.....").queue();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            CacheList.TestCooldownCommandInCooldown.remove(ctx.getAuthor());
            ctx.getChannel().sendMessage("冷卻完畢！").queue();
        }).start();
    }

    @Override
    public String getName() {
        return "cooldowntest";
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
