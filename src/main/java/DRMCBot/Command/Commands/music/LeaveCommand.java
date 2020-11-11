package DRMCBot.Command.Commands.music;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class LeaveCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel=ctx.getChannel();
        AudioManager audioManager=ctx.getGuild().getAudioManager();

        if (!audioManager.isConnected()){
            channel.sendMessage("I'm not connected to a voice channel").queue();
        }

        VoiceChannel voiceChannel=audioManager.getConnectedChannel();

        if (!voiceChannel.getMembers().contains(ctx.getMember())){
            channel.sendMessage("You have to be in the same voice channel as me to use this").queue();
            return;
        }

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
    public EmbedBuilder gethelpembed() {
        return null;
    }
}
