package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.Category;

import java.time.*;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static java.util.Locale.*;

public class ServerinfoCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final Guild guild = ctx.getGuild();
        int usercount;
        int botcount;
        int online;
        int idle;
        int dnd;
        int offline;
        int membercount = guild.getMemberCount();
        String verification_level = "";
        String locale = "";
        String two_factor;
        String timeCreatedString;
        boolean if_two_factor;
        final List<TextChannel> textChannels = guild.getTextChannels();
        final List<VoiceChannel> voiceChannels = guild.getVoiceChannels();
        final List<Category> categories = guild.getCategories();
        final Guild.VerificationLevel verificationLevel = guild.getVerificationLevel();
        final OffsetDateTime timeCreated = guild.getTimeCreated();
        final ZonedDateTime timeCreatedWithZone = timeCreated.atZoneSameInstant(ZoneId.of("Asia/Taipei"));
        final String ownermention = guild.getOwner().getAsMention();

        List<Member> members = guild.getMembers();
        usercount = (int) members.stream().filter(member -> !member.getUser().isBot()).count();
        botcount = (int) members.stream().filter(member -> member.getUser().isBot()).count();
        online = (int) members.stream().filter(member -> member.getOnlineStatus().equals(OnlineStatus.ONLINE)).count();
        idle = (int) members.stream().filter(member -> member.getOnlineStatus().equals(OnlineStatus.IDLE)).count();
        dnd = (int) members.stream().filter(member -> member.getOnlineStatus().equals(OnlineStatus.DO_NOT_DISTURB)).count();
        offline = members.size() - online - idle - dnd;

        if_two_factor = guild.getRequiredMFALevel() == Guild.MFALevel.TWO_FACTOR_AUTH;

        switch (verificationLevel) {
            case NONE -> verification_level = "無";

            case LOW -> verification_level = "低";

            case MEDIUM -> verification_level = "中";

            case HIGH -> verification_level = "高";

            case VERY_HIGH -> verification_level = "最高";
        }

        if (if_two_factor) {
            if (verification_level.length() == 1) {
                two_factor = "開";
            } else {
                two_factor = "開啟";
            }
        } else {
            if (verification_level.length() == 1) {
                two_factor = "關";
            } else {
                two_factor = "關閉";
            }
        }

        Locale guildLocale = guild.getLocale();
        if (TAIWAN.equals(guildLocale)) {
            locale = "台灣";
        } else if (CHINA.equals(guildLocale)) {
            locale = "中國";
        } else if (FRENCH.equals(guildLocale)) {
            locale = "法文";
        } else if (GERMANY.equals(guildLocale)) {
            locale = "德國";
        } else if (ITALY.equals(guildLocale)) {
            locale = "義大利";
        } else if (JAPAN.equals(guildLocale)) {
            locale = "日本";
        } else if (UK.equals(guildLocale)) {
            locale = "英國";
        } else if (US.equals(guildLocale)) {
            locale = "美國";
        } else {
            locale = guildLocale.getCountry();
        }

        String TimeLength = "";
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Taipei"));
        long lengthHour = Duration.between(timeCreatedWithZone, now).getSeconds();
        if (lengthHour / 3600 < 24) {
            long hour = lengthHour / 3600;
            long minute = (lengthHour % 3600) / 60;
            long second = (lengthHour % 3600) % 60;
            if (hour != 0 || minute != 0) {
                if (hour != 0) {
                    TimeLength += hour + "小時";
                }
                if (minute != 0) {
                    TimeLength += minute + "分鐘";
                }
            } else {
                TimeLength += second + "秒";
            }
        } else {
            LocalDate nowlocal = now.toLocalDate();
            LocalDate timeCreatelocal = timeCreatedWithZone.toLocalDate();
            Period period = Period.between(timeCreatelocal, nowlocal);
            if (period.getYears() != 0) {
                TimeLength += period.getYears() + "年";
            }
            if (period.getMonths() != 0) {
                if (TimeLength.length() != 0) {
                    TimeLength += "又";
                }
                TimeLength += period.getMonths() + "個月";
            }
            if (period.getDays() != 0) {
                if (TimeLength.length() != 0) {
                    TimeLength += "又";
                }
                TimeLength += period.getDays() + "天";
            }
        }

        TimeLength += "之前";

        timeCreatedString = formattime(timeCreatedWithZone.getYear()) + "/"
                + formattime(timeCreatedWithZone.getMonthValue()) + "/"
                + formattime(timeCreatedWithZone.getDayOfMonth()) + "\n"
                + formattime(timeCreatedWithZone.getHour()) + ":"
                + formattime(timeCreatedWithZone.getMinute()) + ":"
                + formattime(timeCreatedWithZone.getSecond()) + "\n"
                + "(UTC" + timeCreatedWithZone.getOffset() + ")" + "\n"
                + "``(" + TimeLength + ")``";

        final EmbedBuilder embedBuilder = EmbedUtils.getDefaultEmbed()
                .setTitle("關於伺服器「" + guild.getName() + "」")
                .setDescription("關於伺服器" + guild.getName() + "的資訊")
                .addField("成員［" + membercount + "］：",
                        "使用者數：" + usercount + "\n" +
                                "機器人數：" + botcount + "\n" +
                                "上線人數：" + online + "\n" +
                                "閒置人數：" + idle + "\n" +
                                "勿擾人數：" + dnd + "\n" +
                                "離線人數：" + offline,
                        true)
                .addField("頻道［" + (textChannels.size()+voiceChannels.size()) + "］：",
                        "頻道類別數量：" + categories.size() + "\n" +
                                "文字頻道數量：" + textChannels.size() + "\n" +
                                "語音頻道數量：" + voiceChannels.size(),
                        true)
                .addField("安全層級：",
                        "驗證等級：" + verification_level + "\n" +
                                "兩步驗證：" + two_factor, true)
                .addField("伺服器地區",locale,true)
                .addField("伺服器創建時間", timeCreatedString,true)
                .addField("服主",ownermention,true)
                .setThumbnail(guild.getIconUrl())
                .setFooter("伺服器ID："+guild.getId()+"\n"+ctx.getAuthor().getName()+"#"+ctx.getAuthor().getDiscriminator(),ctx.getAuthor().getAvatarUrl());

        ctx.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
    }

    public String formattime(int i) {
        String a;
        if (String.valueOf(i).length() == 1) {
            a = "0" + i;
        } else {
            a = String.valueOf(i);
        }
        return a;
    }

    @Override
    public List<String> getAliases() {
        return List.of("sinfo", "si", "gi", "guildinfo");
    }

    @Override
    public String getName() {
        return "serverinfo";
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
