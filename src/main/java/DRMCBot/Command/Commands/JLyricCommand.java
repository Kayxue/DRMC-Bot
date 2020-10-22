package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import com.jagrosh.jlyrics.Lyrics;
import com.jagrosh.jlyrics.LyricsClient;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;

public class JLyricCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws Exception {
        LyricsClient lyricsClient = new LyricsClient();
        Lyrics lyrics = lyricsClient.getLyrics(String.join(" ", ctx.getArgs())).get();
        EmbedBuilder embed = EmbedUtils.defaultEmbed()
                .setTitle(lyrics.getTitle() + "-" + lyrics.getAuthor())
                .setDescription("```" + lyrics.getContent() + "```");
        ctx.getChannel().sendMessage(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "jlyric";
    }
}