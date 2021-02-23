package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.CommandManagerV3;
import DRMCBot.Database.DatabaseManager;
import DRMCBot.VeryBadDesign;
import com.sun.management.OperatingSystemMXBean;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.List;

public class BotInfoCommand implements ICommand {
    private final CommandManagerV3 commandManagerV3;

    public BotInfoCommand(CommandManagerV3 commandManagerV3) {
        this.commandManagerV3 = commandManagerV3;
    }

    @Override
    public void handle(CommandContext ctx) {
        Runtime runtime = Runtime.getRuntime();
        OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        int CPUUsed = (int) (osmb.getCpuLoad() * 100.0);
        int memoryLeft = (int) (osmb.getFreeMemorySize() / 1024 / 1024);
        int memoryTotal = (int) (osmb.getTotalMemorySize() / 1024 / 1024);
        int appMemoryMaxUse = (int) (runtime.maxMemory() / 1024 / 1024);
        int appMemoryFree = (int) (runtime.freeMemory() / 1024 / 1024);
        int appMemoryTotal = (int) (runtime.totalMemory() / 1024 / 1024);
        Member selfMember = ctx.getSelfMember();
        User self = selfMember.getUser();
        long guildID = ctx.getGuild().getIdLong();
        String prefix = VeryBadDesign.PREFIXES.computeIfAbsent(guildID, DatabaseManager.INSTANCE::getPrefix);
        long commandsCount= commandManagerV3.returnCommandsCount();
        User botowner = ctx.getJDA().getUserById("470516498050580480");
        EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                .setTitle("關於機器人「" + self.getAsTag() + "」")
                .setDescription("我的ID：" + self.getId())
                .addField("於此伺服器之設定：", "**前綴：**``" + prefix + "``\n", false)
                .addField("其他資訊", "**指令總數：**" + commandsCount + "\n" +
                        "**被邀請至伺服器數：**" + ctx.getJDA().getGuilds().size() + "\n" +
                        "**Java版本：**" + System.getProperty("java.version"), false)
                .addField("JDA", "**版本：**``" + JDAInfo.VERSION_MAJOR + "." + JDAInfo.VERSION_MINOR + "." + JDAInfo.VERSION_REVISION + "_" + JDAInfo.VERSION_BUILD + "``\n" +
                        "**音訊Gateway版本：**``" + JDAInfo.AUDIO_GATEWAY_VERSION + "``\n" +
                        "**Gateway版本：**``" + JDAInfo.DISCORD_GATEWAY_VERSION + "``\n" +
                        "**Rest版本：**``" + JDAInfo.DISCORD_REST_VERSION + "``", false)
                .addField("運行環境", "**作業系統：**``" + System.getProperty("os.name") + " (Ver. " + System.getProperty("os.version") + ")" + "``\n"
                        + "**中央處理器：**``" + CPUUsed + "%/100%``\n" +
                        "**記憶體：**``" + (memoryTotal - memoryLeft) + "MB/" + memoryTotal + "MB``", false)
                .addField("本程式", "**總共記憶體使用：**``" + appMemoryTotal + "MB``\n" +
                        "**最大記憶體使用：**``" + appMemoryMaxUse + "MB``\n" +
                        "**剩餘記憶體可用：**``" + appMemoryFree + "MB``\n", false)
                .setThumbnail(self.getAvatarUrl())
                .setFooter("機器人作者：" + botowner.getAsTag(), botowner.getAvatarUrl());
        ctx.getChannel().sendMessage(embed.build()).queue();
    }

    @Override
    public List<String> getAliases() {
        return List.of("bi", "binfo");
    }

    @Override
    public String getName() {
        return "botinfo";
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
