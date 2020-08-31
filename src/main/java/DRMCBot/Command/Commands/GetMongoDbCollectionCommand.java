package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.Database.MongoDbDataSource;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.json.JSONObject;

public class GetMongoDbCollectionCommand implements ICommand {
    MongoDbDataSource mongoDbDataSource = new MongoDbDataSource();

    @Override
    public void handle(CommandContext ctx) {
        JSONObject jsonObject = mongoDbDataSource.getsomething(ctx.getGuild().getIdLong());
        ctx.getChannel().sendMessage(jsonObject.toString('4')).queue();
    }

    @Override
    public String getName() {
        return "getdbcollection";
    }
}
