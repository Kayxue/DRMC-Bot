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
import java.util.*;
import java.util.regex.Pattern;

public class CommandManagerV2 {
    private final TreeMap<String, ArrayList<ICommand>> category = new TreeMap<>();
    private final TreeMap<String, String> categoryDescription = new TreeMap<>();

    public CommandManagerV2(EventWaiter eventWaiter) {
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
        addCategory("code","程式碼桶子工具類");
        addCategory("nocategory","未分類");
        /*------------------------*/

        /*------Add command------*/
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
        addCommand(new PlayCommand(eventWaiter));
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
        addCommand(new HelpCommandV2(this));
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
        addCommand(new CommandRunLengthCommand(null));
        addCommand(new KitsuneCommand());
        addCommand(new GetHypixelServerBoosterCommand());
        addCommand(new JLyricCommand());
        addCommand(new DuncanDiscordMonsterAPICommand());
        addCommand(new NewMainQRCodeCommand());
        addCommand(new ExchangeCommand());
        addCommand(new PaginatorTestCommand());
        addCommand(new CategoryTestCommand());
        addCommand(new ButtonizeTestCommand());
        addCommand(new RemoveBackgroundCommand());
        addCommand(new SaltyCommand());
        addCommand(new LinguistParseJsonTestCommand());
        addCommand(new SourceBinCommand());
        addCommand(new SayCommand());
        addCommand(new DiscordJSDocumentationCommand());
        addCommand(new RepeatCommand());
        /*-----------------------*/
    }

    private void addCategory(String Engname, String description) {
        boolean nameFound = categoryDescription.containsKey(Engname);

        if (nameFound) {
            throw new IllegalArgumentException("A category with this name is already present");
        }

        category.put(Engname, new ArrayList<>());
        categoryDescription.put(Engname, description);
    }

    private void addCommand(ICommand cmd) {
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
            if (cmd.getCategory() == null) {
                throw new IllegalArgumentException();
            }
            category.get(cmd.getCategory()).add(cmd);
        } catch (NullPointerException e) {
            throw new NullPointerException("Unknown Category: " + cmd.getCategory());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(cmd.getName() + " doesn't have any category");
        }
    }

    public List<ICommand> getCommands(String categorytofind) {
        if (category.keySet().contains(categorytofind)) {
            return category.get(categorytofind);
        } else {
            return null;
        }
    }

    public TreeMap<String, String> getCategoryDescription() {
        return categoryDescription;
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
