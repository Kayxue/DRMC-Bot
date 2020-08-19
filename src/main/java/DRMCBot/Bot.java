package DRMCBot;

import DRMCBot.Database.SQLiteDataSource;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import okhttp3.Cache;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;
import java.util.EnumSet;

public class Bot {

    private Bot() throws LoginException {
        WebUtils.setUserAgent("Mozilla/5.0 DRMC Bot#7872");
        EmbedUtils.setEmbedBuilder(
                ()-> new EmbedBuilder()
                .setColor(0x01afef)
                .setFooter("DRMC Bot")
        );


        new JDABuilder()
                .setToken(Config.get("TOKEN"))
                .enableCache(CacheFlag.VOICE_STATE)
                .addEventListeners(new Listener())
                .setActivity(Activity.playing("Welcome to New DL/RS/MC Chatroom"))
                .build();

    }
    public static void main(String[] args) throws LoginException {
        new Bot();
    }
}
