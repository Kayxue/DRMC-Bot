package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;

public class NewMainQRCodeCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws Exception {
        if (ctx.getArgs().isEmpty()) {
            ctx.getChannel().sendMessage("請輸入QRCode內容！").queue();
            return;
        }
        EmbedBuilder embed = EmbedUtils.embedImage("https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=" + String.join(" ", ctx.getArgs()));
        ctx.getChannel().sendMessage(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "qrcode";
    }
}
