package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class LinguistParseJsonTestCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws Exception {
        JSONObject linguistJson = getLinguist();
        if (linguistJson == null) {
            ctx.getChannel().sendMessage("轉換失敗！").queue();
        } else {
            ctx.getChannel().sendMessage("轉換成功！").queue();
            System.out.println(linguistJson.toString(4));
        }
    }

    public JSONObject getLinguist() throws Exception{
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://raw.githubusercontent.com/github/linguist/f75c5707a62a3d66501993116826f4e64c3ca4dd/lib/linguist/languages.yml")
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return new JSONObject(convertYamlToJson(response.body().string()));
        } else {
            return null;
        }
    }

    public String convertYamlToJson(String yaml) throws Exception{
        ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
        Object obj = yamlReader.readValue(yaml, Object.class);

        ObjectMapper jsonWriter = new ObjectMapper();
        return jsonWriter.writeValueAsString(obj);
    }

    @Override
    public String getName() {
        return "linguistparsejson";
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
