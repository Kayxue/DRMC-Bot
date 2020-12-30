package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.Database.DatabaseManager;
import DRMCBot.Database.MongoDbDataSource;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.JSONObject;

public class GetMongoDbCollectionCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        JSONObject jsonObject = DatabaseManager.INSTANCE.getsomething(ctx.getGuild().getIdLong());
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
    public EmbedBuilder gethelpembed() {
        return null;
    }
}
