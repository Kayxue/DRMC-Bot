package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.Database.MongoDbDataSource;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

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

    @Override
    public String getCategory() {
        return "nocategory";
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
