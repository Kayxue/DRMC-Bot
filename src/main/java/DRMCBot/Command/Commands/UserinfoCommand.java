package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.time.*;
import java.util.HashMap;
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

        String lengthCreated = "";
        String lengthJoined = "";
        final OffsetDateTime timeCreated = member.getUser().getTimeCreated();
        final OffsetDateTime timeJoined = member.getTimeJoined();
        final ZonedDateTime timeCreatedWithZone = timeCreated.atZoneSameInstant(ZoneId.of("Asia/Taipei"));
        final ZonedDateTime timeJoinedWithZone = timeJoined.atZoneSameInstant(ZoneId.of("Asia/Taipei"));
        final ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Taipei"));

        long lengthcreatedsecond = Duration.between(timeCreatedWithZone, now).getSeconds();
        long lengthjoinedsecond = Duration.between(timeJoinedWithZone, now).getSeconds();

        if (lengthcreatedsecond / 3600 < 24) {
            long hour = lengthcreatedsecond / 3600;
            long minute = (lengthcreatedsecond % 3600) / 60;
            long second = (lengthcreatedsecond % 3600) % 60;
            if (hour != 0 || minute != 0) {
                if (hour != 0) {
                    lengthCreated += hour + "小時";
                }
                if (minute != 0) {
                    lengthCreated += minute + "分鐘";
                }
            } else {
                lengthCreated += second + "秒";
            }
        } else {
            LocalDate nowlocal = now.toLocalDate();
            LocalDate timeCreatelocal = timeCreatedWithZone.toLocalDate();
            Period period = Period.between(timeCreatelocal, nowlocal);
            if (period.getYears() != 0) {
                lengthCreated += period.getYears() + "年";
            }
            if (period.getMonths() != 0) {
                if (lengthCreated.length() != 0) {
                    lengthCreated += "又";
                }
                lengthCreated += period.getMonths() + "個月";
            }
            if (period.getDays() != 0) {
                if (lengthCreated.length() != 0) {
                    lengthCreated += "又";
                }
                lengthCreated += period.getDays() + "天";
            }
        }

        if (lengthjoinedsecond / 3600 < 24) {
            long hour = lengthjoinedsecond / 3600;
            long minute = (lengthjoinedsecond % 3600) / 60;
            long second = (lengthjoinedsecond % 3600) % 60;
            if (hour != 0 || minute != 0) {
                if (hour != 0) {
                    lengthJoined += hour + "小時";
                }
                if (minute != 0) {
                    lengthJoined += minute + "分鐘";
                }
            } else {
                lengthJoined += second + "秒";
            }
        } else {
            LocalDate nowlocal = now.toLocalDate();
            LocalDate timeJoinlocal = timeJoinedWithZone.toLocalDate();
            Period period = Period.between(timeJoinlocal, nowlocal);
            if (period.getYears() != 0) {
                lengthJoined += period.getYears() + "年";
            }
            if (period.getMonths() != 0) {
                if (lengthJoined.length() != 0) {
                    lengthJoined += "又";
                }
                lengthJoined += period.getMonths() + "個月";
            }
            if (period.getDays() != 0) {
                if (lengthJoined.length() != 0) {
                    lengthJoined += "又";
                }
                lengthJoined += period.getDays() + "天";
            }
        }

        lengthCreated += "之前";
        lengthJoined += "之前";

        timeCreatedString = formattime(timeCreatedWithZone.getYear()) + "/"
                + formattime(timeCreatedWithZone.getMonthValue()) + "/"
                + formattime(timeCreatedWithZone.getDayOfMonth()) + "\n"
                + formattime(timeCreatedWithZone.getHour()) + ":"
                + formattime(timeCreatedWithZone.getMinute()) + ":"
                + formattime(timeCreatedWithZone.getSecond()) + "\n"
                + "(UTC" + timeCreatedWithZone.getOffset() + ")" + "\n"
                + "``("+lengthCreated+")``";

        timeJoinedString = formattime(timeJoinedWithZone.getYear()) + "/"
                + formattime(timeJoinedWithZone.getMonthValue()) + "/"
                + formattime(timeJoinedWithZone.getDayOfMonth()) + "\n"
                + formattime(timeJoinedWithZone.getHour()) + ":"
                + formattime(timeJoinedWithZone.getMinute()) + ":"
                + formattime(timeJoinedWithZone.getSecond()) + "\n"
                + "(UTC" + timeJoinedWithZone.getOffset() + ")" + "\n"
                + "``("+lengthJoined+")``";

        if (user == ctx.getAuthor()) {
            description = "關於此使用者的資訊";
        } else {
            description = "關於使用者" + member.getUser().getName() + "的資訊";
        }

        EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                .setTitle("使用者資訊")
                .setDescription(description)
                .addField("使用者名稱：", member.getUser().getName() + "#" + member.getUser().getDiscriminator(), false)
                .addField("在此群的暱稱", member.getNickname() != null ? member.getNickname() : "無", false)
                .addField("上線狀態", "還沒寫", false)
                .addField("在伺服器最高身分組", member.getRoles().get(0).getAsMention(), false)
                .addField("加入伺服器時間", timeJoinedString, false)
                .addField("帳號創建時間", timeCreatedString, false)
                .setThumbnail(member.getUser().getAvatarUrl())
                .setFooter(member.getUser().getName() + "#" + member.getUser().getDiscriminator() + "\n" +
                        "ID:" + member.getUser().getId(), member.getUser().getAvatarUrl());

        ctx.getChannel().sendMessageEmbeds(embed.build()).queue();


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
    public List<String> getAliases() {
        return List.of("uinfo", "ui");
    }

    @Override
    public String getName() {
        return "userinfo";
    }

    @Override
    public String getCategory() {
        return "discordinfo";
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
