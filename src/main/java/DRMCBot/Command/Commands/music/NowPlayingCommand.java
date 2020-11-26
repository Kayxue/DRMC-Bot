package DRMCBot.Command.Commands.music;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.lavaplayer.GuildMusicManager;
import DRMCBot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.loadingbar.LoadingBar;
import me.duncte123.loadingbar.LoadingBarConfig;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NowPlayingCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws Exception{
        final TextChannel channel=ctx.getChannel();
        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if (!selfVoiceState.inVoiceChannel()) {
            ctx.getChannel().sendMessage("我不在語音頻道內！").queue();
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()){
            channel.sendMessage("你必須加入一個語音頻道！").queue();
            return;
        }


        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            ctx.getChannel().sendMessage("您跟我在不同頻道！").queue();
            return;
        }

        final GuildMusicManager manager= PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        final AudioPlayer player = manager.audioPlayer;
        final AudioTrack track = player.getPlayingTrack();

        if (player.getPlayingTrack() == null) {
            ctx.getChannel().sendMessage("目前沒有音樂正在播放！").queue();
            return;
        }

        if (player.getPlayingTrack() == null) {
            ctx.getChannel().sendMessage("目前沒有音樂正在播放！").queue();
            return;
        }

        final AudioTrackInfo info = track.getInfo();

        final String loadingBarFile = "loadingBarNowPlaying.png";
        LoadingBarConfig config = LoadingBarConfig.defaultConfig()
                .setFillColor(Color.decode("#01afef"));
        try (FileOutputStream outputStream = new FileOutputStream(loadingBarFile)) {
            outputStream.write(LoadingBar.generateImage(((double) player.getPlayingTrack().getPosition() / (double) player.getPlayingTrack().getDuration()) * 100, config));
        } catch (IOException ignored) {

        }
        BufferedImage imageToEdit = ImageIO.read(new File(loadingBarFile));
        Graphics g = imageToEdit.getGraphics();
        g.setFont(g.getFont().deriveFont((float) 25));
        g.drawString(formatTime(player.getPlayingTrack().getPosition())+" / " + formatTime(player.getPlayingTrack().getDuration()), 73, 28);
        g.dispose();

        ImageIO.write(imageToEdit, "png", new File(loadingBarFile));
        File loadingbar = new File(loadingBarFile);
        channel.sendMessage(
                EmbedUtils.defaultEmbed()
                        .setDescription(String.format("**正在播放：** [%s](%s)\n%s %s － %s", info.title, info.uri, player.isPaused() ? "\u23F8" : "▶", formatTime(player.getPlayingTrack().getPosition()), formatTime(player.getPlayingTrack().getDuration())))
                        .setImage("attachment://" + loadingBarFile)
                        .build()
        ).addFile(loadingbar, loadingBarFile).queue();
        loadingbar.deleteOnExit();
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("np");
    }

    @Override
    public String getName() {
        return "nowplaying";
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

    private String formatTime(long timeInMillis){
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
