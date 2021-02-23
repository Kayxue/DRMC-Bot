package DRMCBot.Command.Commands.reurl;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import okhttp3.*;

import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class TinyurlCommand implements ICommand {
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

            Request request = new Request.Builder()
                    .url("http://tinyurl.com/api-create.php?url="+url)
                    .build();

            Response response = client.newCall(request).execute();

            String requestbody = response.body().string();
            if (requestbody.equals("Error")) {
                ctx.getChannel().sendMessage("請輸入有效連結！").queue();
            } else {
                EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                        .setTitle("轉短連結完成！")
                        .setDescription("感謝您使用此機器人服務！")
                        .addField("縮短後的連結：", requestbody, false)
                        .setFooter("感謝tinyurl.com提供此原服務！","https://lh3.googleusercontent.com/aG5YTLGKdPW1BkDuR9COXjaXsVDvabEJ97HSyyIdKKdSr-FQly---VoAAnvaAEMzuhY");

                ctx.getChannel().sendMessage(embed.build()).queue();
            }
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
        return "tinyurl";
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
