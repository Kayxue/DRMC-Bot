package DRMCBot;

import DRMCBot.Category.Categories.*;
import DRMCBot.Category.ICategory;
import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.Swing.BotController;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import java.util.*;
import java.util.regex.Pattern;

public class CommandManagerV3 {
    private final TreeMap<String, ICategory> category = new TreeMap<>();
    public BotController botController;
    public JDA jda;

    public CommandManagerV3(EventWaiter eventWaiter) {


        /*------Add category------*/
        addCategory(new DiscordInfoCategory(this));
        addCategory(new NoCategory(eventWaiter));
        addCategory(new MusicCategory(eventWaiter));
        addCategory(new CodeCategory());
        addCategory(new EntertainmentCategory());
        addCategory(new GenerationCategory());
        addCategory(new GiveawayCategory(eventWaiter));
        addCategory(new ManagementCategory(this));
        addCategory(new OtherInfoCategory());
        addCategory(new SuggestionCategory());
        addCategory(new TicketCategory());
        addCategory(new OwnerCategory(this));
        /*------------------------*/
    }

    private void addCategory(ICategory categorytoadd) {
        boolean nameFound = category.containsKey(categorytoadd.getName());

        if (nameFound) {
            throw new IllegalArgumentException("Category adding Error: Already a category called \"" + categorytoadd.getName() + "\"");
        }

        for (ICommand commandtoadd : categorytoadd.getCommand()) {
            for (String key : category.keySet()) {
                nameFound = category.get(key).getCommand().stream().anyMatch(it -> it.getName().equalsIgnoreCase(commandtoadd.getName()));
                if (nameFound) {
                    throw new IllegalArgumentException("Category adding Error: Command \"" + commandtoadd.getName() + "\"exist in \"" + categorytoadd.getClass().getName() + "\" class and \"" + category.get(key).getClass().getName() + "\"");
                }
            }
        }

        category.put(categorytoadd.getName(), categorytoadd);
    }

    public long returnCommandsCount() {
        int toReturn = 0;
        for (String categoryKeys : category.keySet()) {
            toReturn += category.get(categoryKeys).getCommand().size();
        }
        return toReturn;
    }

    public ICategory getCategory(String categoryname) {
        try {
            return category.get(categoryname);
        } catch (NullPointerException ignored) {
            return null;
        }
    }

    public TreeMap<String, ICategory> getCategoryDescription() {
        return category;
    }

    @Nullable
    public ICommand getCommand(String search) {
        String searchLower = search.toLowerCase();

        for (String key : category.keySet()) {
            for (ICommand cmd : category.get(key).getCommand()) {
                if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                    return cmd;
                }
            }
        }

        return null;
    }

    public void setJda(JDA jda) {
        this.jda = jda;
        this.botController = new BotController(this, jda);
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
