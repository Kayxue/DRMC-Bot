package DRMCBot.Command.Commands.code;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import okhttp3.*;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class PastemystCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws Exception {
        final List<String> args = ctx.getArgs();
        if (args.isEmpty() || args.size() < 2) {
            ctx.getChannel().sendMessage("請輸入足夠參數！").queue();
            return;
        }
        String languageName;
        OkHttpClient client = new OkHttpClient();
        final String messageContent = ctx.getMessage().getContentRaw();

        Request request = new Request.Builder()
                .url("https://paste.myst.rs/api/v2/data/languageExt?extension=" + args.get(0))
                .build();
        Response getDefineLanguageResponse = client.newCall(request).execute();
        JSONObject languageDetail;
        if (getDefineLanguageResponse.code() == 404) {
            languageName = "Plain Text";
            getDefineLanguageResponse.close();
        } else {
            languageDetail = new JSONObject(getDefineLanguageResponse.body().string());
            languageName = languageDetail.getString("name");
        }

        String pasteTitle = ctx.getAuthor().getAsTag() + "'s Paste";
        JSONObject toPaste = new JSONObject()
                .put("title", pasteTitle)
                .put("expiresIn", "never");

        String codeToPaste = messageContent.substring(messageContent.indexOf(args.get(1)));
        JSONObject pasties = new JSONObject()
                .put("language", languageName)
                .put("title", "The Paste")
                .put("code", codeToPaste);
        toPaste.append("pasties", pasties);

        Request pasteRequest = new Request.Builder()
                .url("https://paste.myst.rs/api/v2/paste")
                .post(RequestBody.create(MediaType.parse("application/json"), toPaste.toString()))
                .build();
        Response pasteResponse = client.newCall(pasteRequest).execute();

        if (!pasteResponse.isSuccessful()) {
            ctx.getChannel().sendMessage("失敗").queue();
            return;
        } /*else {
            ctx.getChannel().sendMessage("成功").queue();
        }
        */

        JSONObject pasteReturn = new JSONObject(pasteResponse.body().string());

        String pasteUrl = "https://paste.myst.rs/" + pasteReturn.getString("_id");

        EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                .setColor(0xe8732b)
                .setAuthor("PasteMyst", "https://paste.myst.rs/", "https://d2.alternativeto.net/dist/icons/pastemyst_166483.jpg?width=64&height=64&mode=crop&upscale=false")
                .setTitle(pasteTitle)
                .addField("連結", pasteUrl, false)
                .addField("程式碼", "```" + (languageName.equals("Plain Text") ? "" : args.get(0) + "\n") + codeToPaste + "```", false);

        ctx.getChannel().sendMessage(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "pastemyst";
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
