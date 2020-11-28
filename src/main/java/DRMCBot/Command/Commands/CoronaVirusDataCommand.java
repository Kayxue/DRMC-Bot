package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CoronaVirusDataCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.covid19api.com/total/dayone/country/south-africa/status/confirmed")
                .build();
        Response response = client.newCall(request).execute();
        String responseString = response.body().string();

    }

    @Override
    public String getName() {
        return "covid";
    }

    @Override
    public String getCategory() {
        return "otherinfo";
    }

    @Override
    public String getdescription() {
        return null;
    }

    @Override
    public EmbedBuilder gethelpembed() {
        return null;
    }
}
