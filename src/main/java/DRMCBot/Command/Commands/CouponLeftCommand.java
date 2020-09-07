package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import com.github.natanbc.reliqua.util.StatusCodeValidator;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.JSONObject;

import java.util.function.Consumer;

public class CouponLeftCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {

        if (ctx.getArgs().isEmpty()) {
            ctx.getChannel().sendMessage("請輸入查詢之郵局！").queue();
        }
        String input = ctx.getArgs().get(0);

        getdata(input,output -> {
            output = output.replaceAll("<br>", " ");
            JSONObject jsonObject = new JSONObject(output);
            try {
                ctx.getChannel().sendMessage((CharSequence) jsonObject.get("error")).queue();
            } catch (Exception e) {
                EmbedBuilder embedBuilder = EmbedUtils.defaultEmbed();
            }
        });

    }

    public void getdata(String input, Consumer<String> output) {
        WebUtils.ins.getJSONArray("https://3000.gov.tw/hpgapi-openmap/api/getPostData",
                (builder) -> builder.setStatusCodeValidator(StatusCodeValidator.ACCEPT_200)
        ).async(
                (json) -> json.forEach((item)->{
                    if (item.get("storeNm").asText().equals(input)) {
                        output.accept(item.toString());
                    }
                }),
                (error) -> {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.append("error", error.getMessage());
                    output.accept(jsonObject.toString());
                }
        );
    }

    @Override
    public String getName() {
        return "couponleft";
    }
}
