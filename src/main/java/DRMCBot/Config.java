package DRMCBot;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.UUID;

public class Config {
    private  static final Dotenv dotenv = Dotenv.load();

    public static String get(String key){
        return dotenv.get(key.toUpperCase());
    }
}
