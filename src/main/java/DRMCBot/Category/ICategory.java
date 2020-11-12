package DRMCBot.Category;

import DRMCBot.Command.ICommand;

import java.util.ArrayList;
import java.util.List;

public interface ICategory {

    List<ICommand> getCommand();

    String getName();

    String getDescription();
}
