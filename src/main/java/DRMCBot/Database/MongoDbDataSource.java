package DRMCBot.Database;

import DRMCBot.Config;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.logging.Filter;

import static com.mongodb.client.model.Filters.eq;

public class MongoDbDataSource implements DatabaseManager{
    MongoClient client;
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoDbDataSource.class);

    public MongoDbDataSource() {
        MongoClientURI uri = new MongoClientURI(
                "mongodb+srv://Kay:Kay20030910@kaybotjava.ojh3g.gcp.mongodb.net/test?retryWrites=true&w=majority");
        client = new MongoClient(uri);

        LOGGER.info("Connected Successful!!!");
    }

    @Override
    public String getPrefix(long guildId) {
        try {
            MongoCollection<Document> collection = client.getDatabase("KayBotJava").getCollection("prefix");
            Document documents = collection.find(eq("_id", String.valueOf(guildId))).first();
            if (documents == null) {
                Document document = new Document("_id", String.valueOf(guildId))
                        .append("prefix", Config.get("prefix"));
                collection.insertOne(document);
            } else {
                return documents.get("prefix").toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Config.get("prefix");
    }

    @Override
    public void setPrefix(long guildId, String newPrefix) {
        try {
            MongoCollection<Document> collection = client.getDatabase("KayBotJava").getCollection("prefix");
            collection.updateOne(eq("_id", String.valueOf(guildId)), new Document("$set", new Document("prefix", newPrefix)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONObject getsomething() {
        MongoCollection<org.bson.Document> collection=client.getDatabase("KayBotJava").getCollection("prefix");
        FindIterable<Document> document=collection.find(eq("_id", "5f39325c0b130698cde2a5d3"));
        JSONObject jsonObject = new JSONObject(document.first().toJson());
        return jsonObject;
    }
}
