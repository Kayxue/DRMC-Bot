package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;

import java.time.*;
import java.util.HashMap;
import java.util.List;

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

        List<Member> members = guild.getMembers();
        usercount = (int) members.stream().filter(member -> !member.getUser().isBot()).count();
        botcount = (int) members.stream().filter(member -> member.getUser().isBot()).count();
        online = (int) members.stream().filter(member -> member.getOnlineStatus().equals(OnlineStatus.ONLINE)).count();
        idle = (int) members.stream().filter(member -> member.getOnlineStatus().equals(OnlineStatus.IDLE)).count();
        dnd = (int) members.stream().filter(member -> member.getOnlineStatus().equals(OnlineStatus.DO_NOT_DISTURB)).count();
        offline = members.size() - online - idle - dnd;

        if_two_factor = guild.getRequiredMFALevel() == Guild.MFALevel.TWO_FACTOR_AUTH;

        switch (verificationLevel) {
            case NONE -> verification_level = "???";

            case LOW -> verification_level = "???";

            case MEDIUM -> verification_level = "???";

            case HIGH -> verification_level = "???";

            case VERY_HIGH -> verification_level = "??????";
        }

        if (if_two_factor) {
            if (verification_level.length() == 1) {
                two_factor = "???";
            } else {
                two_factor = "??????";
            }
        } else {
            if (verification_level.length() == 1) {
                two_factor = "???";
            } else {
                two_factor = "??????";
            }
        }

        switch (guild.getRegion()) {
            case AMSTERDAM, VIP_AMSTERDAM -> region = "???????????????";

            case BRAZIL, VIP_BRAZIL -> region = "??????";

            case EU_CENTRAL, VIP_EU_CENTRAL -> region = "????????????";

            case EU_WEST, VIP_EU_WEST -> region = "????????????";

            case EUROPE -> region = "??????";

            case FRANKFURT, VIP_FRANKFURT -> region = "????????????";

            case INDIA -> region = "??????";

            case HONG_KONG -> region = "??????";

            case JAPAN, VIP_JAPAN -> region = "??????";

            case LONDON, VIP_LONDON -> region = "??????";

            case RUSSIA -> region = "?????????";

            case SINGAPORE, VIP_SINGAPORE -> region = "?????????";

            case SOUTH_AFRICA, VIP_SOUTH_AFRICA -> region = "????????????";

            case SOUTH_KOREA, VIP_SOUTH_KOREA -> region = "??????";

            case SYDNEY, VIP_SYDNEY -> region = "??????";

            case US_CENTRAL, VIP_US_CENTRAL -> region = "????????????";

            case US_EAST, VIP_US_EAST -> region = "????????????";

            case US_SOUTH, VIP_US_SOUTH -> region = "????????????";

            case US_WEST, VIP_US_WEST -> region = "????????????";
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
                    TimeLength += hour + "??????";
                }
                if (minute != 0) {
                    TimeLength += minute + "??????";
                }
            } else {
                TimeLength += second + "???";
            }
        } else {
            LocalDate nowlocal = now.toLocalDate();
            LocalDate timeCreatelocal = timeCreatedWithZone.toLocalDate();
            Period period = Period.between(timeCreatelocal, nowlocal);
            if (period.getYears() != 0) {
                TimeLength += period.getYears() + "???";
            }
            if (period.getMonths() != 0) {
                if (TimeLength.length() != 0) {
                    TimeLength += "???";
                }
                TimeLength += period.getMonths() + "??????";
            }
            if (period.getDays() != 0) {
                if (TimeLength.length() != 0) {
                    TimeLength += "???";
                }
                TimeLength += period.getDays() + "???";
            }
        }

        TimeLength += "??????";

        timeCreatedString = formattime(timeCreatedWithZone.getYear()) + "/"
                + formattime(timeCreatedWithZone.getMonthValue()) + "/"
                + formattime(timeCreatedWithZone.getDayOfMonth()) + "\n"
                + formattime(timeCreatedWithZone.getHour()) + ":"
                + formattime(timeCreatedWithZone.getMinute()) + ":"
                + formattime(timeCreatedWithZone.getSecond()) + "\n"
                + "(UTC" + timeCreatedWithZone.getOffset() + ")" + "\n"
                + "``(" + TimeLength + ")``";

        final EmbedBuilder embedBuilder = EmbedUtils.getDefaultEmbed()
                .setTitle("??????????????????" + guild.getName() + "???")
                .setDescription("???????????????" + guild.getName() + "?????????")
                .addField("?????????" + membercount + "??????",
                        "???????????????" + usercount + "\n" +
                                "???????????????" + botcount + "\n" +
                                "???????????????" + online + "\n" +
                                "???????????????" + idle + "\n" +
                                "???????????????" + dnd + "\n" +
                                "???????????????" + offline,
                        true)
                .addField("?????????" + (textChannels.size()+voiceChannels.size()) + "??????",
                        "?????????????????????" + categories.size() + "\n" +
                                "?????????????????????" + textChannels.size() + "\n" +
                                "?????????????????????" + voiceChannels.size(),
                        true)
                .addField("???????????????",
                        "???????????????" + verification_level + "\n" +
                                "???????????????" + two_factor, true)
                .addField("???????????????",region,true)
                .addField("?????????????????????", timeCreatedString,true)
                .addField("??????",ownermention,true)
                .setThumbnail(guild.getIconUrl())
                .setFooter("?????????ID???"+guild.getId()+"\n"+ctx.getAuthor().getName()+"#"+ctx.getAuthor().getDiscriminator(),ctx.getAuthor().getAvatarUrl());

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
