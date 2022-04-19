package DRMCBot.Command.Commands.music;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.lavaplayer.PlayerManager;
import DRMCBot.lavaplayer.YoutubeSearcher;
import com.google.api.services.youtube.model.SearchResult;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class PlayCommand implements ICommand {
    private final EventWaiter waiter;
    private final YoutubeSearcher youtubeSearcher = new YoutubeSearcher();

    public PlayCommand(EventWaiter waiter){
        this.waiter = waiter;
    }

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel=(TextChannel) ctx.getChannel();
        final List<String> args = ctx.getArgs();

        if (args.isEmpty()){
            channel.sendMessage("Missing Arguments").queue();
            return;
        }

        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();
        boolean selfJoinChannelWithTheCommand = false;

        if (!memberVoiceState.inAudioChannel()){
            channel.sendMessage("請加入一個語音頻道！").queue();
            return;
        }


        if (!selfVoiceState.inAudioChannel()) {
            if (memberVoiceState.inAudioChannel()) {
                AudioManager manager = ctx.getGuild().getAudioManager();
                manager.openAudioConnection(memberVoiceState.getChannel());
                selfJoinChannelWithTheCommand = true;
            } else {
                ctx.getChannel().sendMessage("請加入一個頻道！").queue();
                return;
            }
        }
        if (!selfJoinChannelWithTheCommand) {
            if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
                ctx.getChannel().sendMessage("我們在不同頻道！").queue();
                return;
            }
        }

        String link=String.join(" ",ctx.getArgs());
        PlayerManager manager = PlayerManager.getInstance();

        if (!isUrl(link)) {
            youtubeSearch(link, ctx.getAuthor().getIdLong(), channel,
                    choosed -> {
                        switch (choosed) {
                            case "timeout" -> ctx.getChannel().sendMessage("選擇超時！").queue();
                            case "noresult" -> ctx.getChannel().sendMessage("沒有結果！").queue();
                            case "searcherror" -> ctx.getChannel().sendMessage("搜尋出錯！").queue();
                            case "canceled" -> ctx.getChannel().sendMessage("搜尋取消！").queue();
                            default -> manager.loadAndPlay(channel, choosed);
                        }
                    });
        } else {
            manager.loadAndPlay(channel,link);
        }
    }

    private boolean isUrl(String input){
        try{
            new URL(input);
            return true;
        }
        catch (MalformedURLException ignored){
            return false;
        }

        /*
        Pattern pattern = Pattern.compile("(http(s)?:\\/\\/(www\\.)?+(\\D{1,}\\.)+\\D{1,}(\\/)?+(.{1,})?)");
        return pattern.matcher(input).matches();


         */

    }


    public void youtubeSearch(String input,long author,TextChannel channel, Consumer<String> toPlay) {
        List<SearchResult> results;
        try {
            results = youtubeSearcher.searchVideos(input, 10);
        } catch (Exception e) {
            e.printStackTrace();
            toPlay.accept("searcherror");
            return;
        }

        if (results.isEmpty()) {
            toPlay.accept("noresult");
            return;
        }

        StringBuilder resultstring = new StringBuilder();
        for (int i=0;i<=9;i++) {
            resultstring.append("**#").append(i + 1).append(":**").append(results.get(i).getSnippet().getTitle()).append("\n");
        }

        EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                .setTitle("搜尋結果（20秒內回覆）：")
                .setDescription(resultstring.toString());

        Message botsent = channel.sendMessageEmbeds(embed.build()).complete();
        waiter.waitForEvent(
                MessageReceivedEvent.class,
                event -> {
                    int choose = -1;
                    try {
                        choose = Integer.parseInt(event.getMessage().getContentRaw());
                    } catch (Exception ignored) {

                    }
                    return !event.getAuthor().isBot() && event.getAuthor().getIdLong() == author && choose >= 0 && choose <= 10;
                },
                event -> {
                    int choosed = Integer.parseInt(event.getMessage().getContentRaw());
                    botsent.delete().queue();
                    event.getMessage().delete().queue();
                    if (choosed == 0) {
                        toPlay.accept("canceled");
                    } else {
                        String videoId = results.get(choosed - 1).getId().getVideoId();
                        toPlay.accept("https://www.youtube.com/watch?v=" + videoId);
                    }
                },
                20, TimeUnit.SECONDS,
                () -> {
                    toPlay.accept("timeout");
                }
        );
    }



    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getCategory() {
        return "music";
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
