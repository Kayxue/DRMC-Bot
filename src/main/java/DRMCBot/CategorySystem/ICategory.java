package DRMCBot.CategorySystem;

import DRMCBot.Command.ICommand;

import java.util.ArrayList;
import java.util.List;

public interface ICategory {
    List<ICommand> command = new ArrayList<>();

    default void addCommand(ICommand cmd) {
        boolean nameFoundInCategory = command.stream().anyMatch(it -> it.getName().equalsIgnoreCase(cmd.getName()));
        if (nameFoundInCategory) {
            throw new IllegalArgumentException("A category with this name is already present");
        }
        command.add(cmd);
    }

    default List<ICommand> getCommand() {
        return command;
    }

    String getName();

    String getDescription();
}
