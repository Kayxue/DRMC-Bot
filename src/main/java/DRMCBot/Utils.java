package DRMCBot;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Utils {
    public static EmbedBuilder DiscordJSEmbedParser(JSONObject json, boolean needTimeStamp) {
        EmbedBuilder embed = EmbedUtils.defaultEmbed();
        try {
            embed.setColor(json.getInt("color"));
        } catch (Exception ignored) {

        }
        try {
            JSONObject author = json.getJSONObject("author");
            embed.setAuthor(author.getString("name"), author.getString("url"), author.getString("icon_url"));
        } catch (Exception ignored) {

        }
        try {
            embed.setDescription(json.getString("description"));
        } catch (Exception ignored) {

        }
        try {
            JSONArray fields = json.getJSONArray("fields");
            for (int i = 0; i < fields.length(); i++) {
                embed.addField(((JSONObject) fields.get(i)).getString("name"), ((JSONObject) fields.get(i)).getString("value"), false);
            }
        } catch (Exception ignored) {

        }
        try {
            embed.setTitle(json.getString("title"));
        } catch (Exception ignored) {

        }
        try {
            embed.setThumbnail(json.getJSONObject("thumbnail").getString("url"));
        } catch (Exception ignored) {

        }
        try {
            embed.setImage(json.getJSONObject("image").getString("url"));
        } catch (Exception ignored) {

        }
        if (needTimeStamp) {
            embed.setTimestamp(ZonedDateTime.now(ZoneId.of("Asia/Taipei")));
        }
        try {
            embed.setFooter(json.getJSONObject("footer").getString("text"), json.getJSONObject("footer").getString("icon_url"));
        } catch (Exception ignored) {

        }
        return embed;
    }
}
