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
import DRMCBot.Command.ICommand;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManagerV3 {
    private final HashMap<String, ArrayList<ICommand>> category = new HashMap<>();
    private final HashMap<String, String> categoryDescription = new HashMap<>();

    public CommandManagerV3(EventWaiter eventWaiter) {
        /*------Add category------*/
        addCategory("discordinfo","Discord與機器人資訊類");
        addCategory("otherinfo","其他資訊類");
        addCategory("generation","生成工具類");
        addCategory("entertainment","娛樂類");
        addCategory("music","音樂播放類");
        addCategory("suggestion","建議類（目前僅限DRMC使用）");
        addCategory("ticket","管理員私訊窗口類（目前僅限DRMC使用）");
        addCategory("management","Discord與機器人資訊類");
        addCategory("giveaway","抽獎類（目前暫停使用）");
        addCategory("codebin","程式碼桶子工具類");
        addCategory("nocategory","未分類");
        /*------------------------*/

        /*------Add command------*/
        addCommand(new PingCommand(),"discordinfo");
        addCommand(new PasteCommand(),"codebin");
        addCommand(new KickCommand(),"management");
        addCommand(new MemeCommand(),"entertainment");
        addCommand(new JokeCommand(),"entertainment");
        addCommand(new InstagramCommand(),"otherinfo");
        addCommand(new HasteCommand(),"codebin");
        addCommand(new SetPrefixCommand(),"management");
        addCommand(new JoinCommand(),"music");
        addCommand(new LeaveCommand(),"music");
        addCommand(new PlayCommand(),"music");
        addCommand(new StopCommand(),"music");
        addCommand(new PauseCommand(),"music");
        addCommand(new ResumeCommand(),"music");
        addCommand(new QueueCommand(),"music");
        addCommand(new SkipCommand(),"music");
        addCommand(new NowPlayingCommand(),"music");
        addCommand(new MinecraftCommand(),"otherinfo");
        addCommand(new ServerinfoCommand(),"discordinfo");
        addCommand(new UserinfoCommand(),"discordinfo");
        addCommand(new CouponLeftCommand(),"otherinfo");
        addCommand(new GetMongoDbCollectionCommand(),"nocategory");
        addCommand(new SuggestionCommand(),"suggestion");
        addCommand(new ApproveCommand(),"suggestion");
        addCommand(new ConsiderCommand(),"suggestion");
        addCommand(new DenyCommand(),"suggestion");
        addCommand(new ImplementCommand(),"suggestion");
        addCommand(new QRCodeCommand2(),"generation");
        addCommand(new AccuWeatherCommand(),"otherinfo");
        addCommand(new HowToBullShitCommand(),"generation");
        addCommand(new BanCommand(),"management");
        addCommand(new ChinoCommand(),"entertainment");
        addCommand(new ReurlCommand(),"generation");
        addCommand(new TinyurlCommand(),"generation");
        addCommand(new PinMessageCommand(),"management");
        addCommand(new UnPinMessageCommand(),"management");
        addCommand(new HelpCommand(),"discordinfo");
        addCommand(new ChannelAllPinCommand(),"management");
        addCommand(new OpenTicketCommand(),"ticket");
        addCommand(new CloseTicketCommand(),"ticket");
        addCommand(new EventWaiterTestCommand(eventWaiter),"nocategory");
        addCommand(new RegularExpressionTestCommand(),"nocategory");
        addCommand(new CooldownTestCommand(),"nocategory");
        addCommand(new GiveawayWaitTestCommand(),"nocategory");
        addCommand(new StartGiveawayCommand(eventWaiter),"giveaway");
        addCommand(new GetEmojiNameCommand(),"nocategory");
        addCommand(new GetMessageReactionCommand(),"nocategory");
        addCommand(new CatCommand(),"entertainment");
        addCommand(new DogCommand(),"entertainment");
        addCommand(new UptimeCommand(),"discordinfo");
        addCommand(new ClearCommand(),"management");
        //addCommand(new AnimeMemeCommand());
        addCommand(new EightBallCommand(),"entertainment");
        addCommand(new NekoCommand(),"entertainment");
        addCommand(new CommandRunLengthCommand(null),"discordinfo");
        addCommand(new KitsuneCommand(),"entertainment");
        addCommand(new GetHypixelServerBoosterCommand(),"otherinfo");
        addCommand(new JLyricCommand(),"otherinfo");
        addCommand(new DuncanDiscordMonsterAPICommand(),"entertainment");
        addCommand(new NewMainQRCodeCommand(),"generation");
        addCommand(new ExchangeCommand(),"otherinfo");
        addCommand(new PaginatorTestCommand(),"nocategory");
        addCommand(new CategoryTestCommand(),"nocategory");
        addCommand(new ButtonizeTestCommand(),"nocategory");
        addCommand(new RemoveBackgroundCommand(),"nocategory");
        addCommand(new SaltyCommand(),"entertainment");
        addCommand(new LinguistParseJsonTestCommand(),"nocategory");
        addCommand(new SourceBinCommand(),"codebin");
        addCommand(new SayCommand(),"nocategory");
        /*-----------------------*/
    }

    private void addCategory(String name, String description) {
        boolean nameFound = categoryDescription.containsKey(name);

        if (nameFound) {
            throw new IllegalArgumentException("A category with this name is already present");
        }

        category.put(name, new ArrayList<>());
        categoryDescription.put(name, description);
    }

    private void addCommand(ICommand cmd,String categoryName) {
        boolean nameFound = false;
        for (String key : category.keySet()) {
            nameFound = category.get(key).stream().anyMatch(it -> it.getName().equalsIgnoreCase(cmd.getName()));
            if (nameFound) {
                break;
            }
        }

        if (nameFound) {
            throw new IllegalArgumentException("A command with this name is already present");
        }

        try {
            category.get(categoryName).add(cmd);
        } catch (NullPointerException e) {
            throw new NullPointerException("Unknown Category: " + categoryName);
        }
    }

    @Nullable
    public ICommand getCommand(String search) {
        String searchLower = search.toLowerCase();

        for (String key : category.keySet()) {
            for (ICommand cmd : category.get(key)) {
                if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                    return cmd;
                }
            }
        }

        return null;
    }

    void handle(GuildMessageReceivedEvent event, String prefix) throws Exception {
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(prefix), "")
                .split("\\s+");
        String invoke = split[0].toLowerCase();
        ICommand cmd = this.getCommand(invoke);

        if (cmd != null) {
            event.getChannel().sendTyping().queue();
            List<String> args = Arrays.asList(split).subList(1, split.length);

            CommandContext ctx = new CommandContext(event, args);

            cmd.handle(ctx);
        }
    }
}
