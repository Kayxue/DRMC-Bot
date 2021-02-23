package DRMCBot.Command.Commands.Owner;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import groovy.lang.GroovyShell;

import java.util.HashMap;
import java.util.List;

public class EvalCommand implements ICommand {
    private final GroovyShell engine;
    private final String imports;

    public EvalCommand() {
        this.engine = new GroovyShell();
        this.imports = "import java.io.*\n" +
                "import java.lang.*\n" +
                "import java.util.*\n" +
                "import java.util.concurrent.*\n" +
                "import net.dv8tion.jda.core.*\n" +
                "import net.dv8tion.jda.core.entities.*\n" +
                "import net.dv8tion.jda.core.entities.impl.*\n" +
                "import net.dv8tion.jda.core.managers.*\n" +
                "import net.dv8tion.jda.core.managers.impl.*\n" +
                "import net.dv8tion.jda.core.utils.*\n";
    }

    @Override
    public void handle(CommandContext ctx) throws Exception {
        List<String> args=ctx.getArgs();
        if (!ctx.getAuthor().getId().equals("470516498050580480")) {
            return;
        }

        if (args.isEmpty()) {
            ctx.getChannel().sendMessage("Missing arguments").queue();

            return;
        }

        try {
            engine.setProperty("args", args);
            engine.setProperty("event", ctx);
            engine.setProperty("message", ctx.getMessage());
            engine.setProperty("channel", ctx.getChannel());
            engine.setProperty("jda", ctx.getJDA());
            engine.setProperty("guild", ctx.getGuild());
            engine.setProperty("member", ctx.getMember());

            String script = imports + ctx.getMessage().getContentRaw().split("\\s+", 2)[1];
            Object out = engine.evaluate(script);

            ctx.getChannel().sendMessage(out == null ? "Executed without error" : out.toString()).queue();
        }
        catch (Exception e) {
            ctx.getChannel().sendMessage(e.getMessage()).queue();
        }
    }

    @Override
    public String getName() {
        return "eval";
    }

    @Override
    public String getCategory() {
        return "owner";
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
