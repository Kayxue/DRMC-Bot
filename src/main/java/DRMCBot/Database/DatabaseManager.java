package DRMCBot.Database;

import org.bson.Document;
import org.json.JSONObject;

public interface DatabaseManager {
    DatabaseManager INSTANCE = new SQLiteDataSource();

    String getPrefix(long guildId);

    void setPrefix(long guildId, String newPrefix);

    JSONObject getsomething(Long serverid);

    JSONObject getServerSuggestionCount(long serverid);

    Document insertsuggestion(long serverid, long authorid, long messageid, String suggestion);

    Document editsuggestion(String action,long serverid,long suggestionid);
}
