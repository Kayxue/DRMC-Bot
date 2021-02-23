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
import java.util.HashMap;
import java.util.List;

public class AccuWeatherCommand implements ICommand {

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
        boolean ifDetailed = true;
        String contentRaw = ctx.getMessage().getContentRaw();
        int index;
        ifDetailed = "detailed".equals(ctx.getArgs().get(1));
        System.out.println(ifDetailed);
        if (ifDetailed) {
            index = contentRaw.indexOf(ctx.getArgs().get(1)) + 8;
        } else {
            index = contentRaw.indexOf(ctx.getArgs().get(1));
        }
        System.out.println(index);
        String toSearch = contentRaw.substring(index).trim();
        boolean chineseInTheString=isChinese(toSearch);
        toSearch = toSearch.replaceAll(" ", "%20");
        System.out.println(toSearch);

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
                EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                        .setTitle(cityName + "現在天氣")
                        .addField("目前天氣：", weatherDataResult.getString("WeatherText"), true)
                        .addField("目前溫度：", weatherDataResult.getJSONObject("Temperature").getJSONObject("Metric").getInt("Value") + "\u2103", true)
                        .addField("目前濕度：", weatherDataResult.getInt("RelativeHumidity") + "%", true)
                        .addField("目前UV指數：", String.valueOf(weatherDataResult.getInt("UVIndex")), true)
                        .addField("目前降水類型：", !weatherDataResult.getBoolean("HasPrecipitation") ? "無降水" : weatherDataResult.getString("PrecipitationType"), true)
                        .setFooter("資料提供者：AccuWeather (https://www.accuweather.com/)\n資料更新時間：" + weatherDataResult.getString("LocalObservationDateTime").split("T")[0] + " " + weatherDataResult.getString("LocalObservationDateTime").split("T")[1].substring(0, 8), "https://lh3.ggpht.com/7BB1gD1EJ9g2mcqHfAtMuP0Z5Zg1a1syl4l8GTGIXFUUUpTSbg_txXw99YAVUZ9B8A=h300");
                ctx.getChannel().sendMessage(embed.build()).queue();
            } else {
                EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                        .setTitle(cityName + "現在天氣")
                        .addField("目前天氣：", weatherDataResult.getString("WeatherText"), true)
                        .addField("目前溫度：", "**測量溫度：**" + weatherDataResult.getJSONObject("Temperature").getJSONObject("Metric").getInt("Value") + "\u2103\n"
                                        + "**體感溫度：**" + weatherDataResult.getJSONObject("RealFeelTemperature").getJSONObject("Metric").getInt("Value") + "\u2103"
                                , true)
                        .addField("**風向資訊**", "**風向：**" + weatherDataResult.getJSONObject("Wind").getJSONObject("Direction").getString("Localized") + "方" + weatherDataResult.getJSONObject("Wind").getJSONObject("Direction").getInt("Degrees") + "度"
                                + "\n**風速：**" + weatherDataResult.getJSONObject("Wind").getJSONObject("Speed").getJSONObject("Metric").getInt("Value") + " km/h", true)
                        .addField("目前濕度：", weatherDataResult.getInt("RelativeHumidity") + "%", true)
                        .addField("目前UV指數：", String.valueOf(weatherDataResult.getInt("UVIndex")), true)
                        .addField("目前能見度：", weatherDataResult.getJSONObject("Visibility").getJSONObject("Metric").getInt("Value") + "km", true)
                        .addField("目前降水類型：", !weatherDataResult.getBoolean("HasPrecipitation") ? "無降水" : weatherDataResult.getString("PrecipitationType"), true)
                        .addField("氣壓：", "**目前氣壓大小：**" + weatherDataResult.getJSONObject("Pressure").getJSONObject("Metric").getInt("Value") + "毫巴"
                                + "\n氣壓趨勢：" + weatherDataResult.getJSONObject("PressureTendency").getString("LocalizedText"), true)
                        .setFooter("資料提供者：AccuWeather (https://www.accuweather.com/)\n資料更新時間：" + weatherDataResult.getString("LocalObservationDateTime").split("T")[0] + " " + weatherDataResult.getString("LocalObservationDateTime").split("T")[1].substring(0, 8)+"（GMT+"+weatherDataResult.getString("LocalObservationDateTime").split("\\+")[1]+"）", "https://lh3.ggpht.com/7BB1gD1EJ9g2mcqHfAtMuP0Z5Zg1a1syl4l8GTGIXFUUUpTSbg_txXw99YAVUZ9B8A=h300");
                ctx.getChannel().sendMessage(embed.build()).queue();
            }
        } else {
            Request weatherDataRequest = new Request.Builder()
                    .url("http://dataservice.accuweather.com/forecasts/v1/daily/1day/" + cityId + "?apikey=" + APIKEY + "&language=zh-tw&details=true&metric=true")
                    .build();

            Response weatherDataResponse=client.newCall(weatherDataRequest).execute();
            JSONObject wholedata=new JSONObject(weatherDataResponse.body().string());
            JSONObject HeadLine = wholedata.getJSONObject("Headline");
            JSONObject DailyForecast = (JSONObject) wholedata.getJSONArray("DailyForecasts").get(0);
            JSONObject DayData = DailyForecast.getJSONObject("Day");
            JSONObject NightData = DailyForecast.getJSONObject("Night");
            JSONObject Temperature = DailyForecast.getJSONObject("Temperature");
            JSONObject RealFeelTemperature = DailyForecast.getJSONObject("Temperature");
            if (ifDetailed) {
                JSONObject Sun=DailyForecast.getJSONObject("Sun");
                JSONObject Moon=DailyForecast.getJSONObject("Moon");
                String sunrise = Sun.getString("Rise");
                String sunset = Sun.getString("Set");
                String moonrise= Moon.getString("Rise");
                String moonSet = Moon.getString("Set");
                EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                        .setTitle(cityName + HeadLine.getString("Text"))
                        .setDescription("以下之時間之時區皆為（GMT+" + sunrise.split("\\+")[1] + "）")
                        .addField("溫度",
                                "**最高溫度：**" + Temperature.getJSONObject("Maximum").getInt("Value") + "\u2103\n"
                                        + "**最低溫度：**" + Temperature.getJSONObject("Minimum").getInt("Value") + "\u2103", true)
                        .addField("體感溫度",
                                "**最高溫度：**" + RealFeelTemperature.getJSONObject("Maximum").getInt("Value") + "\u2103\n"
                                        + "**最低溫度：**" + RealFeelTemperature.getJSONObject("Minimum").getInt("Value") + "\u2103", true)
                        .addBlankField(true)
                        .addField("早上：",
                                "**天氣：**" + DayData.getString("IconPhrase")
                                        + "\n**降雨機率：**" + DayData.getInt("PrecipitationProbability") + "%", true)
                        .addField("晚上：",
                                "**天氣：**" + NightData.getString("IconPhrase")
                                        + "\n**降雨機率：**" + NightData.getInt("PrecipitationProbability") + "%", true)
                        .addBlankField(true)
                        .addField("太陽：",
                                "**升起時間：**" + sunrise.split("T")[1].substring(0, 8) + "\n"
                                        + "**隱沒時間：**" + sunset.split("T")[1].substring(0, 8) + "\n"
                                        + "**日照時長：**" + DailyForecast.getDouble("HoursOfSun"), true)
                        .addField("月亮：",
                                "**升起時間：**" + moonrise.split("T")[1].substring(0, 8) + "\n"
                                        + "**隱沒時間：**" + moonSet.split("T")[1].substring(0, 8) + "\n"
                                        + "**月亮形狀：**" + Moon.getString("Phase"), true)
                        .addBlankField(true)
                        .setFooter("資料提供者：AccuWeather (https://www.accuweather.com/)", "https://lh3.ggpht.com/7BB1gD1EJ9g2mcqHfAtMuP0Z5Zg1a1syl4l8GTGIXFUUUpTSbg_txXw99YAVUZ9B8A=h300");
                ctx.getChannel().sendMessage(embed.build()).queue();
            } else {
                EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                        .setTitle(cityName + HeadLine.getString("Text"))
                        .addField("溫度",
                                "**最高溫度：**" + Temperature.getJSONObject("Maximum").getInt("Value") + "\u2103\n"
                                        + "**最低溫度：**" + Temperature.getJSONObject("Minimum").getInt("Value") + "\u2103", true)
                        .addField("體感溫度",
                                "**最高溫度：**" + RealFeelTemperature.getJSONObject("Maximum").getInt("Value") + "\u2103\n"
                                        + "**最低溫度：**" + RealFeelTemperature.getJSONObject("Minimum").getInt("Value") + "\u2103", true)
                        .addBlankField(true)
                        .addField("早上：", "**天氣：**" + DayData.getString("IconPhrase") + "\n降雨機率：" + DayData.getInt("PrecipitationProbability") + "%", true)
                        .addField("晚上：", "**天氣：**" + NightData.getString("IconPhrase") + "\n降雨機率：" + NightData.getInt("PrecipitationProbability") + "%", true)
                        .setFooter("資料提供者：AccuWeather (https://www.accuweather.com/)", "https://lh3.ggpht.com/7BB1gD1EJ9g2mcqHfAtMuP0Z5Zg1a1syl4l8GTGIXFUUUpTSbg_txXw99YAVUZ9B8A=h300");
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
