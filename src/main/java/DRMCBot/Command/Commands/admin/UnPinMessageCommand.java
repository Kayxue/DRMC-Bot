package DRMCBot.Command.Commands.admin;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

import java.util.List;

public class UnPinMessageCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws Exception {
        List<String> args = ctx.getArgs();
        if (args.isEmpty()) {
            ctx.getChannel().sendMessage("請輸入要釘選之訊息編號！").queue();
            return;
        }

        ctx.getChannel().retrieveMessageById(Long.parseLong(args.get(0))).queue(
                message -> message.unpin().queue(__ -> ctx.getChannel().sendMessage("訊息已成功解除釘選！").queue()),
                error -> {
                    if (error instanceof InsufficientPermissionException) {
                        ctx.getChannel().sendMessage("您無權限釘選此訊息！").queue();
                    }
                }
        );
    }

    @Override
    public String getName() {
        return "unpinmessage";
    }
}
