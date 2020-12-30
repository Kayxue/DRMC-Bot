package DRMCBot.Database;

import DRMCBot.Config;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.json.JSONObject;

import static com.mongodb.client.model.Filters.eq;

public class MongoDbDataSource2 implements DatabaseManager{

    @Override
    public String getPrefix(long guildId) {
        MongoClient client;
        MongoClientURI uri = new MongoClientURI(
                "mongodb+srv://Kay:Kay20030910@kaybotjava.ojh3g.gcp.mongodb.net/test?retryWrites=true&w=majority");
        client = new MongoClient(uri);
        MongoCollection<Document> prefixdata;
        prefixdata = client.getDatabase("KayBotJava").getCollection("prefix");
        try {
            Document documents = prefixdata.find(eq("_id", String.valueOf(guildId))).first();
            if (documents == null) {
                Document document = new Document("_id", String.valueOf(guildId))
                        .append("prefix", Config.get("prefix"));
                prefixdata.insertOne(document);
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
        MongoClient client;
        MongoClientURI uri = new MongoClientURI(
                "mongodb+srv://Kay:Kay20030910@kaybotjava.ojh3g.gcp.mongodb.net/test?retryWrites=true&w=majority");
        client = new MongoClient(uri);
        MongoCollection<Document> prefixdata;
        prefixdata = client.getDatabase("KayBotJava").getCollection("prefix");
        try {
            prefixdata.updateOne(eq("_id", String.valueOf(guildId)), new Document("$set", new Document("prefix", newPrefix)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public JSONObject getsomething(Long serverid) {
        MongoClient client;
        MongoClientURI uri = new MongoClientURI(
                "mongodb+srv://Kay:Kay20030910@kaybotjava.ojh3g.gcp.mongodb.net/test?retryWrites=true&w=majority");
        client = new MongoClient(uri);
        MongoCollection<Document> serversettingdata;
        serversettingdata = client.getDatabase("KayBotJava").getCollection("serversetting");
        JSONObject serverallsuggestion = new JSONObject(serversettingdata.find(eq("_id", String.valueOf(serverid))).first().toJson());
        return serverallsuggestion;
    }

    @Override
    public JSONObject getServerSuggestionCount(long serverid) {
        MongoClient client;
        MongoClientURI uri = new MongoClientURI(
                "mongodb+srv://Kay:Kay20030910@kaybotjava.ojh3g.gcp.mongodb.net/test?retryWrites=true&w=majority");
        client = new MongoClient(uri);
        MongoCollection<Document> serversettingdata;
        serversettingdata = client.getDatabase("KayBotJava").getCollection("serversetting");
        try {
            JSONObject jsonObject = new JSONObject(serversettingdata.find(eq("_id", String.valueOf(serverid))).first().toJson());
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Document insertsuggestion(long serverid, long authorid, long messageid, String suggestion) {
        MongoClient client;
        MongoClientURI uri = new MongoClientURI(
                "mongodb+srv://Kay:Kay20030910@kaybotjava.ojh3g.gcp.mongodb.net/test?retryWrites=true&w=majority");
        client = new MongoClient(uri);
        MongoCollection<Document> serversettingdata;
        serversettingdata = client.getDatabase("KayBotJava").getCollection("serversetting");
        MongoCollection<Document> suggestiondata;
        suggestiondata = client.getDatabase("KayBotJava").getCollection("serversuggestion");
        try {
            JSONObject jsonObject = new JSONObject(serversettingdata.find(eq("_id", String.valueOf(serverid))).first().toJson());
            if (client.getDatabase("KayBotJava").getCollection("serversuggestion").find(eq("_id", String.valueOf(serverid))).first() == null) {
                Document insertdocument = new Document("_id", String.valueOf(serverid))
                        .append(String.valueOf(jsonObject.getInt("suggestioncount")), new Document("author", String.valueOf(authorid))
                                .append("messageid", String.valueOf(messageid))
                                .append("status", "waiting")
                                .append("suggestion", suggestion));
                serversettingdata.updateOne(eq("_id", String.valueOf(serverid)), new Document("$set", new Document("suggestioncount", jsonObject.getInt("suggestioncount") + 1)));
                suggestiondata.insertOne(insertdocument);
            } else {
                Document insertdocument = new Document(String.valueOf(jsonObject.getInt("suggestioncount")), new Document("author", String.valueOf(authorid))
                        .append("messageid", String.valueOf(messageid))
                        .append("status", "waiting")
                        .append("suggestion", suggestion));
                suggestiondata.updateOne(eq("_id", String.valueOf(serverid)), new Document("$set", insertdocument));
                serversettingdata.updateOne(eq("_id", String.valueOf(serverid)), new Document("$set", new Document("suggestioncount", jsonObject.getInt("suggestioncount") + 1)));
            }
            return new Document("success",true).append("suggestioncount",jsonObject.getInt("suggestioncount"));
        } catch (Exception e) {
            e.printStackTrace();
            return new Document("success",false);
        }
    }

    @Override
    public Document editsuggestion(String action, long serverid, long suggestionid) {
        MongoClient client;
        MongoClientURI uri = new MongoClientURI(
                "mongodb+srv://Kay:Kay20030910@kaybotjava.ojh3g.gcp.mongodb.net/test?retryWrites=true&w=majority");
        client = new MongoClient(uri);
        MongoCollection<Document> serversettingdata;
        serversettingdata = client.getDatabase("KayBotJava").getCollection("serversetting");
        MongoCollection<Document> suggestiondata;
        suggestiondata = client.getDatabase("KayBotJava").getCollection("serversuggestion");
        try {
            JSONObject serverallsuggestion = new JSONObject(suggestiondata.find(eq("_id", String.valueOf(serverid))).first().toJson());
            JSONObject thesuggestion = (JSONObject) serverallsuggestion.get(String.valueOf(suggestionid));
            JSONObject serverdata = new JSONObject(serversettingdata.find(eq("_id", String.valueOf(serverid))).first().toJson());
            suggestiondata.updateOne(eq("_id", String.valueOf(serverid)), new Document("$set", new Document(suggestionid + ".status", action)));
            return new Document("success", true).append("author", thesuggestion.getLong("author")).append("messageid", thesuggestion.getLong("messageid")).append("suggestion", thesuggestion.get("suggestion")).append("suggestionchannel",serverdata.getLong("suggestionchannel"));
        } catch (Exception e) {
            e.printStackTrace();
            return new Document("success", false);
        }
    }

    public JSONObject getguildticketcategory(long serverid) {
        MongoClient client;
        MongoClientURI uri = new MongoClientURI(
                "mongodb+srv://Kay:Kay20030910@kaybotjava.ojh3g.gcp.mongodb.net/test?retryWrites=true&w=majority");
        client = new MongoClient(uri);
        MongoCollection<Document> serversettingdata;
        serversettingdata = client.getDatabase("KayBotJava").getCollection("serversetting");
        try {
            JSONObject serversettingsdata = new JSONObject(serversettingdata.find(eq("_id", String.valueOf(serverid))).first().toJson());
            JSONObject toreturn = new JSONObject()
                    .put("categoryid", serversettingsdata.getLong("ticketcategory"))
                    .put("success", true);
            return toreturn;
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject().put("success", false);
        }
    }

    @Override
    public JSONObject getlogchannel(long serverid) {
        return null;
    }
}
