package DRMCBot.Command.Commands.admin;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class KickCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel=ctx.getChannel();
        final Message message=ctx.getMessage();
        final Member member=ctx.getMember();
        final List<String> args=ctx.getArgs();

        if(args.size()<2||message.getMentionedMembers().isEmpty()){
            channel.sendMessage("缺少參數！").queue();
        }

        final Member target=message.getMentionedMembers().get(0);

        if (!member.canInteract(target)||!member.hasPermission(Permission.KICK_MEMBERS)){
            channel.sendMessage("您沒有權限踢除成員！").queue();
            return;
        }

        final Member selfMember=ctx.getSelfMember();

        if (!selfMember.canInteract(target)||!selfMember.hasPermission(Permission.KICK_MEMBERS)){
            channel.sendMessage("我沒有權限踢除成員！").queue();
            return;
        }

        final String reason=String.join(" ",args.subList(1,args.size()));

        ctx.getGuild()
                .kick(target,reason)
                .reason(reason)
                .queue(
                        (__) -> channel.sendMessage("踢除完成！").queue(),
                        (error)->channel.sendMessageFormat("無法踢出\n%s",error.getMessage()).queue()
                );
    }

    @Override
    public String getName() {
        return "kick";
    }
}
