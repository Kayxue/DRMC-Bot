package DRMCBot.Command.Commands.Owner;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.CommandManagerV3;
import DRMCBot.Swing.BotController;

import java.util.HashMap;
import java.util.List;

public class OpenBotControllerCommad implements ICommand {
    private final CommandManagerV3 manager;

    public OpenBotControllerCommad(CommandManagerV3 manager) {
        this.manager = manager;
    }

    @Override
    public void handle(CommandContext ctx) throws Exception {
        if (!ctx.getAuthor().getId().equals("470516498050580480")) {
            ctx.getChannel().sendMessage("僅機器人作者可使用此指令！").queue();
            return;
        }
        if (manager.botController != null) {
            ctx.getChannel().sendMessage("控制器已經開啟了").queue();
        } else {
            manager.botController = new BotController(manager, manager.jda);
            ctx.getChannel().sendMessage("新控制器已成功開啟").queue();
        }
    }

    @Override
    public String getName() {
        return "openbotcontroller";
    }

    @Override
    public String getCategory() {
        return "management";
    }

    @Override
    public String getdescription() {
        return "開啟Bot控制器";
    }

    @Override
    public List<String> getUsages() {
        return List.of("openbotcontroller");
    }

    @Override
    public List<String> getExamples() {
        return List.of("openbotcontroller");
    }

    @Override
    public HashMap<String, HashMap<String, String>> getArguments() {
        return null;
    }
}
