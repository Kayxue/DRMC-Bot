package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;

import java.time.*;
import java.util.Arrays;
import java.util.List;

public class ServerinfoCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final Guild guild = ctx.getGuild();
        int usercount = 0;
        int botcount = 0;
        int online = 0;
        int idle = 0;
        int dnd = 0;
        int offline = 0;
        int membercount = guild.getMemberCount();
        String verification_level = "";
        String region = "";
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

        for (Member member : guild.getMembers()) {
            if (!member.getUser().isBot()) {
                usercount += 1;
            } else {
                botcount += 1;
            }
            switch (member.getOnlineStatus()) {
                case ONLINE -> online += 1;

                case IDLE -> idle += 1;

                case DO_NOT_DISTURB -> dnd += 1;

                default -> offline += 1;
            }
        }



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

        botcount = membercount - usercount;

        switch (guild.getRegion()) {
            case AMSTERDAM, VIP_AMSTERDAM -> region = "阿姆斯特丹";

            case BRAZIL, VIP_BRAZIL -> region = "巴西";

            case EU_CENTRAL, VIP_EU_CENTRAL -> region = "歐洲中部";

            case EU_WEST, VIP_EU_WEST -> region = "歐洲西部";

            case EUROPE -> region = "歐洲";

            case FRANKFURT, VIP_FRANKFURT -> region = "法蘭克福";

            case INDIA -> region = "印度";

            case HONG_KONG -> region = "香港";

            case JAPAN, VIP_JAPAN -> region = "日本";

            case LONDON, VIP_LONDON -> region = "倫敦";

            case RUSSIA -> region = "俄羅斯";

            case SINGAPORE, VIP_SINGAPORE -> region = "新加坡";

            case SOUTH_AFRICA, VIP_SOUTH_AFRICA -> region = "非洲南部";

            case SOUTH_KOREA, VIP_SOUTH_KOREA -> region = "南韓";

            case SYDNEY, VIP_SYDNEY -> region = "雪梨";

            case US_CENTRAL, VIP_US_CENTRAL -> region = "美國中部";

            case US_EAST, VIP_US_EAST -> region = "美國東部";

            case US_SOUTH, VIP_US_SOUTH -> region = "美國南部";

            case US_WEST, VIP_US_WEST -> region = "美國西部";
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

        final EmbedBuilder embedBuilder = EmbedUtils.defaultEmbed()
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
                .addField("伺服器地區",region,true)
                .addField("伺服器創建時間", timeCreatedString,true)
                .addField("服主",ownermention,true)
                .setThumbnail(guild.getIconUrl())
                .setFooter("伺服器ID："+guild.getId()+"\n"+ctx.getAuthor().getName()+"#"+ctx.getAuthor().getDiscriminator(),ctx.getAuthor().getAvatarUrl());

        ctx.getChannel().sendMessage(embedBuilder.build()).queue();
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
    public EmbedBuilder gethelpembed() {
        return null;
    }
}
