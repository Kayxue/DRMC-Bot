package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WeatherCommand implements ICommand {

    final ArrayList<String> searchType = new ArrayList<>(Arrays.asList("current","forecast"));


    @Override
    public void handle(CommandContext ctx) throws Exception {
        List<String> args = ctx.getArgs();
        if (args.isEmpty()||args.size() < 2) {
            ctx.getChannel().sendMessage("請輸入足夠參數！").queue();
            return;
            //drmc!accuweather current (detailed) taipei city
        }
        if (!searchType.contains(args.get(0))) {
            ctx.getChannel().sendMessage("請指定顯示資訊的類型！").queue();
            return;
        }
        boolean ifDetailed;
        String contentRaw = ctx.getMessage().getContentRaw();
        String secondArgument;
        int index;
        if (ctx.getArgs().get(1).equals("detailed")) {
            ifDetailed = true;
            secondArgument = ctx.getArgs().get(1);
        } else {
            ifDetailed = false;
            secondArgument = ctx.getArgs().get(0);
        }
        index = contentRaw.indexOf(secondArgument) + secondArgument.length();
        String toSearch = contentRaw.substring(index).trim();
        boolean chineseInTheString=isChinese(toSearch);
        toSearch = toSearch.replaceAll(" ", "%20");


        final String APIKEY = "kyC6nwcgGXn1einkJt7pyvIOnY8wkllc";
        OkHttpClient client = new OkHttpClient();
        String languageToSearch = chineseInTheString ? "zh-tw" : "en-us";
        Request citySearchRequest = new Request.Builder()
                .url("http://dataservice.accuweather.com/locations/v1/cities/search?apikey="+APIKEY+"&q="+toSearch+"&language="+languageToSearch)
                .build();

        Response searchCityIdResponse = client.newCall(citySearchRequest).execute();
        JSONArray citySearchResult=new JSONArray(searchCityIdResponse.body().string());
        if (citySearchResult.length() == 0) {
            ctx.getChannel().sendMessage("對不起！找不到您要的地方！").queue();
            return;
        }

        JSONObject firstCityResult = (JSONObject) citySearchResult.get(0);
        String cityName = firstCityResult.getString("LocalizedName");
        String cityId = firstCityResult.getString("Key");
        if (args.get(0).equals("current")) {
            Request weatherDataRequest = new Request.Builder()
                    .url("http://dataservice.accuweather.com/currentconditions/v1/" + cityId + "?apikey=" + APIKEY + "&language=zh-tw&details=true")
                    .build();

            Response weatherDataResponse = client.newCall(weatherDataRequest).execute();
            JSONArray searchWeatherDataResult = new JSONArray(weatherDataResponse.body().string());
            JSONObject weatherDataResult = (JSONObject) searchWeatherDataResult.get(0);
            if (!ifDetailed) {
                EmbedBuilder embed = EmbedUtils.defaultEmbed()
                        .setTitle(cityName + "現在天氣")
                        .addField("目前天氣：", weatherDataResult.getString("WeatherText"), false)
                        .addField("目前溫度：", weatherDataResult.getJSONObject("Temperature").getJSONObject("Metric").getInt("Value") + "\u2103", false)
                        .addField("目前濕度：",weatherDataResult.getInt("RelativeHumidity")+"%",false)
                        .addField("目前UV指數：",String.valueOf(weatherDataResult.getInt("UVIndex")),false)
                        .addField("目前是否有降雨：",!weatherDataResult.getBoolean("HasPrecipitation")?"無":weatherDataResult.getString("PrecipitationType"),false)
                        .setFooter("資料提供者：AccuWeather (https://www.accuweather.com/)\n資料更新時間："+weatherDataResult.getString("LocalObservationDateTime").split("T")[0]+" "+weatherDataResult.getString("LocalObservationDateTime").split("T")[1].substring(0,8),"https://lh3.ggpht.com/7BB1gD1EJ9g2mcqHfAtMuP0Z5Zg1a1syl4l8GTGIXFUUUpTSbg_txXw99YAVUZ9B8A=h300");
                ctx.getChannel().sendMessage(embed.build()).queue();
            } else {
                EmbedBuilder embed = EmbedUtils.defaultEmbed()
                        .setTitle(cityName + "現在天氣")
                        .addField("目前天氣：", weatherDataResult.getString("WeatherText"), false)
                        .addField("目前溫度：", weatherDataResult.getJSONObject("Temperature").getJSONObject("Metric").getInt("Value") + "\u2103", false)
                        .addField("目前濕度：",weatherDataResult.getInt("RelativeHumidity")+"%",false)
                        .addField("目前UV指數：",String.valueOf(weatherDataResult.getInt("UVIndex")),false)
                        .addField("目前是否有降雨：",!weatherDataResult.getBoolean("HasPrecipitation")?"無":weatherDataResult.getString("PrecipitationType"),false)
                        .setFooter("資料提供者：AccuWeather (https://www.accuweather.com/)\n資料更新時間："+weatherDataResult.getString("LocalObservationDateTime").split("T")[0]+" "+weatherDataResult.getString("LocalObservationDateTime").split("T")[1].substring(0,8),"https://lh3.ggpht.com/7BB1gD1EJ9g2mcqHfAtMuP0Z5Zg1a1syl4l8GTGIXFUUUpTSbg_txXw99YAVUZ9B8A=h300");
                ctx.getChannel().sendMessage(embed.build()).queue();
            }
        }
    }

    public static boolean isChinese(char c) {
        return c >= 0x4E00 &&  c <= 0x9FA5;// 根據位元組碼判斷
    }
    // 判斷一個字串是否含有中文
    public static boolean isChinese(String str) {
        if (str == null) return false;
        for (char c : str.toCharArray()) {
            if (isChinese(c)) return true;// 有一箇中文字元就返回
        }
        return false;
    }

    @Override
    public String getName() {
        return "accuweather";
    }
}
