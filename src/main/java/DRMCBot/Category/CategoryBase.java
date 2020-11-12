package DRMCBot.Category;

import DRMCBot.Command.ICommand;

import java.util.LinkedList;

public class CategoryBase {
    public final LinkedList<ICommand> command = new LinkedList<>();

    public void addCommand(ICommand cmd) {
        boolean nameFoundInCategory = command.stream().anyMatch(it -> it.getName().equalsIgnoreCase(cmd.getName()));
        if (nameFoundInCategory) {
            throw new IllegalArgumentException("A Command with this name is already present");
        }
        command.add(cmd);
    }
}
