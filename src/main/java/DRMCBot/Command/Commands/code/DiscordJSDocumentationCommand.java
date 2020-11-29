package DRMCBot.Command.Commands.code;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.util.List;

import static DRMCBot.Utils.Utils.DiscordJSEmbedParser;

public class DiscordJSDocumentationCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws Exception {
        List<String> args = ctx.getArgs();
        if (args.isEmpty()) {
            ctx.getChannel().sendMessage("請輸入參數！").queue();
            return;
        }
        OkHttpClient client = new OkHttpClient();
        String url = "https://djsdocs.sorta.moe/v2/embed?src=master&q=" + String.join("%20", args);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.75 Safari/537.36")
                .build();
        Response response = client.newCall(request).execute();
        String responsebody = response.body().string();
        if (response.code() != 200 || responsebody.equals("null")) {
            ctx.getChannel().sendMessage("沒有符合的結果！").queue();
            return;
        }
        JSONObject Embeddata = new JSONObject(responsebody);
        EmbedBuilder embed = DiscordJSEmbedParser(Embeddata, false);
        ctx.getChannel().sendMessage(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "discordjs";
    }

    @Override
    public String getCategory() {
        return "code";
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
