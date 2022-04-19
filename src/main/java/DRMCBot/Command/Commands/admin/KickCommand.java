package DRMCBot.Command.Commands.admin;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.List;

public class KickCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = (TextChannel) ctx.getChannel();
        final Message message = ctx.getMessage();
        final Member member = ctx.getMember();
        final List<String> args = ctx.getArgs();

        if (args.size() < 1) {
            channel.sendMessage("缺少參數！").queue();
        }

        Member target = (message.getMentionedMembers().size() == 0 ? null : message.getMentionedMembers().get(0));
        if (target == null) {
            target = message.getGuild().getMemberById(args.get(0));
            if (target == null) {
                message.getChannel().sendMessage("找不到該成員！").queue();
                return;
            }
        }

        if (!member.canInteract(target) || !member.hasPermission(Permission.KICK_MEMBERS)) {
            channel.sendMessage("您沒有權限踢除成員！").queue();
            return;
        }

        final Member selfMember = ctx.getSelfMember();

        if (!selfMember.canInteract(target) || !selfMember.hasPermission(Permission.KICK_MEMBERS)) {
            channel.sendMessage("我沒有權限踢除成員！").queue();
            return;
        }

        final String reason = (args.size() > 1 ? String.join(" ", args.subList(1, args.size())) : null);

        ctx.getGuild()
                .kick(target, reason)
                .reason(reason)
                .queue(
                        (__) -> channel.sendMessage("踢除完成！").queue(),
                        (error) -> channel.sendMessageFormat("無法踢出\n%s", error.getMessage()).queue()
                );
    }

    @Override
    public String getName() {
        return "kick";
    }

    @Override
    public String getCategory() {
        return "management";
    }

    @Override
    public String getdescription() {
        return "從伺服器踢出一位成員";
    }

    @Override
    public List<String> getUsages() {
        return List.of("kick <member>");
    }

    @Override
    public List<String> getExamples() {
        return List.of("kick @我不是人#1234","kick 123456789012345678");
    }

    @Override
    public HashMap<String, HashMap<String, String>> getArguments() {
        HashMap<String, String> member = new HashMap<>();
        member.put("沒有指定", "可為任意提及之成員或成員之ID");
        HashMap<String, HashMap<String, String>> toReturn = new HashMap<>();
        toReturn.put("member", member);
        return toReturn;
    }
}
