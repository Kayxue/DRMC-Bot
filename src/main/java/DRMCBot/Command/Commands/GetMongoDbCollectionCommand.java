package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.Database.MongoDbDataSource;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.json.JSONObject;

public class GetMongoDbCollectionCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        JSONObject jsonObject = new MongoDbDataSource().getsomething();
        ctx.getChannel().sendMessage(jsonObject.get("name").toString()).queue();
    }

    @Override
    public String getName() {
        return "getdbcollection";
    }
}
