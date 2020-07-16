package DRMCBot.Command;

import java.util.List;

public interface ICommand {
    void handle(CommandContext ctx);

    String getName();

    default List<String> getAliases(){
        return List.of();
    }
}
