package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;

import java.text.DecimalFormat;

public class ExchangeCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws Exception {
        if (ctx.getArgs().isEmpty() || ctx.getArgs().size() < 3) {
            ctx.getChannel().sendMessage("請輸入參數！").queue();
            return;
        }
        WebUtils.ins.getJSONObject("https://tw.rter.info/capi.php").async(
                json -> {
                    String from = ctx.getArgs().get(0).toUpperCase();
                    String to = ctx.getArgs().get(1).toUpperCase();
                    String stringdollar = ctx.getArgs().get(2);
                    double dollar;
                    try {
                        dollar = Double.parseDouble(stringdollar);
                    } catch (NumberFormatException e) {
                        ctx.getChannel().sendMessage("請輸入金額！").queue();
                        return;
                    }

                    double output;
                    DecimalFormat formator = new DecimalFormat("0.00");


                    if (json.get("USD" + from) == null) {
                        ctx.getChannel().sendMessage("找不到該貨幣單位！").queue();
                        return;
                    }

                    if (json.get("USD" + to) == null) {
                        ctx.getChannel().sendMessage("找不到該貨幣單位！").queue();
                        return;
                    }

                    if (from.equalsIgnoreCase(to)) {
                        ctx.getChannel().sendMessage("單位相同，不用轉換！").queue();
                        return;
                    } else if ("USD".equalsIgnoreCase(from) || "USD".equalsIgnoreCase(to)) {
                        if ("USD".equalsIgnoreCase(from)) {
                            output =dollar * json.get("USD" + to).get("Exrate").asDouble();
                        } else {
                            output = dollar / json.get("USD" + from).get("Exrate").asDouble();
                        }
                    } else {
                        output = dollar / json.get("USD" + from).get("Exrate").asDouble();
                        output = output * json.get("USD" + to).get("Exrate").asDouble();
                    }
                    output = Double.parseDouble(formator.format(output));
                    EmbedBuilder embed = EmbedUtils.defaultEmbed()
                            .setTitle("貨幣換算")
                            .setDescription(dollar + " **" + from + "**" + " ➡ ️" + output + " **" + to + "**\n\n"
                                    + "> 今日匯率：1 " + from + " = " + formator.format((1 / json.get("USD" + from).get("Exrate").asDouble())
                                    * json.get("USD" + to).get("Exrate").asDouble()) + " " + to)
                            .setFooter("感謝即匯站提供匯率！\n更新時間：" + " " + json.get("USD" + from).get("UTC").asText());

                    ctx.getChannel().sendMessage(embed.build()).queue();
                },
                Throwable::printStackTrace
        );
    }

    @Override
    public String getName() {
        return "exchange";
    }
}
