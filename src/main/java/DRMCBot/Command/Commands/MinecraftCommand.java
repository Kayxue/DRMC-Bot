package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import com.github.natanbc.reliqua.util.StatusCodeValidator;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MinecraftCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();

        if (args.size() < 2) {
            channel.sendMessage("正確用法：minecraft uuid/names <username/uuid>").queue();
        }

        final String item = args.get(0);
        final String id = args.get(1);
        if (item.equals("uuid")) {
            fetchUUID(id,(uuid)->{
                if (uuid == null) {
                    channel.sendMessage("找不到使用者名稱為" + id + "之使用者").queue();
                    return;
                }

                channel.sendMessage(id + "的uuid是" + uuid).queue();
            });
        }
        else if (item.equals("names")) {
            fetchNameHistory(id,(names)->{
                if (names == null) {
                    channel.sendMessage("此uuid不存在").queue();
                    return;
                }

                final String namesJoined = String.join(",", names);

                channel.sendMessageFormat("%s的名稱歷史：\n%s",id,namesJoined).queue();
            });
        }
        else {
            channel.sendMessageFormat("未知類型「%s」，請選擇uuid或names",item).queue();
        }
    }

    @Override
    public String getName() {
        return "minecraft";
    }

    private void fetchUUID(String username, Consumer<String> callback) {
        WebUtils.ins.getJSONObject(
                "https://api.mojang.com/users/profiles/minecraft/"+username,
                (builder)->builder.setStatusCodeValidator(StatusCodeValidator.ACCEPT_200)
        ).async(
                (json)->{
                    callback.accept(json.get("id").asText());
                },
                (error)->{
                    callback.accept(null);
                }
        );
    }

    private void fetchNameHistory(String uuid,Consumer<List<String>> callback) {
        WebUtils.ins.getJSONArray(
                "https://api.mojang.com/user/profiles/"+uuid+"/names",
                (builder)->builder.setStatusCodeValidator(StatusCodeValidator.ACCEPT_200)
        ).async(
                (json)->{
                    List<String> names = new ArrayList<>();
                    json.forEach((item)->names.add(item.get("name").asText()));
                    callback.accept(names);
                },
                (error)->{
                    callback.accept(null);
                }
        );
    }
}
