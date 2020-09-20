package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;

public class ChannelAllPinCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        List<Message> messages=ctx.getChannel().retrievePinnedMessages().complete();
        Emote pinemote = ctx.getJDA().getEmotesByName("pinani", true).get(0);
        EmbedBuilder embed = EmbedUtils.defaultEmbed()
                .setTitle(pinemote.getAsMention() + " 此頻道之全部釘選訊息");
        String output = "";
        if (messages.isEmpty()) {
            embed.setDescription("此頻道沒有任何釘選訊息");
        } else {
            for (Message message : messages) {
                output += "發送者："+message.getAuthor().getName()+"#"+message.getAuthor().getDiscriminator()+"\n";
                output += "訊息內容：" + (message.getContentRaw().isEmpty() ? "（無文字訊息，請查看釘選訊息列表）" : message.getContentRaw()) + "\n";
                output += "----------------------------------------\n";
            }
            embed.setDescription("```" + output + "```");
        }
        ctx.getChannel().sendMessage(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "channelpin";
    }
}
