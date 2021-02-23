package DRMCBot.Command;

import java.util.HashMap;
import java.util.List;

public interface ICommand {
    void handle(CommandContext ctx) throws Exception;

    String getName();

    String getCategory();

    String getdescription();

    List<String> getUsages();

    List<String> getExamples();

    HashMap<String, HashMap<String, String>> getArguments();

    default List<String> getAliases(){
        return List.of();
    }
}
