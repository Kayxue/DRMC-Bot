package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;

import java.time.OffsetDateTime;
import java.time.ZoneId;
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
                case ONLINE :
                    online += 1;
                    break;
                case IDLE :
                    idle += 1;
                    break;
                case DO_NOT_DISTURB :
                    dnd += 1;
                    break;
                default :
                    offline += 1;
                    break;
            }
        }

        if_two_factor = guild.getRequiredMFALevel() == Guild.MFALevel.TWO_FACTOR_AUTH;

        switch (verificationLevel) {
            case NONE :
                verification_level = "無";
                break;
            case LOW :
                verification_level = "低";
                break;
            case MEDIUM:
                verification_level = "中";
                break;
            case HIGH :
                verification_level = "高";
                break;
            case VERY_HIGH :
                verification_level = "最高";
                break;
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
            case AMSTERDAM:
            case VIP_AMSTERDAM :
                region = "阿姆斯特丹";
                break;
            case BRAZIL:
            case VIP_BRAZIL:
                region = "巴西";
                break;
            case EU_CENTRAL:
            case VIP_EU_CENTRAL:
                region = "歐洲中部";
                break;
            case EU_WEST:
            case VIP_EU_WEST:
                region = "歐洲西部";
                break;
            case EUROPE:
                region = "歐洲";
                break;
            case FRANKFURT:
            case VIP_FRANKFURT:
                region = "法蘭克福";
                break;
            case HONG_KONG:
                region = "香港";
                break;
            case INDIA:
                region = "印度";
                break;
            case JAPAN:
            case VIP_JAPAN:
                region = "日本";
                break;
            case LONDON:
            case VIP_LONDON:
                region = "倫敦";
                break;
            case RUSSIA:
                region = "俄羅斯";
                break;
            case SINGAPORE:
            case VIP_SINGAPORE:
                region = "新加坡";
                break;
            case SOUTH_AFRICA:
            case VIP_SOUTH_AFRICA:
                region = "非洲南部";
                break;
            case SOUTH_KOREA:
            case VIP_SOUTH_KOREA:
                region = "南韓";
                break;
            case SYDNEY:
            case VIP_SYDNEY:
                region = "雪梨";
                break;
            case US_CENTRAL:
            case VIP_US_CENTRAL:
                region = "美國中部";
                break;
            case US_EAST:
            case VIP_US_EAST:
                region = "美國東部";
                break;
            case US_SOUTH:
            case VIP_US_SOUTH:
                region = "美國南部";
                break;
            case US_WEST:
            case VIP_US_WEST:
                region = "美國西部";
                break;
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
        String a;
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
