package DRMCBot.Command.Commands.anime;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Okio;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static DRMCBot.CacheList.ChinoCommandInCooldown;

public class ChinoCommand implements ICommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChinoCommand.class);

    @Override
    public void handle(CommandContext ctx) throws IOException {
        String userId = ctx.getAuthor().getId();
        if (ChinoCommandInCooldown.containsKey(userId)) {
            ctx.getChannel().sendMessage("您正在冷卻中！冷卻倒數："+ChinoCommandInCooldown.get(userId)+"秒").queue();
            LOGGER.info(ctx.getAuthor().getAsTag() + "正在冷卻中");
            return;
        }
        LOGGER.info("即將對" + ctx.getAuthor().getAsTag() + "發送圖片");
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
            //System.out.println(illustManga.length());
            //System.out.println(illustManga.toString(2));
            JSONObject choosed = illustManga.getJSONObject(random.nextInt(59));
            //System.out.println(choosed.toString(2));

            Request request = new Request.Builder()
                    .url("https://www.pixiv.net/ajax/illust/" + choosed.getString("id"))
                    .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36")
                    .build();
            Response getillustinforesponse = client.newCall(request).execute();
            if (getillustinforesponse.isSuccessful()) {
                JSONObject maindata = new JSONObject(getillustinforesponse.body().string()).getJSONObject("body");

                System.out.println(maindata.toString(2));

                String artworkwebsiteurl = "https://www.pixiv.net/artworks/" + maindata.getString("illustId");

                String pictureurl = maindata.getJSONObject("urls").getString("original");

                //System.out.println(pictureurl);

                Request pictureRequest = new Request.Builder()
                        .url(pictureurl)
                        .addHeader("referer", " https://www.pixiv.net/")
                        .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36")
                        .build();

                Response pictureResponse = client.newCall(pictureRequest).execute();
                if (!pictureResponse.isSuccessful()) {
                    ctx.getChannel().sendMessage("失敗！若問題持續發生，請向New DL/RS/MC Chatroom請求協助！").queue();
                }
                File outputFile = new File("chino.png");
                pictureResponse.body().source().readAll(Okio.sink(outputFile));

                EmbedBuilder embedBuilder = EmbedUtils.getDefaultEmbed()
                        .setTitle(maindata.getString("title"), artworkwebsiteurl)
                        .setImage("attachment://chino.png");

                System.out.println(pictureurl);

                ctx.getChannel().sendMessage(embedBuilder.build()).addFile(outputFile, "chino.png").queue();
                outputFile.deleteOnExit();
            }
        }
        ChinoCommandInCooldown.put(userId, 60);
        new Thread(() -> {
            while (ChinoCommandInCooldown.get(userId) > 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ChinoCommandInCooldown.put(userId, ChinoCommandInCooldown.get(userId) - 1);
            }
            ChinoCommandInCooldown.remove(userId);
        }).start();
    }

    @Override
    public String getName() {
        return "chino";
    }

    @Override
    public String getCategory() {
        return "entertainment";
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
