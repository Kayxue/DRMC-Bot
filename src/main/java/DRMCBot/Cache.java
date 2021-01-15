package DRMCBot;

import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Cache {
    public static List<User> TestCooldownCommandInCooldown = new LinkedList<>();

    public static HashMap<String, String> ContentCache = new HashMap<>();

    public static HashMap<String, List<String>> AttachmentsLinksList = new HashMap<>();

    public static HashMap<String, Integer> ChinoCommandInCooldown = new HashMap<>();

    public static HashMap<String, Long> RunningGiveaway = new HashMap<>(); //{"channelid+messageid":length}
}
