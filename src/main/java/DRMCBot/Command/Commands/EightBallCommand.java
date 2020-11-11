package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.util.List;

public class EightBallCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws Exception {
        List<String> args = ctx.getArgs();
        boolean isempty = args.isEmpty();
        String contentraw = ctx.getMessage().getContentRaw();
        boolean endwith = !(contentraw.endsWith("?") || contentraw.endsWith("？"));
        if (isempty || endwith) {
            ctx.getChannel().sendMessage("請輸入問題！").queue();
            return;
        }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://nekos.life/api/v2/8ball").build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            ctx.getChannel().sendMessage("伺服器有點問題，請稍後再試！").queue();
            return;
        }
        JSONObject jsonObj = new JSONObject(response.body().string());
        ctx.getChannel().sendMessage(
                EmbedUtils.defaultEmbed()
                        .setAuthor(
                                "Magic \uD83C\uDFB1",
                                ctx.getJDA().getInviteUrl(),
                                ctx.getAuthor().getEffectiveAvatarUrl())
                        .setDescription("❓: " + String.join(" ", args) + "\nℹ: " + jsonObj.getString("response"))
                        .setImage(jsonObj.getString("url"))
                        .build()).queue();
    }

    @Override
    public String getName() {
        return "8ball";
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
