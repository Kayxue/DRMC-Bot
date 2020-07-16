package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.natanbc.reliqua.util.StatusCodeValidator;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;

public class CouponLeftCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        String input = ctx.getArgs().get(0);
        WebUtils.ins.getJSONArray("https://3000.gov.tw/hpgapi-openmap/api/getPostData",
                (builder) -> builder.setStatusCodeValidator(StatusCodeValidator.ACCEPT_200)
        ).async(
                (json) -> {
                    json.forEach((item)->{
                        if (item.get("storeNm").asText().equals(input)) {
                            EmbedBuilder embedBuilder= EmbedUtils.defaultEmbed()
                                    .setTitle("三倍劵查詢測試")
                                    .addField("hsnCd",item.get("hsnCd").asText(),false);
                            ctx.getChannel().sendMessage(embedBuilder.build()).queue();
                            return;
                        }
                    });
                },
                (error) -> {
                    ctx.getChannel().sendMessage("失敗").queue();
                }
        );
    }

    @Override
    public String getName() {
        return "couponleft";
    }
}
