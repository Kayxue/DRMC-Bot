package DRMCBot.Command.Commands.admin;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ClearCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        Member me = ctx.getMember();
        Member bot= ctx.getSelfMember();

        if (!me.hasPermission(Permission.MESSAGE_MANAGE)) {
            channel.sendMessage("你沒有權限清理訊息！").queue();
            return;
        }

        if (!bot.hasPermission(Permission.MESSAGE_MANAGE)) {
            channel.sendMessage("我沒有權限清理訊息！").queue();
            return;
        }

        if (ctx.getArgs().isEmpty()) {
            channel.sendMessage("請填寫參數！").queue();
            return;
        }

        int count;
        String countstring = ctx.getArgs().get(0);
        try {
            count = Integer.parseInt(countstring);
        } catch (NumberFormatException e) {
            channel.sendMessage(countstring + "不是數字！").queue();
            return;
        }

        if (count < 2 || count > 100) {
            channel.sendMessage("數字必須介在2~100之間！").queue();
            return;
        }
        channel.getIterableHistory()
                .takeAsync(count + 1)
                .thenAcceptAsync(
                        messages -> {
                            List<Message> messagestodelete = messages.stream()
                                    .filter(m -> !m.getTimeCreated().isBefore(
                                            OffsetDateTime.now().minus(2, ChronoUnit.WEEKS)
                                    ))
                                    .collect(Collectors.toList());

                            channel.purgeMessages(messagestodelete);
                        }
                )
                .whenCompleteAsync((amount, thr) -> {
                    channel.sendMessage("成功刪除" + count + "則訊息！").queue(
                            message -> message.delete().queueAfter(10, TimeUnit.SECONDS)
                    );
                });
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getCategory() {
        return "management";
    }

    @Override
    public String getdescription() {
        return "清理指定數量之訊息";
    }

    @Override
    public List<String> getUsages() {
        return List.of("clear <count>");
    }

    @Override
    public List<String> getExamples() {
        return List.of("clear 5");
    }

    @Override
    public HashMap<String, HashMap<String, String>> getArguments() {
        HashMap<String, String> count = new HashMap<>();
        count.put("沒有指定", "可為1~100間之數字");
        HashMap<String, HashMap<String, String>> toReturn = new HashMap<>();
        toReturn.put("count", count);
        return toReturn;
    }
}
