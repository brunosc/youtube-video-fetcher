package com.github.brunosc.fetcher;

import com.github.brunosc.fetcher.domain.VideoDetails;
import com.github.brunosc.fetcher.domain.YouTubeFetcherParams;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.PlaylistItemListResponse;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.List;

import static com.google.api.client.googleapis.javanet.GoogleNetHttpTransport.newTrustedTransport;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class YouTubeFetcher {

    private static final Long DEFAULT_MAX_RESULTS = 5L;
    private static final String APPLICATION_NAME = "YouTube Fetcher";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private final YouTube youTube;

    public YouTubeFetcher(YouTubeFetcherParams params) throws GeneralSecurityException, IOException {
        this.youTube = new YouTube.Builder(newTrustedTransport(), JSON_FACTORY, credentials(params.getClientSecretsIn()))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private GoogleCredential credentials(InputStream clientSecrets) throws IOException {
        return GoogleCredential
                .fromStream(clientSecrets)
                .createScoped(singletonList(YouTubeScopes.YOUTUBE_READONLY));
    }

    public List<VideoDetails> fetchByChannelId(String channelId) throws IOException {
        return fetchByChannelId(channelId, DEFAULT_MAX_RESULTS);
    }

    public List<VideoDetails> fetchByChannelId(String channelId, Long maxResults) throws IOException {
        YouTube.Channels.List request = youTube
                .channels()
                .list(singletonList("contentDetails"));
        ChannelListResponse response = request
                .setId(singletonList(channelId))
                .execute();

        Channel channel = response.getItems().get(0);
        String uploadId = channel
                .getContentDetails()
                .getRelatedPlaylists()
                .getUploads();

        return fetchByPlaylistId(uploadId, maxResults);
    }

    public List<VideoDetails> fetchByPlaylistId(String playlistId) throws IOException {
        return fetchByPlaylistId(playlistId, DEFAULT_MAX_RESULTS);
    }

    public List<VideoDetails> fetchByPlaylistId(String playlistId, Long maxResults) throws IOException {
        YouTube.PlaylistItems.List playlistRequest = youTube
                .playlistItems()
                .list(singletonList("snippet"));
        PlaylistItemListResponse playlistResponse = playlistRequest
                .setPlaylistId(playlistId)
                .setMaxResults(maxResults)
                .execute();

        return playlistResponse
                .getItems()
                .stream()
                .map(VideoDetails::new)
                .collect(toList());
    }

}
