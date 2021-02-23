package DRMCBot.Swing;

import DRMCBot.CommandManagerV3;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class BotControllerWindowsListener extends WindowAdapter {
    private final CommandManagerV3 commandManager;
    public BotControllerWindowsListener(CommandManagerV3 managerV3) {
        this.commandManager = managerV3;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        commandManager.botController = null;
    }
}
