package DRMCBot.Command.Commands.anime;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import com.google.gson.JsonArray;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

public class ChinoCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Random random = new Random();
        Request getsearchpage = new Request.Builder()
                .url("https://www.pixiv.net/ajax/search/artworks/%E9%A6%99%E9%A2%A8%E6%99%BA%E4%B9%83?word=%E9%A6%99%E9%A2%A8%E6%99%BA%E4%B9%83&order=date_d&mode=all&p=" + random.nextInt(20) + "&s_mode=s_tag&type=all&lang=zh_tw")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36")
                .build();

        Response getpageresponse = client.newCall(getsearchpage).execute();
        if (getpageresponse.code() == 200) {
            JSONObject allpagedata = new JSONObject(getpageresponse.body().string());
            JSONArray illustManga = allpagedata.getJSONObject("body").getJSONObject("illustManga").getJSONArray("data");
            System.out.println(illustManga.length());
            System.out.println(illustManga.toString(2));
            JSONObject choosed = illustManga.getJSONObject(random.nextInt(59));
            System.out.println(choosed.toString(2));

            Request request = new Request.Builder()
                    .url("https://www.pixiv.net/ajax/illust/" + choosed.getString("id"))
                    .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36")
                    .build();
            Response getillustinforesponse = client.newCall(request).execute();
            if (getillustinforesponse.isSuccessful()) {
                JSONObject maindata = new JSONObject(getillustinforesponse.body().string()).getJSONObject("body");

                String artworkwebsiteurl = "https://www.pixiv.net/artworks/" + maindata.getString("illustId");

                String pictureurl = maindata.getJSONObject("urls").getString("original");



                EmbedBuilder embedBuilder = EmbedUtils.embedImage(pictureurl);

                System.out.println(pictureurl);

                ctx.getChannel().sendMessage(embedBuilder.build()).queue();

                ctx.getChannel().sendMessage("取得完成！").queue();
            }

        }
    }

    @Override
    public String getName() {
        return "chino";
    }
}
