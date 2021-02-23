package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.Config;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.time.*;
import java.util.HashMap;
import java.util.List;

public class OsuCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {
        List<String> args = ctx.getArgs();
        if (args.isEmpty()) {
            ctx.getChannel().sendMessage("請輸入參數！").queue();
            return;
        }
        String userToSearch = String.join("%20", args);
        System.out.println(userToSearch);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://osu.ppy.sh/api/get_user?k=" + Config.get("osukey") + "&u=" + userToSearch)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            ctx.getChannel().sendMessage("獲取資料失敗！").queue();
        }
        JSONArray jsonArray = new JSONArray(response.body().string());
        if (jsonArray.isEmpty()) {
            ctx.getChannel().sendMessage("找不到使用者！").queue();
            return;
        }
        JSONObject userjson = (JSONObject) jsonArray.get(0);
        System.out.println(userjson.toString(4));
        String userProfileUrl = "https://osu.ppy.sh/u/" + userjson.get("user_id");
        String rankCardUrl = "http://lemmmy.pw/osusig/sig.php?colour=hex01afef&uname=" + userToSearch + "&pp=2&removeavmargin&flagshadow&darktriangles&rankedscore&onlineindicator=undefined&xpbar&xpbarhex";
        String useravatarurl = "http://s.ppy.sh/a/" + userjson.getString("user_id");
        String joinDate = userjson.getString("join_date").replaceAll(" ", "T");
        OffsetDateTime joinedNoZone = OffsetDateTime.of(LocalDateTime.parse(joinDate), ZoneOffset.UTC);
        ZonedDateTime joinedWithZone = joinedNoZone.atZoneSameInstant(ZoneId.of("Asia/Taipei"));
        EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                .setTitle(userjson.getString("username") + "｜" + userjson.getString("user_id") + "｜" + (userjson.get("pp_rank").toString().equals("null") ? "0" : "#" + userjson.getString("pp_rank")), userProfileUrl)
                .setDescription(
                        "SS+－" + (userjson.get("count_rank_ssh").toString().equals("null") ? "0" : userjson.getInt("count_rank_ssh")) + "｜" +
                                "SS－" + (userjson.get("count_rank_ss").toString().equals("null") ? "0" : userjson.getInt("count_rank_ss")) + "｜" +
                                "S+－" + (userjson.get("count_rank_sh").toString().equals("null") ? "0" : userjson.getInt("count_rank_sh")) + "｜" +
                                "S－" + (userjson.get("count_rank_s").toString().equals("null") ? "0" : userjson.getInt("count_rank_s")) + "｜" +
                                "A－" + (userjson.get("count_rank_a").toString().equals("null") ? "0" : userjson.getInt("count_rank_a"))
                )
                .addField("國家", userjson.getString("country"), true)
                .addField("遊玩次數", (userjson.get("playcount").toString().equals("null") ? "0" : userjson.getString("playcount")), true)
                .addField("等級", (userjson.get("level").toString().equals("null") ? "0" : String.valueOf((int) Double.parseDouble(userjson.getString("level")))), true)
                .addField("Ranked｜總分", (userjson.get("ranked_score").toString().equals("null") ? "0" : userjson.getString("ranked_score")) + "｜" + (userjson.get("total_score").toString().equals("null") ? "0" : userjson.getString("total_score")), true)
                .addField("國內排｜PP", (userjson.get("pp_country_rank").toString().equals("null") ? "0" : "#" + userjson.getString("pp_country_rank")) + "｜" + (userjson.get("pp_raw").toString().equals("null") ? "0" : String.valueOf((int) Math.floor(Double.parseDouble(userjson.getString("pp_raw"))))), true)
                .addField("準確率", (userjson.get("accuracy").toString().equals("null") ? "0.00%" : new DecimalFormat("##0.00").format(Double.parseDouble(userjson.getString("accuracy"))) + "%"), true)
                .addField("加入時間", getTimeString(joinedWithZone), true)
                .setThumbnail(useravatarurl)
                .setImage(rankCardUrl);
        ctx.getChannel().sendMessage(embed.build()).queue();
    }

    private String getTimeString(ZonedDateTime dateTime) {
        String timeString;
        timeString = dateTime.getYear() + "/"
                + getTwoDigit(dateTime.getMonthValue()) + "/"
                + getTwoDigit(dateTime.getDayOfMonth()) + " "
                + getTwoDigit(dateTime.getHour()) + ":"
                + getTwoDigit(dateTime.getMinute()) + ":"
                + getTwoDigit(dateTime.getSecond())
                + " (UTC" + dateTime.getOffset() + ")";
        return timeString;
    }

    private String getTwoDigit(long number) {
        String numberString = String.valueOf(number);
        if (numberString.length() == 2) {
            return numberString;
        } else {
            return "0" + numberString;
        }
    }

    @Override
    public String getName() {
        return "osu";
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
