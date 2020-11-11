package DRMCBot.Command.Commands.reurl;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import okhttp3.*;
import org.json.JSONObject;

import java.net.URL;
import java.util.List;

public class ReurlCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws Exception {
        List<String> args = ctx.getArgs();

        if (args.isEmpty()) {
            ctx.getChannel().sendMessage("請輸入有效連結！").queue();
            return;
        }

        String url=ctx.getArgs().get(0);

        if (isUrl(url)) {
            OkHttpClient client = new OkHttpClient();
            JSONObject jsonObject = new JSONObject()
                    .put("url", url)
                    .put("utm_source","FB_AD");

            Request request = new Request.Builder()
                    .url("https://api.reurl.cc/shorten")
                    .addHeader("reurl-api-key","4070ff49d794e33116563b663c974755ecd3b431949d04df8a38b58d65165567c4f5d6")
                    .post(RequestBody.create(MediaType.parse("application/json"),jsonObject.toString()))
                    .build();

            Response response = client.newCall(request).execute();
            JSONObject jsonrequest = new JSONObject(response.body().string());
            try {
                String errorcode = jsonrequest.getString("msg");
                if (errorcode.equals("request url failed")) {
                    ctx.getChannel().sendMessage("網址連線失敗，請確認是否為有效網址或稍後再試！").queue();
                }
                else {
                    ctx.getChannel().sendMessage("指令遇到錯誤！\n網頁回應訊息為：" + errorcode + "若問題持續發生請先改用其他url生成服務！").queue();
                }
            } catch (Exception e) {
                EmbedBuilder embed = EmbedUtils.defaultEmbed()
                        .setTitle("轉短連結完成！")
                        .setDescription("感謝您使用此機器人服務！")
                        .addField("縮短後的連結：", jsonrequest.getString("short_url"), false)
                        .setFooter("感謝reurl.cc提供此原服務！","https://lh3.googleusercontent.com/MXk9tuquBOx_jIH7z7g6plnivJFne63xlhzOwhmzHP4_Cr74ByCPMNS3LqV4UqAAhAntpZoR=w128-h128-e365-rj-sc0x00ffffff");

                ctx.getChannel().sendMessage(embed.build()).queue();
            }
        } else {
            ctx.getChannel().sendMessage("請輸入有效連結！").queue();
        }
    }

    public Boolean isUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getName() {
        return "reurl";
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
