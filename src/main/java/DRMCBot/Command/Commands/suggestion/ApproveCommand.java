package DRMCBot.Command.Commands.suggestion;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.Database.MongoDbDataSource;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.bson.Document;

import java.util.ArrayList;

public class ApproveCommand implements ICommand {
    MongoDbDataSource mongoDbDataSource = new MongoDbDataSource();
    @Override
    public void handle(CommandContext ctx) {
        try {
            Integer.parseInt(ctx.getArgs().get(0));
        } catch (Exception e) {
            ctx.getChannel().sendMessage("請輸入建議編號！");
            return;
        }
        Document data = mongoDbDataSource.editsuggestion("approve", ctx.getGuild().getIdLong(), Long.parseLong(ctx.getArgs().get(0)));
        if (data.get("success").equals(true)) {
            final User suggestionAuthor = ctx.getSelfUser().getJDA().getUserById(data.getLong("author"));
            final TextChannel suggestionchannel = ctx.getGuild().getTextChannelById(data.getLong("suggestionchannel"));

            suggestionchannel.retrieveMessageById(data.getLong("messageid")).queue(
                    message -> {
                        ArrayList<String> reason = new ArrayList();
                        for (int i = 1; i < ctx.getArgs().size(); i++) {
                            reason.add(ctx.getArgs().get(i));
                        }
                        String reasonstring = String.join(" ", reason);
                        EmbedBuilder embedBuilder = EmbedUtils.defaultEmbed()
                                .setColor(0x06D837)
                                .setTitle("建議#" + Long.parseLong(ctx.getArgs().get(0)))
                                .addField("建議內容：", data.getString("suggestion"), false)
                                .addField("原因：", reasonstring, false)
                                .setFooter(ctx.getAuthor().getName() + "#" + ctx.getAuthor().getDiscriminator(), ctx.getAuthor().getAvatarUrl());

                        message.editMessage("\u2705 建議已批准！").embed(embedBuilder.build()).queue(
                                editmessage -> {
                                    editmessage.removeReaction("\u2705").queue();
                                    editmessage.removeReaction("\u274C").queue();
                                    Emote emote = ctx.getSelfUser().getJDA().getEmotesByName("secretthonk", true).get(0);
                                    editmessage.removeReaction(emote).queue();
                                }
                        );
                        suggestionAuthor.openPrivateChannel().queue(
                                channel -> {
                                    channel.sendMessage("您提的建議已被批准！原因為：" + reasonstring).embed(embedBuilder.build()).queue();
                                    ctx.getChannel().sendMessage("指令已成功批准！").queue();
                                },
                                error -> {
                                    error.printStackTrace();
                                    ctx.getChannel().sendMessage("在發送私訊時發生錯誤！").queue();
                                }
                        );
                    },
                    error -> {
                        error.printStackTrace();
                        ctx.getChannel().sendMessage("指令無法成功執行！").queue();
                    }
            );

        } else {
            ctx.getChannel().sendMessage("更改資料時出線錯誤！");
        }
    }

    @Override
    public String getName() {
        return "approve";
    }
}
