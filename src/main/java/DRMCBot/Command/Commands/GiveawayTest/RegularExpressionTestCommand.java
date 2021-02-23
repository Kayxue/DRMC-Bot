package DRMCBot.Command.Commands.GiveawayTest;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpressionTestCommand implements ICommand {

    Pattern timeFormat = Pattern.compile("(?:(\\d{1,5})(h|s|m|d))+?");

    @Override
    public void handle(CommandContext ctx) {
        String dataToTest = ctx.getMessage().getContentRaw().substring(ctx.getMessage().getContentRaw().indexOf(ctx.getArgs().get(0))).trim();
        boolean ifMatches = Pattern.matches(String.valueOf(timeFormat), dataToTest);
        ctx.getChannel().sendMessage(String.valueOf(ifMatches)).queue();
        Matcher matcher = timeFormat.matcher(dataToTest);

        int sec = 0;
        while (matcher.find()) {
            switch (matcher.group(2)) {
                case "d" -> sec += (Integer.parseInt(matcher.group(1)) * 86400);
                case "h" -> sec += (Integer.parseInt(matcher.group(1)) * 3600);
                case "m" -> sec += (Integer.parseInt(matcher.group(1)) * 60);
                case "s" -> sec += Integer.parseInt(matcher.group(1));
            }
        }
        ctx.getChannel().sendMessage("結果為" + sec + "秒").queue();
    }

    @Override
    public String getName() {
        return "testiftime";
    }

    @Override
    public String getCategory() {
        return "nocategory";
    }

    @Override
    public String getdescription() {
        return null;
    }

    @Override
    public List<String> getUsages() {
        return null;
    }

    @Override
    public List<String> getExamples() {
        return null;
    }

    @Override
    public HashMap<String, HashMap<String, String>> getArguments() {
        return null;
    }
}
