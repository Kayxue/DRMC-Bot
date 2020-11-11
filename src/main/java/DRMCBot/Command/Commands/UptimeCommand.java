package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class UptimeCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        long ultimewholesecond = runtimeMXBean.getUptime() / 1000;
        long h = ultimewholesecond / 3600;
        long m = (ultimewholesecond % 3600) / 60;
        long s = (ultimewholesecond % 3600) % 60;
        StringBuilder builder = new StringBuilder();
        if (h != 0) {
            builder.append(h + "小時\n");
        }
        if (m != 0) {
            builder.append(m + "分\n");
        }
        if (s != 0) {
            builder.append(s + "秒\n");
        }
        ctx.getChannel().sendMessage("上線時間：" + builder.toString()).queue();
    }

    @Override
    public String getName() {
        return "uptime";
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
