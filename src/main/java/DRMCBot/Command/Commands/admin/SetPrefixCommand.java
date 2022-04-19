package DRMCBot.Command.Commands.admin;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.Database.DatabaseManager;
import DRMCBot.VeryBadDesign;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.List;

public class SetPrefixCommand implements ICommand{
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel=(TextChannel) ctx.getChannel();
        final List<String> args=ctx.getArgs();
        final Member member=ctx.getMember();

        if(!member.hasPermission(Permission.MANAGE_SERVER)){
            channel.sendMessage("你必須要有「管理伺服器」的權限！").queue();
            return;
        }

        if(args.isEmpty()){
            channel.sendMessage("缺少參數！").queue();
            return;
        }

        final String newPrefix=String.join("",args);
        updatePrefix(ctx.getGuild().getIdLong(),newPrefix);

        channel.sendMessageFormat("New prefix has been set to `%s`",newPrefix).queue();
    }

    @Override
    public String getName() {
        return "setprefix";
    }

    @Override
    public String getCategory() {
        return "management";
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

    private void updatePrefix(long guildId,String newPrefix){
        VeryBadDesign.PREFIXES.put(guildId,newPrefix);
        DatabaseManager.INSTANCE.setPrefix(guildId,newPrefix);

    }
}
