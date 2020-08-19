package DRMCBot;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.Commands.*;
import DRMCBot.Command.Commands.admin.SetPrefixCommand;
import DRMCBot.Command.Commands.music.*;
import DRMCBot.Command.ICommand;
import DRMCBot.Command.Commands.music.QueueCommand;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {
    private final List<ICommand> commands = new ArrayList<>();

    public CommandManager(){
        addCommand(new PingCommand());
        addCommand(new PasteCommand());
        addCommand(new KickCommand());
        addCommand(new MemeCommand());
        addCommand(new JokeCommand());
        addCommand(new InstagramCommand());
        addCommand(new HasteCommand());
        addCommand(new SetPrefixCommand());
        addCommand(new JoinCommand());
        addCommand(new LeaveCommand());
        addCommand(new PlayCommand());
        addCommand(new StopCommand());
        addCommand(new PauseCommand());
        addCommand(new ResumeCommand());
        addCommand(new QueueCommand());
        addCommand(new SkipCommand());
        addCommand(new NowPlayingCommand());
        addCommand(new MinecraftCommand());
        addCommand(new ServerinfoCommand());
        addCommand(new UserinfoCommand());
        addCommand(new CouponLeftCommand());
        addCommand(new GetMongoDbCollectionCommand());
    }

    private void addCommand(ICommand cmd){
        boolean nameFound = this.commands.stream().anyMatch((it)-> it.getName().equalsIgnoreCase(cmd.getName()));

        if(nameFound){
            throw new IllegalArgumentException("A command with this name is already present");
        }

        commands.add(cmd);
    }

    @Nullable
    private ICommand getCommand(String search){
        String searchLower = search.toLowerCase();

        for (ICommand cmd:this.commands){
            if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)){
                return cmd;
            }
        }

        return null;
    }

    void handle(GuildMessageReceivedEvent event,String prefix){
        String[] split=event.getMessage().getContentRaw()
                .replaceFirst("(?i)"+ Pattern.quote(prefix),"")
                .split("\\s+");
        String invoke = split[0].toLowerCase();
        ICommand cmd=this.getCommand(invoke);

        if (cmd != null){
            event.getChannel().sendTyping().queue();
            List<String> args= Arrays.asList(split).subList(1,split.length);

            CommandContext ctx=new CommandContext(event,args);

            cmd.handle(ctx);
        }
    }
}
