package DRMCBot.Command.Commands.code;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import okhttp3.*;
import org.json.JSONObject;

import java.io.File;
import java.util.*;

public class SourceBinCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws Exception {

        List<String> args = ctx.getArgs();
        if (args.isEmpty() || args.size() < 2) {
            ctx.getChannel().sendMessage("請輸入足夠參數！").queue();//send not enough arguments message
            return;
        }

        /*
        Return :
        -2:can't get linguist file or parse error
        372:text
        */
        long languageid = checkIsDefinedLanguage(args.get(0));

        if (languageid == -2) {
            ctx.getChannel().sendMessage("無法取得linguist.yaml或轉換成Json失誤！");//send get linguist.yaml or parse linguist.yaml to json error message
        }

        String messagecontent = ctx.getMessage().getContentRaw();

        if (languageid != -2) {
            ArrayList<JSONObject> parsedBins = new ArrayList<>();
            String binname = ctx.getAuthor().getName();
            JSONObject parsedBin = new JSONObject()
                    .put("content", messagecontent.substring(messagecontent.indexOf(ctx.getArgs().get(1))))
                    .put("languageId", languageid);
            parsedBins.add(parsedBin);

            final JSONObject binObject = new JSONObject()
                    .put("files", parsedBins.toArray())
                    .put("title", binname)
                    .put("description", "A bin posted by " + binname);

            OkHttpClient client = new OkHttpClient();

            Request postrequest = new Request.Builder()
                    .url("https://sourceb.in/api/bins/")
                    .post(RequestBody.create(MediaType.parse("application/json"), binObject.toString()))
                    .build();

            Response postresponse = client.newCall(postrequest).execute();

            if (postresponse.isSuccessful()) {
                //System.out.println("Posted finished!");
                //System.out.println(postresponse.body().string());
                JSONObject postrequestjson = new JSONObject(postresponse.body().string());
                File icon = new File("sourcebin.png");
                EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
                        .setAuthor("SourceBin", "https://sourceb.in/", "attachment://icon.png")
                        .setTitle(postrequestjson.getString("key"), "https://sourceb.in/" + postrequestjson.getString("key"))
                        .setDescription("```" + (languageid != 372 ? ctx.getArgs().get(0) : "") + "\n" + messagecontent.substring(messagecontent.indexOf(ctx.getArgs().get(1))) + "```")
                        .setColor(0xfb5756);
                ctx.getChannel().sendMessageEmbeds(builder.build()).addFile(icon, "icon.png").queue();
            } else {
                System.out.println(postresponse.message());
            }
        } else {
            ctx.getChannel().sendMessage("程式語言查詢伺服器出錯！").queue();
        }
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
        /*
        System.out.println(linguistJson.keySet());
        System.out.println(linguistJson.toString(4));
         */
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
            /*
            System.out.println(extensions.contains("js"));
            System.out.println("-------------");
            System.out.println(languagename);
            extensions.forEach(e -> System.out.println(e));
            System.out.println(extensions.size());

             */
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
            /*
            aliases.forEach(e -> System.out.println(e));
            System.out.println(aliases.size());
            System.out.println("-------------");
             */
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
