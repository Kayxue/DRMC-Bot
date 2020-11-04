package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SourceBinCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws Exception {
        /*
        Return :
        -2:can't get linguist file or parse error
        -1:not defined language
         */
        System.out.println(checkIsDefinedLanguage("js"));
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
        Iterator<String> stringIterator = linguistJson.keys();
        stringIterator.forEachRemaining(e -> System.out.println(e));

        for (String languagename : linguistJson.keySet()) {
            if (!linguistJson.getJSONObject(languagename).keySet().contains("extensions")) {
                linguistJson.remove(languagename);
            }

            /*
            List<String> extensions = Collections.singletonList(linguistJson.getJSONObject(languagename).get("extensions").toString());
            List<String> aliases = Collections.singletonList(linguistJson.getJSONObject(languagename).get("aliases").toString());
            extensions.forEach(x -> x = x.substring(1));
            if (language.equals(languagename) || extensions.contains(language) || aliases.contains(language)) {
                return linguistJson.getJSONObject(languagename).getLong("language_id");
            }
            */

        }


        return -1;
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
