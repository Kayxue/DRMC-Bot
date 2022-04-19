package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.util.HashMap;
import java.util.List;

public class SaltyCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws Exception {
        List<Member> members = ctx.getMessage().getMentionedMembers();
        if (members.isEmpty()) {
            ctx.getChannel().sendMessage("請提及成員！").queue();
            return;
        }
        EmbedBuilder embed = EmbedUtils.embedImage("https://api.alexflipnote.dev/salty?image=" + members.get(0).getUser().getAvatarUrl());
        ctx.getChannel().sendMessageEmbeds(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "salty";
    }

    @Override
    public String getCategory() {
        return "entertainment";
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
