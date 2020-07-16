package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDAInfo;

import javax.lang.model.element.Name;

public class PingCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        JDA jda=ctx.getJDA();

        jda.getRestPing().queue(
                (ping) -> ctx.getChannel()
                .sendMessage(EmbedUtils.defaultEmbed()
                        .setTitle("回應資訊")
                        .setDescription("休息時回應: "+ping
                                +"毫秒\n網路端回應: " +jda.getGatewayPing()
                                +"毫秒").build()).queue()
        );
    }

    @Override
    public String getName() {
        return "ping";
    }
}
