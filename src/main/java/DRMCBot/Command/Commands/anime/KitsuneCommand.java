package DRMCBot.Command.Commands.anime;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class KitsuneCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws Exception{
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://neko-love.xyz/api/v1/kitsune")
                .build();
        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            JSONObject jsonObject = new JSONObject(response.body().string());
            EmbedBuilder embed = EmbedUtils.embedImage(jsonObject.getString("url"));
            ctx.getChannel().sendMessageEmbeds(embed.build()).queue();
        } else {
            ctx.getChannel().sendMessage("伺服器有點問題，請稍後再試！").queue();
        }
    }

    @Override
    public String getName() {
        return "kitsune";
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
    public List<String> getUsages() {
        return null;
    }

    @Override
    public List<String> getExamples() {
        return null;
    }

    @Override
    public HashMap<String, HashMap<String, String>> getArguments() {
        return null;
    }
}
