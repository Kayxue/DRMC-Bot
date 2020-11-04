package DRMCBot;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.Commands.*;
import DRMCBot.Command.Commands.Giveaway.StartGiveawayCommand;
import DRMCBot.Command.Commands.GiveawayTest.GiveawayWaitTestCommand;
import DRMCBot.Command.Commands.GiveawayTest.RegularExpressionTestCommand;
import DRMCBot.Command.Commands.Hypixel.GetHypixelServerBoosterCommand;
import DRMCBot.Command.Commands.PaginatorUtilTest.ButtonizeTestCommand;
import DRMCBot.Command.Commands.PaginatorUtilTest.CategoryTestCommand;
import DRMCBot.Command.Commands.PaginatorUtilTest.PaginatorTestCommand;
import DRMCBot.Command.Commands.Ticket.CloseTicketCommand;
import DRMCBot.Command.Commands.Ticket.OpenTicketCommand;
import DRMCBot.Command.Commands.admin.*;
import DRMCBot.Command.Commands.anime.ChinoCommand;
import DRMCBot.Command.Commands.anime.KitsuneCommand;
import DRMCBot.Command.Commands.anime.NekoCommand;
import DRMCBot.Command.Commands.music.*;
import DRMCBot.Command.Commands.reurl.ReurlCommand;
import DRMCBot.Command.Commands.reurl.TinyurlCommand;
import DRMCBot.Command.Commands.suggestion.*;
import DRMCBot.Command.Commands.EightBallCommand;
import DRMCBot.Command.ICommand;
import DRMCBot.Command.Commands.music.QueueCommand;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {
    private final List<ICommand> commands = new ArrayList<>();
    
    public CommandManager(EventWaiter eventWaiter){
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
        addCommand(new SuggestionCommand());
        addCommand(new ApproveCommand());
        addCommand(new ConsiderCommand());
        addCommand(new DenyCommand());
        addCommand(new ImplementCommand());
        addCommand(new QRCodeCommand2());
        addCommand(new AccuWeatherCommand());
        addCommand(new HowToBullShitCommand());
        addCommand(new BanCommand());
        addCommand(new ChinoCommand());
        addCommand(new ReurlCommand());
        addCommand(new TinyurlCommand());
        addCommand(new PinMessageCommand());
        addCommand(new UnPinMessageCommand());
        addCommand(new HelpCommand());
        addCommand(new ChannelAllPinCommand());
        addCommand(new OpenTicketCommand());
        addCommand(new CloseTicketCommand());
        addCommand(new EventWaiterTestCommand(eventWaiter));
        addCommand(new RegularExpressionTestCommand());
        addCommand(new CooldownTestCommand());
        addCommand(new GiveawayWaitTestCommand());
        addCommand(new StartGiveawayCommand(eventWaiter));
        addCommand(new GetEmojiNameCommand());
        addCommand(new GetMessageReactionCommand());
        addCommand(new CatCommand());
        addCommand(new DogCommand());
        addCommand(new UptimeCommand());
        addCommand(new ClearCommand());
        //addCommand(new AnimeMemeCommand());
        addCommand(new EightBallCommand());
        addCommand(new NekoCommand());
        addCommand(new CommandRunLengthCommand(this));
        addCommand(new KitsuneCommand());
        addCommand(new GetHypixelServerBoosterCommand());
        addCommand(new JLyricCommand());
        addCommand(new Duncte123DiscordMonsterAPICommand());
        addCommand(new NewMainQRCodeCommand());
        addCommand(new ExchangeCommand());
        addCommand(new PaginatorTestCommand());
        addCommand(new CategoryTestCommand());
        addCommand(new ButtonizeTestCommand());
        addCommand(new RemoveBackgroundCommand());
        addCommand(new SaltyCommand());
        addCommand(new LinguistParseJsonTestCommand());
        addCommand(new SourceBinCommand());
    }

    private void addCommand(ICommand cmd){
        boolean nameFound = this.commands.stream().anyMatch((it)-> it.getName().equalsIgnoreCase(cmd.getName()));

        if(nameFound){
            throw new IllegalArgumentException("A command with this name is already present");
        }

        commands.add(cmd);
    }

    @Nullable
    public ICommand getCommand(String search){
        String searchLower = search.toLowerCase();

        for (ICommand cmd:this.commands){
            if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)){
                return cmd;
            }
        }

        return null;
    }

    void handle(GuildMessageReceivedEvent event,String prefix) throws Exception {
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
