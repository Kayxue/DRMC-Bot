package DRMCBot.Command.Commands.Ticket;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.Database.DatabaseManager;
import DRMCBot.Database.MongoDbDataSource;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import org.json.JSONObject;

public class OpenTicketCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        Member ticketcreator = ctx.getMember();
        JSONObject jsonObject = DatabaseManager.INSTANCE.getguildticketcategory(ctx.getGuild().getIdLong());
        if (jsonObject.getBoolean("success")) {
            Category ticketcategory = ctx.getGuild().getCategoryById(jsonObject.getLong("categoryid"));
            if (ticketcategory == null) {
                ctx.getChannel().sendMessage("找不到此類別！").queue();
                return;
            }

            ticketcategory.createTextChannel(ctx.getAuthor().getId()).queue(
                    textChannel -> {
                        textChannel.getManager().setTopic("票口開啟人：" + ctx.getAuthor().getName() + "#" + ctx.getAuthor().getDiscriminator()).queue();
                        Role everyone = ctx.getGuild().getPublicRole();
                        Permission[] permissions = {Permission.MESSAGE_READ,
                                Permission.MESSAGE_WRITE,
                                Permission.MESSAGE_HISTORY,
                                Permission.MESSAGE_ADD_REACTION,
                                Permission.MESSAGE_ATTACH_FILES};
                        textChannel.putPermissionOverride(everyone)
                                .setDeny(Permission.ALL_PERMISSIONS).queue();
                        textChannel.createPermissionOverride(ticketcreator)
                                .setDeny(Permission.ALL_PERMISSIONS)
                                .setAllow(permissions)
                                .setAllow(permissions)
                                .queue();
                        textChannel.sendMessage("票口開啟人：" + ctx.getAuthor().getName() + "#" + ctx.getAuthor().getDiscriminator()).queue();
                        ctx.getChannel().sendMessage("票口開設完成！").queue();
                    },
                    error -> {
                        if (error instanceof InsufficientPermissionException) {
                            ctx.getChannel().sendMessage("我沒有權限建立頻道！").queue();
                        } else {
                            ctx.getChannel().sendMessage(error.getLocalizedMessage()).queue();
                        }
                    }
            );
        } else {
            ctx.getChannel().sendMessage("我拿不到資料！").queue();
        }
    }

    @Override
    public String getName() {
        return "openticket";
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
    public EmbedBuilder gethelpembed() {
        return null;
    }
}
