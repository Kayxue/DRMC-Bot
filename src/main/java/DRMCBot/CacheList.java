package DRMCBot;

import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CacheList {
    public static List<User> TestCooldownCommandInCooldown = new LinkedList<>();

    public static HashMap<String, Integer> ChinoCommandInCooldown = new HashMap<>();

    public static HashMap<String, Long> RunningGiveaway = new HashMap<>(); //{"serverid+channelid+messageid":length}
}
