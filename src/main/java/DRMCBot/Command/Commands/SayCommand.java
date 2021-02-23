package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;

import java.util.HashMap;
import java.util.List;

public class SayCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws Exception {
        List<String> args = ctx.getArgs();
        if (args.isEmpty()) {
            ctx.getChannel().sendMessage("請輸入參數！").queue();
            return;
        }
        ctx.getChannel().sendMessage(String.join(" ", args)).queue();
    }

    @Override
    public String getName() {
        return "say";
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
