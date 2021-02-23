package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import okhttp3.*;
import okio.Okio;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class RemoveBackgroundCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws Exception {
        if (!ctx.getMessage().getAttachments().get(0).isImage()) {
            ctx.getChannel().sendMessage("您上傳了不是圖片的檔案！").queue();
            return;
        }

        if (!ctx.getMessage().getAttachments().get(0).getFileName().endsWith(".jfif")
                && !ctx.getMessage().getAttachments().get(0).getFileName().endsWith(".pjpeg")
                && !ctx.getMessage().getAttachments().get(0).getFileName().endsWith(".jpeg")
                && !ctx.getMessage().getAttachments().get(0).getFileName().endsWith(".pjp")
                && !ctx.getMessage().getAttachments().get(0).getFileName().endsWith(".jpg")
                && !ctx.getMessage().getAttachments().get(0).getFileName().endsWith(".png")) {
            ctx.getChannel().sendMessage("抱歉目前僅支援jfif、pjpeg、jpeg、pjp、jpg、png！").queue();
            return;
        }

        OkHttpClient client = new OkHttpClient();
        JSONObject jsonObject = new JSONObject().put("image_url", ctx.getMessage().getAttachments().get(0).getUrl()).put("size", "auto");
        Request request = new Request.Builder()
                .url("https://api.remove.bg/v1.0/removebg")
                .addHeader("X-Api-Key","zG1AUAC1V7ujAP2daVjAC24f")
                .post(RequestBody.create(MediaType.parse("application/json"),jsonObject.toString()))
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            File file = new File("tmp.jpg");
            response.body().source().readAll(Okio.sink(file));
            ctx.getChannel().sendFile(file).queue();
            file.deleteOnExit();
        }
    }

    @Override
    public String getName() {
        return "removebackground";
    }

    @Override
    public String getCategory() {
        return "nocategory";
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
