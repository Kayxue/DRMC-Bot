package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.CommandManagerV2;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class HelpCommandV2 implements ICommand {
    private final CommandManagerV2 commandManager;

    public HelpCommandV2(CommandManagerV2 commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void handle(CommandContext ctx) {
        User botowner = ctx.getJDA().getUserById("470516498050580480");
        List<String> args = ctx.getArgs();
        EmbedBuilder embed = EmbedUtils.getDefaultEmbed();
        embed.setAuthor(ctx.getJDA().getSelfUser().getName() + "#" + ctx.getJDA().getSelfUser().getDiscriminator(), null, ctx.getJDA().getSelfUser().getAvatarUrl());
        embed.setTitle("指令幫助");
        embed.addField("查詢方法", "```help [類別/指令名稱]```", false);
        if (args.isEmpty()) {
            String fieldValue = "";
            TreeMap<String, String> category = commandManager.getCategoryDescription();
            for (String key : category.keySet()) {
                fieldValue += "**" + key + "**－" + category.get(key) + "\n";
            }
            embed.addField("類別選項", fieldValue, false);
        } else {
            List<ICommand> commands = commandManager.getCommands(args.get(0));
            if (commands == null) {
                ICommand cmd = commandManager.getCommand(args.get(0));
                if (cmd == null) {
                    ctx.getChannel().sendMessage("找不到此指令或類別！").queue();
                    StringBuilder fieldValue = new StringBuilder();
                    TreeMap<String, String> category = commandManager.getCategoryDescription();
                    for (String key : category.keySet()) {
                        fieldValue.append("**").append(key).append("**－").append(category.get(key)).append("\n");
                    }
                    embed.addField("類別選項", fieldValue.toString(), false);
                } else {
                    if (cmd.gethelpembed() == null) {
                        ctx.getChannel().sendMessage("請等待作者新增指令幫助！").queue();
                    } else {
                        ctx.getChannel().sendMessage(cmd.gethelpembed().build()).queue();
                    }
                    return;
                }
            } else {
                TreeMap<String, String> categoryDescription = commandManager.getCategoryDescription();
                StringBuilder fieldValue = new StringBuilder();
                for (ICommand cmd : commands) {
                    fieldValue.append("**").append(cmd.getName()).append("**－").append(cmd.getdescription() == null ? "請等待作者新增描述" : cmd.getdescription()).append("\n");
                }
                embed.addField(categoryDescription.get(args.get(0)), fieldValue.toString(), false);
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
