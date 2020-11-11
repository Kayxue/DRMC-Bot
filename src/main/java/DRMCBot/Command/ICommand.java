package DRMCBot.Command;

import net.dv8tion.jda.api.EmbedBuilder;

import java.util.List;

public interface ICommand {
    void handle(CommandContext ctx) throws Exception;

    String getName();

    String getCategory();

    String getdescription();

    EmbedBuilder gethelpembed();

    default List<String> getAliases(){
        return List.of();
    }
}
