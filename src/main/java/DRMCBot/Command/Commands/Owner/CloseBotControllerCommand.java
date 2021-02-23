package DRMCBot.Command.Commands.Owner;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.CommandManagerV3;

import java.util.HashMap;
import java.util.List;

public class CloseBotControllerCommand implements ICommand {
    private final CommandManagerV3 manager;

    public CloseBotControllerCommand(CommandManagerV3 manager) {
        this.manager = manager;
    }

    @Override
    public void handle(CommandContext ctx) throws Exception {
        if (!ctx.getAuthor().getId().equals("470516498050580480")) {
            ctx.getChannel().sendMessage("僅機器人作者可使用此指令！").queue();
            return;
        }
        if (manager.botController == null) {
            ctx.getChannel().sendMessage("Bot Controller未開啟！").queue();
        } else {
            manager.botController.frame.setVisible(false);
            manager.botController.frame.dispose();
            manager.botController = null;
            ctx.getChannel().sendMessage("Bot Controller成功關閉！").queue();
        }
    }

    @Override
    public String getName() {
        return "closebotcontroller";
    }

    @Override
    public String getCategory() {
        return "management";
    }

    @Override
    public String getdescription() {
        return "關閉Bot控制器";
    }

    @Override
    public List<String> getUsages() {
        return List.of("closebotcontroller");
    }

    @Override
    public List<String> getExamples() {
        return List.of("closebotcontroller");
    }

    @Override
    public HashMap<String, HashMap<String, String>> getArguments() {
        return null;
    }
}
