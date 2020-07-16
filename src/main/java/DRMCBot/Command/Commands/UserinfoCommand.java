package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class UserinfoCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final List<String> args = ctx.getArgs();
        User user = ctx.getAuthor();
        Member member = null;
        String description = "";
        String timeCreatedString = "";
        String timeJoinedString = "";


        if (args.size() == 1 && ctx.getMessage().getMentionedChannels().size() == 0 && ctx.getMessage().getMentionedMembers().size() < 2 && ctx.getMessage().getMentionedRoles().size() == 0) {

            if (ctx.getMessage().getMentionedMembers().size() > 0) {
                member = ctx.getMessage().getMentionedMembers().get(0);
                if (member != null) {
                    ctx.getChannel().sendMessage("成功抓取使用者！").queue();

                }
            } else {
                if (ctx.getArgs().get(0).contains(".") || ctx.getArgs().get(0).length() != 18) {
                    ctx.getChannel().sendMessage("輸入內容不是ID！").queue();
                    return;
                }
                try {
                    Long.parseLong(ctx.getArgs().get(0));
                } catch (NumberFormatException e) {
                    ctx.getChannel().sendMessage("輸入內容不是ID！").queue();
                    return;
                }
                member = ctx.getGuild().getMemberById(ctx.getArgs().get(0));
                if (member == null) {
                    ctx.getChannel().sendMessage("找不到該使用者！").queue();
                    return;
                } else {

                    ctx.getChannel().sendMessage("成功抓取使用者！").queue();
                }
            }

        } else if (args.size() > 1 ||
                ctx.getMessage().getMentionedUsers().size() > 1) {
            ctx.getChannel().sendMessage("一次只能提及一個使用者！").queue();
            return;
        } else if (ctx.getMessage().getMentionedChannels().size() > 0) {
            ctx.getChannel().sendMessage("請提及使用者！").queue();
            return;
        } else if (ctx.getMessage().getMentionedRoles().size() > 0) {
            ctx.getChannel().sendMessage("請提及使用者！").queue();
            return;
        } else if (args.size() == 0) {
            member = ctx.getGuild().getMemberById(ctx.getAuthor().getId());
            if (member != null) {
                ctx.getChannel().sendMessage("成功抓取使用者！").queue();

            }
        }

        final OffsetDateTime timeCreated = member.getUser().getTimeCreated();
        final OffsetDateTime timeJoined = member.getTimeJoined();
        final ZonedDateTime timeCreatedWithZone = timeCreated.atZoneSameInstant(ZoneId.of("Asia/Taipei"));
        final ZonedDateTime timeJoinedWithZone = timeCreated.atZoneSameInstant(ZoneId.of("Asia/Taipei"));

        timeCreatedString = formattime(timeCreatedWithZone.getYear()) + "/"
                + formattime(timeCreatedWithZone.getMonthValue()) + "/"
                + formattime(timeCreatedWithZone.getDayOfMonth()) + "\n"
                + formattime(timeCreatedWithZone.getHour()) + ":"
                + formattime(timeCreatedWithZone.getMinute()) + ":"
                + formattime(timeCreatedWithZone.getSecond()) + "\n"
                + "(UTC"
                + timeCreatedWithZone.getOffset()
                + ")";

        timeJoinedString = formattime(timeJoinedWithZone.getYear()) + "/"
                + formattime(timeJoinedWithZone.getMonthValue()) + "/"
                + formattime(timeJoinedWithZone.getDayOfMonth()) + "\n"
                + formattime(timeJoinedWithZone.getHour()) + ":"
                + formattime(timeJoinedWithZone.getMinute()) + ":"
                + formattime(timeJoinedWithZone.getSecond()) + "\n"
                + "(UTC"
                + timeJoinedWithZone.getOffset()
                + ")";

        if (user == ctx.getAuthor()) {
            description = "關於此使用者的資訊";
        } else {
            description = "關於使用者" + member.getUser().getName() + "的資訊";
        }

        EmbedBuilder embed = EmbedUtils.defaultEmbed()
                .setTitle("使用者資訊")
                .setDescription(description)
                .addField("使用者名稱：", member.getUser().getName() + "#" + member.getUser().getDiscriminator(), false)
                .addField("在此群的暱稱",member.getNickname(),false)
                .addField("上線狀態","還沒寫",false)
                .addField("在伺服器最高身分組",member.getRoles().get(0).getAsMention(),false)
                .addField("加入伺服器時間", timeJoinedString,false)
                .addField("帳號創建時間", timeCreatedString,false)
                .setThumbnail(member.getUser().getAvatarUrl())
                .setFooter(member.getUser().getName()+"#"+member.getUser().getDiscriminator()+"\n"+
                        "ID:"+member.getUser().getId(),member.getUser().getAvatarUrl());

        ctx.getChannel().sendMessage(embed.build()).queue();


    }

    public String formattime(int i) {
        String a = "";
        if (String.valueOf(i).length() == 1) {
            a = "0" + i;
        } else {
            a = String.valueOf(i);
        }
        return a;
    }

    @Override
    public String getName() {
        return "userinfo";
    }
}
