package DRMCBot.Utils;

import DRMCBot.lavaplayer.GuildMusicManager;
import DRMCBot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class MusicPlayerController extends ListenerAdapter {
    private final Guild playingGuild;
    public TextChannel messageShowChannel;
    private Message playerControllerMessage;
    private final JDA jda;
    private final String PLAYORPAUSE = "\u23EF";
    private final String SKIP = "\u23ED";
    private final String MUTE = "\uD83D\uDD07";
    private final String LOWERVOLUME = "\uD83D\uDD09";
    private final String LOUDERVOLUME = "\uD83D\uDD0A";
    private final String REPEAT = "\uD83D\uDD01";
    private final String STOP = "\u23F9\uFE0F";
    private int originalVolume = 100;
    private boolean isMuting = false;
    private AudioTrack nowPlaying;


    public MusicPlayerController(TextChannel messageShowChannel) {
        this.messageShowChannel = messageShowChannel;
        this.playingGuild = messageShowChannel.getGuild();
        this.jda = playingGuild.getJDA();
    }

    public void editPlaying(AudioTrack track) {
        if (playerControllerMessage == null) {
            EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                    .setTitle("\u25B6正在播放：" + track.getInfo().title + " by " + track.getInfo().author, track.getInfo().uri);
            playerControllerMessage = messageShowChannel.sendMessageEmbeds(embed.build()).complete();
            nowPlaying = track;
            jda.addEventListener(this);
            new Thread(() -> {
                playerControllerMessage.addReaction(PLAYORPAUSE).queue();
                playerControllerMessage.addReaction(SKIP).queue();
                playerControllerMessage.addReaction(MUTE).queue();
                playerControllerMessage.addReaction(LOWERVOLUME).queue();
                playerControllerMessage.addReaction(LOUDERVOLUME).queue();
                playerControllerMessage.addReaction(REPEAT).queue();
                playerControllerMessage.addReaction(STOP).queue();
            }).start();
        } else {
            EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                    .setTitle("\u25B6正在播放：" + track.getInfo().title + " by " + track.getInfo().author, track.getInfo().uri);
            nowPlaying = track;
            playerControllerMessage = playerControllerMessage.editMessageEmbeds(embed.build()).complete();
        }
    }

    public void removeController() {
        if (playerControllerMessage != null) {
            EmbedBuilder embed = EmbedUtils.getDefaultEmbed().setTitle("播放已結束");
            playerControllerMessage.editMessageEmbeds(embed.build()).queue();
            playerControllerMessage.clearReactions().queue();
        }
        jda.removeEventListener(this);
        messageShowChannel = null;
        playerControllerMessage = null;
        isMuting = false;
        originalVolume = 100;
    }

    public void editPausing(boolean isPausing) {
        if (isPausing) {
            EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                    .setTitle("\u23F8播放暫停中：" + nowPlaying.getInfo().title + " by " + nowPlaying.getInfo().author, nowPlaying.getInfo().uri);
            playerControllerMessage = playerControllerMessage.editMessageEmbeds(embed.build()).complete();
        } else {
            EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                    .setTitle("\u25B6正在播放：" + nowPlaying.getInfo().title + " by " + nowPlaying.getInfo().author, nowPlaying.getInfo().uri);
            playerControllerMessage = playerControllerMessage.editMessageEmbeds(embed.build()).complete();
        }
    }

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        if (event.getMessageIdLong() == playerControllerMessage.getIdLong() && !event.getUser().isBot()) {
            final TextChannel channel = (TextChannel) event.getChannel();
            final Member self = event.getGuild().getSelfMember();
            final GuildVoiceState selfVoiceState = self.getVoiceState();

            if (!selfVoiceState.inAudioChannel()) {
                channel.sendMessage("我不在語音頻道內！").queue();
                return;
            }

            final Member member = event.getMember();
            final GuildVoiceState memberVoiceState = member.getVoiceState();

            if (!memberVoiceState.inAudioChannel()){
                channel.sendMessage("你必須加入一個語音頻道！").queue();
                return;
            }


            if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
                channel.sendMessage("您跟我在不同頻道！").queue();
                return;
            }

            final GuildMusicManager manager= PlayerManager.getInstance().getMusicManager(channel);
            switch (event.getReactionEmote().getName()) {
                case PLAYORPAUSE -> {
                    playerControllerMessage.removeReaction(PLAYORPAUSE, event.getUser()).queue();
                    if (!manager.audioPlayer.isPaused()) {
                        final AudioPlayer audioPlayer = manager.audioPlayer;
                        if (audioPlayer.getPlayingTrack() == null) {
                            channel.sendMessage("目前沒有音樂正在播放！").queue(message -> {
                                message.delete().queueAfter(5, TimeUnit.SECONDS);
                            });
                            return;
                        }
                        manager.audioPlayer.setPaused(true);
                        editPausing(true);
                        channel.sendMessage("歌曲已暫停").queue(message -> {
                            message.delete().queueAfter(5, TimeUnit.SECONDS);
                        });
                    } else {
                        final AudioPlayer audioPlayer = manager.audioPlayer;

                        if (audioPlayer.getPlayingTrack() == null) {
                            channel.sendMessage("目前沒有音樂正在播放！").queue(message -> {
                                message.delete().queueAfter(5, TimeUnit.SECONDS);
                            });
                            return;
                        }
                        manager.audioPlayer.setPaused(false);
                        editPausing(false);
                        channel.sendMessage("歌曲已繼續播放").queue(message -> {
                            message.delete().queueAfter(5, TimeUnit.SECONDS);
                        });
                    }

                }
                case SKIP -> {
                    playerControllerMessage.removeReaction(SKIP, event.getUser()).queue();
                    final AudioPlayer audioPlayer = manager.audioPlayer;

                    if (audioPlayer.getPlayingTrack() == null) {
                        channel.sendMessage("目前沒有音樂正在播放！").queue(message -> {
                            message.delete().queueAfter(5, TimeUnit.SECONDS);
                        });
                        return;
                    }
                    manager.scheduler.nextTrack();

                }
                case MUTE -> {
                    playerControllerMessage.removeReaction(MUTE, event.getUser()).queue();
                    if (isMuting) {
                        manager.audioPlayer.setVolume(originalVolume);
                        channel.sendMessage("音量已設定成" + manager.audioPlayer.getVolume() + "%").queue(message -> {
                            message.delete().queueAfter(5, TimeUnit.SECONDS);
                        });
                        isMuting = false;
                    } else {
                        originalVolume = manager.audioPlayer.getVolume();
                        manager.audioPlayer.setVolume(0);
                        channel.sendMessage("音量已設定成" + manager.audioPlayer.getVolume() + "%").queue(message -> {
                            message.delete().queueAfter(5, TimeUnit.SECONDS);
                        });
                        isMuting = true;
                    }

                }
                case LOWERVOLUME -> {
                    playerControllerMessage.removeReaction(LOWERVOLUME, event.getUser()).queue();
                    if (isMuting) {
                        isMuting = false;
                        if (originalVolume - 10 < 0) {
                            manager.audioPlayer.setVolume(0);
                        } else {
                            manager.audioPlayer.setVolume(originalVolume - 10);
                        }
                    } else {
                        if (manager.audioPlayer.getVolume() - 10 < 0) {
                            manager.audioPlayer.setVolume(0);
                        } else {
                            manager.audioPlayer.setVolume(manager.audioPlayer.getVolume() - 10);
                        }
                    }
                    channel.sendMessage("音量已設定成" + manager.audioPlayer.getVolume() + "%").queue(message -> {
                        message.delete().queueAfter(5, TimeUnit.SECONDS);
                    });

                }
                case LOUDERVOLUME -> {
                    playerControllerMessage.removeReaction(LOUDERVOLUME, event.getUser()).queue();
                    if (isMuting) {
                        isMuting = false;
                        if (originalVolume + 10 > 100) {
                            manager.audioPlayer.setVolume(100);
                        } else {
                            manager.audioPlayer.setVolume(originalVolume + 10);
                        }
                    } else {
                        if (manager.audioPlayer.getVolume() + 10 > 100) {
                            manager.audioPlayer.setVolume(100);
                        } else {
                            manager.audioPlayer.setVolume(manager.audioPlayer.getVolume() + 10);
                        }
                    }
                    channel.sendMessage("音量已設定成" + manager.audioPlayer.getVolume() + "%").queue(message -> {
                        message.delete().queueAfter(5, TimeUnit.SECONDS);
                    });
                }
                case REPEAT -> {
                    playerControllerMessage.removeReaction(REPEAT, event.getUser()).queue();
                    final boolean newRepeating = !manager.scheduler.repeating;
                    manager.scheduler.repeating = newRepeating;
                    channel.sendMessageFormat("重複播放模式已成功%s", newRepeating ? "開啟" : "關閉").queue(message -> {
                        message.delete().queueAfter(5, TimeUnit.SECONDS);
                    });
                }
                case STOP -> {
                    manager.scheduler.player.stopTrack();
                    manager.scheduler.queue.clear();
                    removeController();
                    channel.sendMessage("已停止播放並清空音樂列表").queue();
                }
            }
        }
    }
}
