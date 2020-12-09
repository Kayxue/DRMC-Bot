package DRMCBot.lavaplayer;

import DRMCBot.Utils.MusicPlayerController;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    public final AudioPlayer player;
    public final BlockingQueue<AudioTrack> queue;
    public boolean repeating = false;
    public final MusicPlayerController controller;

    public TrackScheduler(AudioPlayer player, TextChannel channel){
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
        this.controller = new MusicPlayerController(channel);
    }

    public void queue(AudioTrack track) {
        if (!this.player.startTrack(track, true)) {
            this.queue.offer(track);
        } else {
            this.controller.editPlaying(track);
        }
    }

    public void nextTrack(){
        if (this.queue.size() >= 1) {
            this.controller.editPlaying(this.queue.element());
        }
        if (!this.player.startTrack(this.queue.poll(), false)) {
            this.controller.removeController();
        }
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext){
            if (this.repeating) {
                this.player.startTrack(track.makeClone(), false);
                return;
            }
            nextTrack();
        }
    }
}
