package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.Utils.Paginator;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CoronaVirusDataCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {
        List<String> args = ctx.getArgs();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.covid19api.com/summary")
                .addHeader("X-Access-Token", "5cf9dfd5-3449-485e-b5ae-70a60e997864")
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            ctx.getChannel().sendMessage("資料取得失敗！").queue();
            return;
        }
        JSONObject responseJson = new JSONObject(response.body().string());
        JSONObject globalJson = responseJson.getJSONObject("Global");
        JSONArray countries = responseJson.getJSONArray("Countries");
        if (args.isEmpty()) {
            List<EmbedBuilder> embeds = new LinkedList<>();
            int countryPerPage = 10;
            int toDoTimes = (countries.length() % countryPerPage != 0 ? (countries.length() / countryPerPage) + 1 : countries.length() / countryPerPage);
            for (int i = 0; i < toDoTimes; i++) {
                EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                        .setAuthor("（頁" + (i + 1) + "/" + toDoTimes + "）－總共國家數：" + countries.length())
                        .setTitle("COVID-19世界數據：")
                        .setDescription(
                                "**更新時間：**" + formatTime(responseJson.getString("Date")) + "\n"
                                        + "總個案數：" + globalJson.getInt("TotalConfirmed") + "\n"
                                        + "新個案數：+" + globalJson.getInt("NewConfirmed") + "\n"
                                        + "死亡人數：" + globalJson.getInt("TotalDeaths") + "\n"
                                        + "新死亡數：+" + globalJson.getInt("NewDeaths") + "\n"
                                        + "總康復數：" + globalJson.getInt("TotalRecovered") + "\n"
                                        + "新康復數：+" + globalJson.getInt("NewRecovered") + "\n\n"
                                        + "輸入包含國家參數的指令來顯示關於該國家之資訊"
                        );
                for (int j = i * 10; j < (Math.min((i * 10) + countryPerPage, countries.length())); j++) {
                    JSONObject country = (JSONObject) countries.get(j);
                    embed.addField((j + 1) + ". :flag_" + country.getString("CountryCode").toLowerCase() + ": " + country.getString("Country"),
                            "**總案件數：**" + country.getInt("TotalConfirmed") + " | **今日：**" + country.getInt("NewConfirmed") + " | **康復：**" + country.getInt("TotalRecovered") + " | **死亡：**" + country.getInt("TotalDeaths"),
                            false
                    );
                }
                embeds.add(embed);
            }
            ctx.getChannel().sendMessage("成功！").queue();
            new Paginator(embeds, 20, ctx.getChannel()).start();
        } else {
            JSONObject toFind = new JSONObject();
            for (Object object : countries) {
                if (String.join(" ", args).equalsIgnoreCase(((JSONObject) object).getString("Country"))
                        || String.join(" ", args).equalsIgnoreCase(((JSONObject) object).getString("Slug").replaceAll("-", " "))) {
                    toFind = (JSONObject) object;
                    break;
                }
            }
            if (!toFind.isEmpty()) {
                EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                        .setTitle(toFind.getString("Country") + "確診個案")
                        .addField("總個案數", String.valueOf(toFind.getInt("TotalConfirmed")), true)
                        .addField("新個案數", String.valueOf(toFind.getInt("NewConfirmed")), true)
                        .addField("正確診中", String.valueOf(toFind.getInt("TotalConfirmed") - toFind.getInt("TotalDeaths") - toFind.getInt("TotalRecovered")), true)
                        .addField("總共死亡", String.valueOf(toFind.getInt("TotalDeaths")), true)
                        .addField("新增死亡", String.valueOf(toFind.getInt("NewDeaths")), true)
                        .addField("總康復數", String.valueOf(toFind.getInt("TotalRecovered")), true)
                        .addField("新康復數", String.valueOf(toFind.getInt("NewRecovered")), true)
                        .setThumbnail("https://flagcdn.com/w160/" + toFind.getString("CountryCode").toLowerCase() + ".png");
                ctx.getChannel().sendMessage(embed.build()).queue();
            } else {
                ctx.getChannel().sendMessage("找不到指定國家！").queue();
            }
        }
    }

    private String formatTime(String inputTime) {
        ZonedDateTime time = OffsetDateTime.parse(inputTime).toZonedDateTime().withZoneSameInstant(ZoneId.of("Asia/Taipei"));
        return time.getYear() + "/" + getTwoDigit(time.getMonthValue()) + "/" + getTwoDigit(time.getDayOfMonth()) + " " + getTwoDigit(time.getHour()) + ":" + getTwoDigit(time.getMinute()) + ":" + getTwoDigit(time.getSecond());
    }

    private String getTwoDigit(int input) {
        return String.valueOf(input).length() == 1 ? "0" + input : "" + input;
    }

    @Override
    public String getName() {
        return "covid";
    }

    @Override
    public String getCategory() {
        return "otherinfo";
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
