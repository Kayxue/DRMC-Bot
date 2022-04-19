package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.CommandManagerV3;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandRunLengthCommandV3 implements ICommand {
    private final CommandManagerV3 commandManager;

    public CommandRunLengthCommandV3(CommandManagerV3 manager) {
        this.commandManager = manager;
    }

    @Override
    public void handle(CommandContext ctx) throws Exception {
        if (ctx.getArgs().isEmpty()) {
            ctx.getChannel().sendMessage("請輸入參數！").queue();
            return;
        }
        if ("commandrunlength".equals(ctx.getArgs().get(0))) {
            ctx.getChannel().sendMessage("無法測得此指令執行時間！").queue();
            return;
        }
        ZonedDateTime startedat = ZonedDateTime.now(ZoneId.of("Asia/Taipei"));
        ArrayList<String> args = new ArrayList<>(ctx.getArgs());
        List<String> newargs;
        if (args.size() != 1) {
            newargs = args.subList(1, args.size());
        } else {
            newargs = null;
        }
        ICommand cmd = commandManager.getCommand(args.get(0));
        if (cmd == null) {
            ctx.getChannel().sendMessage("找不到此指令！").queue();
            return;
        }
        ZonedDateTime gotcmdat = ZonedDateTime.now(ZoneId.of("Asia/Taipei"));
        boolean issuccess;
        try {
            cmd.handle(new CommandContext(ctx.getEvent(), newargs));
            issuccess = true;
        } catch (Exception e) {
            issuccess = false;
        }
        if (issuccess) {
            ZonedDateTime endedat = ZonedDateTime.now(ZoneId.of("Asia/Taipei"));
            String gotcommandlength = "取得指令時間：" + Duration.between(startedat, gotcmdat).toMillis() + "毫秒";
            String runcommandlength = "執行指令時間：" + Duration.between(gotcmdat, endedat).toMillis() + "毫秒";
            EmbedBuilder embedBuilder = EmbedUtils.getDefaultEmbed()
                    .setTitle("指令" + args.get(0) + "執行時長")
                    .setDescription(gotcommandlength + "\n" + runcommandlength);
            ctx.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
        } else {
            ctx.getChannel().sendMessage("指令出錯！").queue();
        }
    }

    @Override
    public String getName() {
        return "commandrunlength";
    }

    @Override
    public String getCategory() {
        return "discordinfo";
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
