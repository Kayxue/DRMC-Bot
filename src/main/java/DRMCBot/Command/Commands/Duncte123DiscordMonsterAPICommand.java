package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

public class Duncte123DiscordMonsterAPICommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws Exception {
        /*First way:Use WebUtils*/
        WebUtils.ins.getJSONObject("https://apis.duncte123.me/animal/discord-monster").async(
                json -> {
                    EmbedBuilder embed = EmbedUtils.embedImageWithTitle("編號id：" + json.get("data").get("id").asInt(), null, json.get("data").get("file").asText());
                    ctx.getChannel().sendMessage(embed.build()).queue();
                },
                error -> error.printStackTrace()
        );
        /*---------------------------*/

        /*Second way:Use OkHttpClient*/
        /*
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://apis.duncte123.me/animal/discord-monster")
                .build();
        Response response = client.newCall(request).execute();
        JSONObject jsonObject = new JSONObject(response.body().string());
        if (response.isSuccessful()) {
            EmbedBuilder embed = EmbedUtils.embedImageWithTitle("編號id：" + jsonObject.getJSONObject("data").getInt("id"), null, jsonObject.getJSONObject("data").getString("file"));
            ctx.getChannel().sendMessage(embed.build()).queue();
        }
        */
        /*---------------------------*/
    }

    @Override
    public String getName() {
        return "dcmonster";
    }
}
