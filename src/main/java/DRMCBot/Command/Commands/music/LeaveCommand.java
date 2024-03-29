package DRMCBot.Command.Commands.music;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import DRMCBot.lavaplayer.GuildMusicManager;
import DRMCBot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.HashMap;
import java.util.List;

public class LeaveCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = (TextChannel) ctx.getChannel();
        AudioManager audioManager = ctx.getGuild().getAudioManager();

        if (!audioManager.isConnected()){
            channel.sendMessage("I'm not connected to a voice channel").queue();
        }

        AudioChannel voiceChannel=audioManager.getConnectedChannel();

        if (!voiceChannel.getMembers().contains(ctx.getMember())){
            channel.sendMessage("You have to be in the same voice channel as me to use this").queue();
            return;
        }

        final GuildMusicManager manager= PlayerManager.getInstance().getMusicManager((TextChannel) ctx.getChannel());

        manager.scheduler.repeating = false;
        manager.scheduler.queue.clear();
        manager.scheduler.player.stopTrack();
        manager.scheduler.controller.removeController();

        audioManager.closeAudioConnection();
        channel.sendMessage("Disconnect from your channel.").queue();
    }

    @Override
    public String getName() {
        return "leave";
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
