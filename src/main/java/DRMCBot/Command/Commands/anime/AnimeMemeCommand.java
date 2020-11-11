package DRMCBot.Command.Commands.anime;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

public class AnimeMemeCommand implements ICommand {
    OkHttpClient client = new OkHttpClient();
    @Override
    public void handle(CommandContext ctx) throws Exception {
        Request request = new Request.Builder().url("https://nekos.life/api/v2/img/gecg").build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            EmbedBuilder embed = EmbedUtils.embedImage(new JSONObject(response.body().string()).getString("url"));
            ctx.getChannel().sendMessage(embed.build()).queue();
        } else {
            ctx.getChannel().sendMessage("抱歉伺服器出錯了！").queue();
        }
    }

    @Override
    public String getName() {
        return "animememe";
    }

    @Override
    public String getCategory() {
        return null;
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
