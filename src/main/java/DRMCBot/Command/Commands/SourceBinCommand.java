package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SourceBinCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws Exception {
        /*
        Return :
        -2:can't get linguist file or parse error
        372:text
        */
        System.out.println(checkIsDefinedLanguage("rs"));
    }

    public long checkIsDefinedLanguage(String language) {
        JSONObject linguistJson;
        try {
            linguistJson = getLinguist();
        } catch (Exception e) {
            e.printStackTrace();
            return -2;
        }
        if (linguistJson == null) {
            return -2;
        }
        System.out.println(linguistJson.keySet());
        System.out.println(linguistJson.toString(4));
        for (String languagename : linguistJson.keySet()) {

            List<String> extensions=new LinkedList<>();
            List<String> aliases=new LinkedList<>();

            try {
                String[] extensionarray = linguistJson.getJSONObject(languagename).get("extensions").toString()
                        .replaceAll("\\[", "")
                        .replaceAll("\\]", "")
                        .replaceAll("\"", "")
                        .split(",");
                extensions = Arrays.asList(extensionarray);
            } catch (Exception e) {
                extensions.add("");
            }
            System.out.println(extensions.contains("js"));
            System.out.println("-------------");
            System.out.println(languagename);
            extensions.forEach(e -> System.out.println(e));
            System.out.println(extensions.size());
            try {
                String[] aliasarray = linguistJson.getJSONObject(languagename).get("aliases").toString()
                        .replaceAll("\\[", "")
                        .replaceAll("\\]", "")
                        .replaceAll("\"", "")
                        .split(",");
                aliases = Arrays.asList(aliasarray);
            } catch (Exception e) {
                aliases.add("");
            }
            aliases.forEach(e -> System.out.println(e));
            System.out.println(aliases.size());
            System.out.println("-------------");
            if (language.equalsIgnoreCase(languagename) || extensions.contains("." + language) || aliases.contains(language)) {
                return linguistJson.getJSONObject(languagename).getLong("language_id");
            }
        }
        return 372;
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
        return "sourcebin";
    }
}
