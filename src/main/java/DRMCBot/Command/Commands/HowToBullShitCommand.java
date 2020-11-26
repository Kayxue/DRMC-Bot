package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import okhttp3.*;
import org.json.JSONObject;

import java.util.List;

public class HowToBullShitCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws Exception {
        List<String> args = ctx.getArgs();
        if (args.isEmpty() || args.size() < 2) {
            ctx.getChannel().sendMessage("請輸入足夠參數！").queue();
            return;
        }
        try {
            int length = Integer.parseInt(args.get(0));
            if (length > 1000) {
                ctx.getChannel().sendMessage("字數不能大於1000！").queue();
                return;
            }
        } catch (Exception e) {
            ctx.getChannel().sendMessage("請輸入文章長度！").queue();
            return;
        }
        String contentRaw = ctx.getMessage().getContentRaw();
        int wherevalueis = contentRaw.indexOf(args.get(0));
        String topic = contentRaw.substring(wherevalueis + args.get(0).length()).trim();
        JSONObject jsonObject = new JSONObject().put("Topic", topic).put("MinLen", Integer.parseInt(args.get(0)));
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.howtobullshit.me/bullshit")
                .addHeader("charset", "utf-8")
                .post(RequestBody.create(MediaType.parse("application/json"),jsonObject.toString()))
                .build();
        Response response = httpClient.newCall(request).execute();
        String respond=response.body().string().replaceAll("&nbsp;","").replaceAll("<br>","\n");
        EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                .setTitle("唬爛產生器")
                .setDescription("```" + respond + "```")
                .setFooter("感謝Bill Hsu製作唬爛產生器網頁\n原唬爛產生器網址：https://howtobullshit.me/","https://avatars3.githubusercontent.com/u/28007209?s=460&u=8d1eb0ced119617cf37fb983bc5998a804d99e57&v=4");
        ctx.getChannel().sendMessage(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "hulan";
    }

    @Override
    public String getCategory() {
        return "generation";
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
