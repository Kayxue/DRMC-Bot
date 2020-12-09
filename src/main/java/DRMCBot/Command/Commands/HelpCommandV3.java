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
            String fieldValue = "";
            TreeMap<String, ICategory> category = commandManager.getCategoryDescription();
            for (String key : category.keySet()) {
                fieldValue += "**" + key + "**－" + (category.get(key).getDescription() == null ? "請等待作者新增描述" : category.get(key).getDescription()) + "\n";
            }
            embed.addField("類別選項", fieldValue, false);
        } else {
            ICategory category = commandManager.getCategory(args.get(0));
            if (category != null) {
                for (ICommand cmd : category.getCommand()) {
                    output += "**" + cmd.getName() + "**－" + (cmd.getdescription() == null ? "請等待作者新增描述" : cmd.getdescription()) + "\n";
                }
                embed.addField(category.getDescription(), output, false);
            } else {
                ICommand cmd = commandManager.getCommand(args.get(0));
                if (cmd == null) {
                    String fieldValue = "";
                    TreeMap<String, ICategory> categories = commandManager.getCategoryDescription();
                    for (String key : categories.keySet()) {
                        fieldValue += "**" + key + "**－" + categories.get(key).getDescription() + "\n";
                    }
                    embed.addField("類別選項", fieldValue, false);
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
