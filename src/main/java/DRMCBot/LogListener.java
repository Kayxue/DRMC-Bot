package DRMCBot;

import DRMCBot.Database.DatabaseManager;
import DRMCBot.Database.MongoDbDataSource;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import javax.annotation.Nonnull;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static me.duncte123.botcommons.messaging.EmbedUtils.*;

public class LogListener extends ListenerAdapter {
    MongoDbDataSource mongoDbDataSource = new MongoDbDataSource();
    @Override
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent event) {
        JSONObject logchanneldata = mongoDbDataSource.getlogchannel(event.getGuild().getIdLong());
        if (logchanneldata.getBoolean("success")) {
            TextChannel logchannel = event.getGuild().getTextChannelById(logchanneldata.getLong("logchannelid"));
            EmbedBuilder embed = getDefaultEmbed()
                    .setTitle("有人加入語音頻道")
                    .addField("加入之使用者", event.getMember().getUser().getAsTag(), true)
                    .addField("加入之語音頻道", event.getChannelJoined().getName(), true)
                    .addField("目前人數", event.getChannelJoined().getMembers().size() + "" + (event.getChannelJoined().getUserLimit() == 0 ? "" : "/" + event.getChannelJoined().getUserLimit()), true)
                    .setColor(0x0DFC3D)
                    .setTimestamp(ZonedDateTime.now(ZoneId.of("Asia/Taipei")));
            logchannel.sendMessage(embed.build()).queue();
        }
    }

    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event) {
        JSONObject logchanneldata = mongoDbDataSource.getlogchannel(event.getGuild().getIdLong());
        if (logchanneldata.getBoolean("success")) {
            TextChannel logchannel = event.getGuild().getTextChannelById(logchanneldata.getLong("logchannelid"));
            EmbedBuilder embed = getDefaultEmbed()
                    .setTitle("有人離開了語音頻道")
                    .addField("離開之使用者", event.getMember().getAsMention() + "\n" + event.getMember().getUser().getAsTag(), true)
                    .addField("離開之語音頻道", event.getChannelLeft().getName(), true)
                    .addField("目前人數", event.getChannelLeft().getMembers().size() + "" + (event.getChannelLeft().getUserLimit() == 0 ? "" : "/" + event.getChannelLeft().getUserLimit()), true)
                    .setColor(0xF0301E)
                    .setTimestamp(ZonedDateTime.now(ZoneId.of("Asia/Taipei")));
            logchannel.sendMessage(embed.build()).queue();
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        while (Cache.ContentCache.keySet().size() > 100) {
            Cache.ContentCache.remove(Cache.ContentCache.keySet().toArray()[0]);
        }
        while (Cache.AttachmentsLinksList.keySet().size() > 100) {
            Cache.AttachmentsLinksList.remove(Cache.AttachmentsLinksList.keySet().toArray()[0]);
        }
        Cache.ContentCache.put(event.getMessageId(), event.getMessage().getContentRaw());
        if (event.getMessage().getAttachments().size() > 0) {
            List<String> AttachmentsLinks=event.getMessage().getAttachments().stream().map(Message.Attachment::getProxyUrl).collect(Collectors.toList());
            Cache.AttachmentsLinksList.put(event.getMessageId(), AttachmentsLinks);
        }
    }
}
