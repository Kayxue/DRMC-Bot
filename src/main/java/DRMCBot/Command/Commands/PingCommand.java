package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;

public class PingCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        JDA jda=ctx.getJDA();
        jda.getRestPing().queue(
                (ping) -> ctx.getChannel()
                        .sendMessage(EmbedUtils.getDefaultEmbed()
                                .setTitle("回應資訊")
                                .setDescription("運行於：``" + System.getProperty("os.name") + " (Ver. " + System.getProperty("os.version") + ")``" + "\n休息時回應: " + ping
                                        + "毫秒\n網路端回應: " + jda.getGatewayPing()
                                        + "毫秒").build()).queue()
        );
    }

    @Override
    public String getName() {
        return "ping";
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
    public EmbedBuilder gethelpembed() {
        return null;
    }
}
