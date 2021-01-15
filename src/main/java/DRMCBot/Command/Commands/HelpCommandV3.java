package DRMCBot.Command.Commands;

import DRMCBot.Category.ICategory;
import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.CommandManagerV2;
import DRMCBot.CommandManagerV3;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class HelpCommandV3 implements ICommand {
    private final CommandManagerV3 commandManager;

    public HelpCommandV3(CommandManagerV3 commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void handle(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        User botowner = ctx.getJDA().getUserById("470516498050580480");
        String output = "";
        EmbedBuilder embed = EmbedUtils.getDefaultEmbed();
        embed.setAuthor(ctx.getJDA().getSelfUser().getName() + "#" + ctx.getJDA().getSelfUser().getDiscriminator(), null, ctx.getJDA().getSelfUser().getAvatarUrl());
        embed.setTitle("指令幫助");
        embed.addField("查詢方法", "```help [類別/指令名稱]```", false);
        if (args.isEmpty()) {
            TreeMap<String, ICategory> category = commandManager.getCategoryDescription();
            List<String> outputList = category.keySet().stream().map(key -> "**" + key + "**－" + (category.get(key).getDescription() == null ? "請等待作者新增描述" : category.get(key).getDescription())).collect(toList());
            output = String.join("\n", outputList);
            embed.addField("類別選項", output, false);
        } else {
            ICategory category = commandManager.getCategory(args.get(0));
            if (category != null) {
                List<String> outputList = category.getCommand().stream().map(cmd -> "**" + cmd.getName() + "**－" + (cmd.getdescription() == null ? "請等待作者新增描述" : cmd.getdescription())).collect(toList());
                output = String.join("\n", outputList);
                embed.addField(category.getDescription(), output, false);
            } else {
                ICommand cmd = commandManager.getCommand(args.get(0));
                if (cmd == null) {
                    TreeMap<String, ICategory> categories = commandManager.getCategoryDescription();
                    List<String> outputList = categories.keySet().stream().map(key -> "**" + key + "**－" + (categories.get(key).getDescription() == null ? "請等待作者新增描述" : categories.get(key).getDescription())).collect(toList());
                    output = String.join("\n", outputList);
                    embed.addField("類別選項", output, false);
                } else {
                    if (cmd.gethelpembed() != null) {
                        ctx.getChannel().sendMessage(cmd.gethelpembed().build()).queue();
                        return;
                    } else {
                        ctx.getChannel().sendMessage("請等待作者新增！").queue();
                        return;
                    }
                }
            }
        }
        embed.setFooter("機器人由「" + botowner.getName() + "#" + botowner.getDiscriminator() + "」製作", botowner.getAvatarUrl());
        ctx.getChannel().sendMessage(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "help";
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
