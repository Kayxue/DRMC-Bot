package DRMCBot.Database;

public interface DatabaseManager {
    DatabaseManager INSTANCE = new MongoDbDataSource();

    String getPrefix(long guildId);

    void setPrefix(long guildId, String newPrefix);
}
