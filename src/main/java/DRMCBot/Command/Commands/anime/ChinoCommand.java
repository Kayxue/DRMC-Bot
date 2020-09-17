package DRMCBot.Command.Commands.anime;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;

public class ChinoCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://www.pixiv.net/ajax/search/artworks/%E9%A6%99%E9%A2%A8%E6%99%BA%E4%B9%83?word=%E9%A6%99%E9%A2%A8%E6%99%BA%E4%B9%83&order=date_d&mode=all&p=1&s_mode=s_tag&type=all&lang=zh_tw")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36")
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            JSONObject jsonObject = new JSONObject(response.body().string());
            System.out.println(jsonObject.toString(2));
            ctx.getChannel().sendMessage("取得完成！").queue();
        }
    }

    @Override
    public String getName() {
        return "chino";
    }
}
