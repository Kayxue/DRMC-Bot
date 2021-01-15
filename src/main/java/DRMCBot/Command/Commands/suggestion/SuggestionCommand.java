package DRMCBot.Command.Commands.suggestion;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.Database.DatabaseManager;
import DRMCBot.Database.MongoDbDataSource;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.bson.Document;
import org.json.JSONObject;

import javax.xml.crypto.Data;
import java.util.List;

public class SuggestionCommand implements ICommand {

    MongoDbDataSource mongoDbDataSource = new MongoDbDataSource();
    @Override
    public void handle(CommandContext ctx) {
        final User user = ctx.getAuthor();
        final List<String> args = ctx.getArgs();
        final long guildId = ctx.getGuild().getIdLong();
        if (ctx.getArgs().isEmpty()) {
            ctx.getChannel().sendMessage("請輸入建議！").queue();
            return;
        }
        if (!ctx.getGuild().getId().equals("647643447712415754")) {
            ctx.getChannel().sendMessage("抱歉！此指令目前僅限DRMC伺服器使用！").queue();
            return;
        }
        String suggestion = String.join(" ", args);
        JSONObject channelandsuggestioncount = mongoDbDataSource.getServerSuggestionCount(guildId);
        final TextChannel suggestionsendchannel = ctx.getGuild().getTextChannelById(channelandsuggestioncount.getLong("suggestionchannel"));
        EmbedBuilder embedBuilder = EmbedUtils.getDefaultEmbed()
                .setTitle("建議#" + channelandsuggestioncount.getInt("suggestioncount"))
                .setDescription("建議內容："+suggestion);

        suggestionsendchannel.sendMessage(embedBuilder.build()).queue(
                (message) -> {
                    final long messageId = message.getIdLong();
                    Document insertinfo = mongoDbDataSource.insertsuggestion(guildId, user.getIdLong(), messageId, suggestion);
                    if (insertinfo.getBoolean("success")) {
                        Emote check=ctx.getJDA().getEmotesByName("checkani",true).get(0);
                        Emote cross = ctx.getJDA().getEmotesByName("crossani", true).get(0);
                        message.addReaction(check).queue();
                        message.addReaction(cross).queue();
                        Emote emote = ctx.getSelfUser().getJDA().getEmotesByName("secretthonk", true).get(0);
                        message.addReaction(emote).queue();
                        ctx.getChannel().sendMessage("建議發送成功！").queue();
                    } else {
                        ctx.getChannel().sendMessage("指令未能成功執行").queue();
                    }
                },
                (error) -> {
                    ctx.getChannel().sendMessage("指令未能成功執行").queue();
                });

        //ctx.getChannel().sendMessage(suggestion).queue();
    }

    @Override
    public String getName() {
        return "suggest";
    }

    @Override
    public String getCategory() {
        return "suggestion";
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
