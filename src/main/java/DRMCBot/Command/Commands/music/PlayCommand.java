package DRMCBot.Command.Commands.music;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.Config;
import DRMCBot.lavaplayer.PlayerManager;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class PlayCommand implements ICommand {
    private final EventWaiter waiter;
    private final YouTube youTube;

    public PlayCommand(EventWaiter waiter){
        this.waiter = waiter;
        YouTube temp = null;

        try {
            temp = new YouTube.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    null
            )
                    .setApplicationName("Application")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        youTube = temp;
    }

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel=ctx.getChannel();
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

        if (!memberVoiceState.inVoiceChannel()){
            channel.sendMessage("Please join a voice channel").queue();
            return;
        }


        if (!selfVoiceState.inVoiceChannel()) {
            if (memberVoiceState.inVoiceChannel()) {
                AudioManager manager = ctx.getGuild().getAudioManager();
                manager.openAudioConnection(memberVoiceState.getChannel());
                selfJoinChannelWithTheCommand = true;
            } else {
                ctx.getChannel().sendMessage("please join a voice channel").queue();
                return;
            }
        }
        if (!selfJoinChannelWithTheCommand) {
            if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
                ctx.getChannel().sendMessage("we are in different channel").queue();
                return;
            }
        }


        String link=String.join(" ",ctx.getArgs());

        PlayerManager manager = PlayerManager.getInstance();
        if (!isUrl(link)) {
            youtubeSearch(link, ctx.getAuthor().getIdLong(), channel,
                    choosed -> {
                        switch (choosed) {
                            case "timeout" -> ctx.getChannel().sendMessage("Choose timeout").queue();
                            case "noresult" -> ctx.getChannel().sendMessage("No result").queue();
                            case "searcherror" -> ctx.getChannel().sendMessage("Search error").queue();
                            case "canceled" -> ctx.getChannel().sendMessage("Search Canceled").queue();
                            default -> manager.loadAndPlay(channel, choosed);
                        }
                    });
        } else {
            manager.loadAndPlay(channel,link);
        }
    }

    private boolean isUrl(String input){
        try{
            new URI(input);
            return true;
        }
        catch (URISyntaxException ignored){
            return false;
        }
    }

    public void youtubeSearch(String input,long author,TextChannel channel, Consumer<String> toPlay) {
        List<SearchResult> results;
        try {
            results = youTube.search()
                    .list("id,snippet")
                    .setQ("faded")
                    .setMaxResults(5L)
                    .setType("video")
                    .setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
                    .setKey(Config.get("youtubekey"))
                    .execute()
                    .getItems();
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
        for (int i=0;i<=4;i++) {
            resultstring.append("**#").append(i + 1).append(":**").append(results.get(i).getSnippet().getTitle()).append("\n");
        }
        EmbedBuilder embed = EmbedUtils.defaultEmbed()
                .setTitle("Search result (reply in 20s):")
                .setDescription(resultstring.toString());
        channel.sendMessage(embed.build()).queue();
        waiter.waitForEvent(
                GuildMessageReceivedEvent.class,
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
    public EmbedBuilder gethelpembed() {
        return null;
    }
}
