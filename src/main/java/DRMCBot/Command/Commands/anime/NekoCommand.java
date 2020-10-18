package DRMCBot.Command.Commands.anime;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

public class NekoCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws Exception{
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://neko-love.xyz/api/v1/neko")
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            ctx.getChannel().sendMessage("伺服器有點問題，請稍後再試！").queue();
            return;
        }
        JSONObject jsonObject = new JSONObject(response.body().string());
        if (jsonObject.getInt("code") == 200) {
            EmbedBuilder embed = EmbedUtils.embedImage(jsonObject.getString("url"));
            ctx.getChannel().sendMessage(embed.build()).queue();
        } else {
            ctx.getChannel().sendMessage("伺服器有點問題，請稍後再試！").queue();
        }
    }

    @Override
    public String getName() {
        return "neko";
    }
}
