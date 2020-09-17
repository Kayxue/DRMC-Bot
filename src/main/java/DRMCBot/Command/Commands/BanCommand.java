package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class BanCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        User usertoban;
        String reason = "";
        Member member = ctx.getMember();
        Member selfMember = ctx.getSelfMember();
        if ((ctx.getMessage().getMentionedMembers().isEmpty() && args.isEmpty())) {
            ctx.getChannel().sendMessage("請輸入使用者ID或提及使用者！").queue();
            return;
        } else if (ctx.getMessage().getMentionedMembers().isEmpty() && args.size() >= 1) {
            //可執行
            System.out.println(args.get(0).length());
            if (args.get(0).length() != 18) {
                ctx.getChannel().sendMessage("請輸入正確的使用者ID！").queue();
                return;
            }
            usertoban = ctx.getJDA().retrieveUserById(Long.parseLong(args.get(0))).complete();
            if (usertoban == null) {
                ctx.getChannel().sendMessage("請輸入正確的使用者ID！").queue();
                return;
            }
            reason = String.join(" ", args.subList(1, args.size()));
        } else if (ctx.getMessage().getMentionedMembers().size() == 1) {
            //可執行
            usertoban = ctx.getMessage().getMentionedMembers().get(0).getUser();
            reason = String.join(" ", args.subList(1, args.size()));
        } else {
            //不可執行
            ctx.getChannel().sendMessage("請輸入參數或著您提及多位使用者！").queue();
            return;
        }

        if (!member.hasPermission(Permission.KICK_MEMBERS)){
            ctx.getChannel().sendMessage("您沒有權限封鎖成員！").queue();
            return;
        }

        if (!selfMember.hasPermission(Permission.KICK_MEMBERS)){
            ctx.getChannel().sendMessage("我沒有權限封鎖成員！").queue();
            return;
        }

        ctx.getGuild()
                .ban(usertoban, 7, reason)
                .reason(reason)
                .queue(
                        __ -> ctx.getChannel().sendMessage("封鎖完成！").queue(),
                        error -> ctx.getChannel().sendMessage(error.getMessage()).queue()
                );
    }

    @Override
    public String getName() {
        return "ban";
    }
}
