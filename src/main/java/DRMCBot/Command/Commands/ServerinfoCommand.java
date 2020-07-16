package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Region;
import net.dv8tion.jda.api.entities.*;

import javax.swing.*;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
        String two_factor = "";
        String timeCreatedString = "";
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
            if (member.getOnlineStatus() == OnlineStatus.ONLINE) {
                online += 1;
            } else if (member.getOnlineStatus() == OnlineStatus.IDLE) {
                idle += 1;
            } else if (member.getOnlineStatus() == OnlineStatus.DO_NOT_DISTURB) {
                dnd += 1;
            } else {
                offline += 1;
            }
        }

        if (guild.getRequiredMFALevel() == Guild.MFALevel.TWO_FACTOR_AUTH) {
            if_two_factor = true;
        } else {

            if_two_factor = false;
        }

        if (verificationLevel == Guild.VerificationLevel.NONE) {
            verification_level = "無";
        } else if (verificationLevel == Guild.VerificationLevel.LOW) {
            verification_level = "低";
        } else if (verificationLevel == Guild.VerificationLevel.MEDIUM) {
            verification_level = "中";
        } else if (verificationLevel == Guild.VerificationLevel.HIGH) {
            verification_level = "高";
        } else if (verificationLevel == Guild.VerificationLevel.VERY_HIGH) {
            verification_level = "最高";
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

        if (guild.getRegion() == Region.AMSTERDAM || guild.getRegion() == Region.VIP_AMSTERDAM) {
            region = "阿姆斯特丹";
        } else if (guild.getRegion() == Region.BRAZIL || guild.getRegion() == Region.VIP_BRAZIL) {
            region = "巴西";
        } else if (guild.getRegion() == Region.EU_CENTRAL || guild.getRegion() == Region.VIP_EU_CENTRAL) {
            region = "歐洲中部";
        } else if (guild.getRegion() == Region.EU_WEST || guild.getRegion() == Region.VIP_EU_WEST) {
            region = "歐洲西部";
        } else if (guild.getRegion() == Region.EUROPE) {
            region = "歐洲";
        } else if (guild.getRegion() == Region.FRANKFURT || guild.getRegion() == Region.VIP_FRANKFURT) {
            region = "法蘭克福";
        } else if (guild.getRegion() == Region.HONG_KONG) {
            region = "香港";
        } else if (guild.getRegion() == Region.INDIA) {
            region = "印度";
        } else if (guild.getRegion() == Region.JAPAN || guild.getRegion() == Region.VIP_JAPAN) {
            region = "日本";
        } else if (guild.getRegion() == Region.LONDON || guild.getRegion() == Region.VIP_LONDON) {
            region = "倫敦";
        } else if (guild.getRegion() == Region.RUSSIA) {
            region = "俄羅斯";
        } else if (guild.getRegion() == Region.SINGAPORE || guild.getRegion() == Region.VIP_SINGAPORE) {
            region = "新加坡";
        } else if (guild.getRegion() == Region.SOUTH_AFRICA || guild.getRegion() == Region.VIP_SOUTH_AFRICA) {
            region = "非洲南部";
        } else if (guild.getRegion() == Region.SOUTH_KOREA || guild.getRegion() == Region.VIP_SOUTH_KOREA) {
            region = "南韓";
        } else if (guild.getRegion() == Region.SYDNEY || guild.getRegion() == Region.VIP_SYDNEY) {
            region = "雪梨";
        } else if (guild.getRegion() == Region.US_CENTRAL || guild.getRegion() == Region.VIP_US_CENTRAL) {
            region = "美國中部";
        } else if (guild.getRegion() == Region.US_EAST || guild.getRegion() == Region.VIP_US_EAST) {
            region = "美國東部";
        } else if (guild.getRegion() == Region.US_SOUTH || guild.getRegion() == Region.VIP_US_SOUTH) {
            region = "美國南部";
        } else if (guild.getRegion() == Region.US_WEST || guild.getRegion() == Region.VIP_US_WEST) {
            region = "美國西部";
        }

        timeCreatedString = formattime(timeCreatedWithZone.getYear()) + "/"
                + formattime(timeCreatedWithZone.getMonthValue()) + "/"
                + formattime(timeCreatedWithZone.getDayOfMonth()) + "\n"
                + formattime(timeCreatedWithZone.getHour()) + ":"
                + formattime(timeCreatedWithZone.getMinute()) + ":"
                + formattime(timeCreatedWithZone.getSecond()) + "\n"
                + "(UTC"
                + timeCreatedWithZone.getOffset()
                + ")";

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
        return "serverinfo";
    }
}
