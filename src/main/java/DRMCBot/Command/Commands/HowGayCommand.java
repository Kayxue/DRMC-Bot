package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class HowGayCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws Exception {
        List<String> args = ctx.getArgs();
        int gay = new Random().nextInt(100);
        if (args.isEmpty()) {
            EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                    .setTitle("Gay r8 Machine")
                    .setDescription("你是" + gay + "%同性戀");
            ctx.getChannel().sendMessageEmbeds(embed.build()).queue();
        } else {
            if (ctx.getMessage().getMentionedMembers().size() == 0) {
                Member member = ctx.getGuild().getMemberById(args.get(0));
                EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                        .setTitle("Gay隨機機器")
                        .setDescription(member.getEffectiveName() + "是" + gay + "%同性戀");
                ctx.getChannel().sendMessageEmbeds(embed.build()).queue();
            } else {
                Member member = ctx.getMessage().getMentionedMembers().get(0);
                EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                        .setTitle("Gay隨機機器")
                        .setDescription(member.getEffectiveName() + "是" + gay + "%同性戀");
                ctx.getChannel().sendMessageEmbeds(embed.build()).queue();
            }
        }
    }

    @Override
    public String getName() {
        return "howgay";
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
