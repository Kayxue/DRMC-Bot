package DRMCBot.Command.Commands.Hypixel;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.EnvironmentVariable;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.BoostersReply;

import java.util.List;

public class GetHypixelServerBoosterCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        HypixelAPI api = new HypixelAPI(EnvironmentVariable.uuid);
        api.getBoosters().whenCompleteAsync(
                (reply, throwable) -> {
                    String output = "";
                    List<BoostersReply.Booster> boosters = reply.getBoosters();
                    System.out.println(boosters.size());
                    if (!boosters.isEmpty()) {
                        output += "```-------------------------------\n";
                        System.out.println(boosters.get(0).toString());
                        if (boosters.size() < 5) {
                            for (BoostersReply.Booster booster : boosters) {
                                output += "加成者UUID：" + booster.getPurchaserUuid() + "\n";
                                output += "加成數量：" + booster.getAmount() + "\n";
                                output += "加成時長：" + booster.getOriginalLength() + "\n";
                                output += "加成剩餘時長：" + booster.getLength() + "\n";
                                output += ("-------------------------------\n");
                            }
                        } else {
                            for (int i = 0; i <= 5; i++) {
                                output += "加成者UUID：" + boosters.get(i).getPurchaserUuid() + "\n";
                                output += "加成數量：" + boosters.get(i).getAmount() + "\n";
                                output += "加成時長：" + boosters.get(i).getOriginalLength() + "\n";
                                output += "加成剩餘時長：" + boosters.get(i).getLength() + "\n";
                                output += "-------------------------------\n";
                            }
                        }
                        output += "\n```";
                        System.out.println(output);
                        EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                                .setTitle("目前Hypixel加成名單")
                                .setDescription(output);
                        ctx.getChannel().sendMessage(embed.build()).queue();
                    }
                }
        );
    }

    public String formattime(int i) {
        String a;
        if (String.valueOf(i).length() == 1) {
            a = "0" + i;
        } else {
            a = String.valueOf(i);
        }
        return a;
    }

    @Override
    public String getName() {
        return "gethypixelbooster";
    }

    @Override
    public String getCategory() {
        return "otherinfo";
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
