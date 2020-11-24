package DRMCBot.lavaplayer;

import DRMCBot.Config;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;

import java.util.List;

public class YoutubeSearcher {
    private final YouTube youtube;

    public YoutubeSearcher() {
        YouTube temp = null;
        try {
            temp = new YouTube.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    null
            )
                    .setApplicationName("Application")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        youtube = temp;
    }

    public List<SearchResult> searchVideos(String input, long resultcount) throws Exception {
        List<SearchResult> results = null;

        results = youtube.search()
                .list("id,snippet")
                .setQ(input)
                .setMaxResults(resultcount)
                .setType("video")
                .setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
                .setKey(Config.get("youtubekey"))
                .execute()
                .getItems();
        return results;
    }
}
