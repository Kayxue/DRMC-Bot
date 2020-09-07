package DRMCBot.Command;

import java.util.List;

public interface ICommand {
    void handle(CommandContext ctx) throws Exception;

    String getName();

    default List<String> getAliases(){
        return List.of();
    }
}
