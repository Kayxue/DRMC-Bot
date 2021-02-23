package DRMCBot.Command.Commands.suggestion;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.Database.MongoDbDataSource;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DenyCommand implements ICommand {
    MongoDbDataSource mongoDbDataSource = new MongoDbDataSource();
    @Override
    public void handle(CommandContext ctx) {
        final Member member = ctx.getMember();
        if (!member.hasPermission(Permission.MANAGE_CHANNEL)){
            ctx.getChannel().sendMessage("您沒有權限批改建議！").queue();
            return;
        }
        if (!ctx.getGuild().getId().equals("647643447712415754")) {
            ctx.getChannel().sendMessage("抱歉！此指令目前僅限DRMC伺服器使用！").queue();
            return;
        }
        try {
            if (ctx.getArgs().isEmpty()) {
                ctx.getChannel().sendMessage("請輸入建議編號！").queue();
                return;
            }
            Integer.parseInt(ctx.getArgs().get(0));
            if (ctx.getArgs().size() < 2) {
                ctx.getChannel().sendMessage("請輸入原因！").queue();
                return;
            }
        } catch (Exception e) {
            ctx.getChannel().sendMessage("請輸入建議編號！").queue();
            return;
        }
        Document data = mongoDbDataSource.editsuggestion("deny", ctx.getGuild().getIdLong(), Long.parseLong(ctx.getArgs().get(0)));
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
                        EmbedBuilder embedBuilder = EmbedUtils.getDefaultEmbed()
                                .setColor(0xF53221)
                                .setTitle("建議#" + Long.parseLong(ctx.getArgs().get(0)))
                                .addField("建議內容：", data.getString("suggestion"), false)
                                .addField("原因：", reasonstring, false)
                                .setFooter(ctx.getAuthor().getName() + "#" + ctx.getAuthor().getDiscriminator(), ctx.getAuthor().getAvatarUrl());
                        Emote cross = ctx.getJDA().getEmotesByName("crossani", true).get(0);
                        message.editMessage(cross.getAsMention()+" 建議已被駁回！").embed(embedBuilder.build()).queue(
                                editmessage -> {
                                    Emote check=ctx.getJDA().getEmotesByName("checkani",true).get(0);
                                    editmessage.removeReaction(check).queue();
                                    editmessage.removeReaction(cross).queue();
                                    Emote emote = ctx.getSelfUser().getJDA().getEmotesByName("secretthonk", true).get(0);
                                    editmessage.removeReaction(emote).queue();
                                }
                        );
                        suggestionAuthor.openPrivateChannel().queue(
                                channel -> {
                                    channel.sendMessage("您提的建議已被駁回！原因為：" + reasonstring).embed(embedBuilder.build()).queue();
                                    ctx.getChannel().sendMessage("建議已成功批准！").queue();
                                },
                                error -> {
                                    error.printStackTrace();
                                    ctx.getChannel().sendMessage("在發送私訊時發生錯誤！").queue();
                                }
                        );
                    },
                    error -> {
                        if (error instanceof NullPointerException) {
                            ctx.getChannel().sendMessage("找不到訊息！").queue();
                        } else {
                            ctx.getChannel().sendMessage(error.getMessage()).queue();
                        }
                    }
            );
        } else {
            ctx.getChannel().sendMessage("更改資料時出現錯誤！");
        }
    }

    @Override
    public String getName() {
        return "deny";
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
