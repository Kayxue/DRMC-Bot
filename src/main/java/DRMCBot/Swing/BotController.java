package DRMCBot.Swing;

import DRMCBot.CommandManagerV3;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.swing.*;
import java.util.HashMap;

public class BotController {
    private final JDA jda;
    public final JFrame frame;
    private JPanel Panel1;
    private JButton send;
    private JButton leave;
    private JComboBox chooseguild;
    private JComboBox choosechannel;
    private JTextArea message;
    private HashMap<String, Guild> guildList = new HashMap<>();
    private HashMap<String, TextChannel> textChannelList = new HashMap<>();

    public BotController(CommandManagerV3 manager, JDA jda) {
        this.jda = jda;
        this.frame = new JFrame("BotController");
        jda.getGuilds().stream().iterator().forEachRemaining(
                guild -> {
                    guildList.put(guild.getName(), guild);
                    chooseguild.addItem(guild.getName());
                }
        );
        frame.setContentPane(this.Panel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(new BotControllerWindowsListener(manager));
        choosechannel.setEnabled(false);
        message.setEditable(false);
        chooseguild.addItemListener(e -> {
            message.setEditable(false);
            choosechannel.setEnabled(false);
            String guildName = e.getItem().toString();
            textChannelList = new HashMap<>();
            choosechannel.removeAllItems();
            guildList.get(guildName).getTextChannels().stream().iterator().forEachRemaining(
                    textChannel -> {
                        textChannelList.put(textChannel.getName(), textChannel);
                        choosechannel.addItem(textChannel.getName());
                    }
            );
            choosechannel.setEnabled(true);
            if (choosechannel.getItemCount() == 0) {
                choosechannel.addItem("無頻道");
            } else {
                message.setEditable(true);
            }
        });
        send.addActionListener(e -> {
            if (choosechannel.getSelectedItem() != null) {
                if (message.getText().length() != 0) {
                    jda.getGuildsByName(chooseguild.getSelectedItem().toString(), false).get(0)
                            .getTextChannelsByName(choosechannel.getSelectedItem().toString(), false).get(0)
                            .sendMessage(message.getText()).queue();
                } else {
                    JOptionPane.showMessageDialog(frame, "請輸入訊息內容！", "錯誤！", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "請選擇伺服器頻道並輸入訊息內容！", "錯誤！", JOptionPane.ERROR_MESSAGE);
            }

        });
        leave.addActionListener(e -> {
            frame.setVisible(false);
            frame.dispose();
            manager.botController = null;
        });
    }
}
