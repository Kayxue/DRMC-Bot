package DRMCBot.Command.Commands.Ticket;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.Database.MongoDbDataSource;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.TextChannel;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class CloseTicketCommand implements ICommand {
    MongoDbDataSource mongoDbDataSource = new MongoDbDataSource();

    @Override
    public void handle(CommandContext ctx) {
        if (!ctx.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
            ctx.getChannel().sendMessage("您沒有權限執行此指令！").queue();
            return;
        }
        if (!ctx.getSelfMember().hasPermission(Permission.MANAGE_CHANNEL)) {
            ctx.getChannel().sendMessage("我沒有權限執行此指令！").queue();
            return;
        }
        JSONObject jsonObject = mongoDbDataSource.getguildticketcategory(ctx.getGuild().getIdLong());
        Category ticketcategory = ctx.getGuild().getCategoryById(jsonObject.getLong("categoryid"));
        TextChannel ticketchannel = (TextChannel) ctx.getChannel();
        if (!ticketcategory.getChannels().contains(ticketchannel)) {
            ctx.getChannel().sendMessage("此指令無法用在非票口頻道！").queue();
            return;
        }
        ticketchannel.delete().queue();
    }

    @Override
    public String getName() {
        return "closeticket";
    }

    @Override
    public String getCategory() {
        return "ticket";
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
