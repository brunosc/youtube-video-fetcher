package com.github.brunosc.fetcher;

import com.github.brunosc.fetcher.domain.VideoDetails;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;

public class YouTubeFetcher {

    private static final Long DEFAULT_MAX_RESULTS = 5L;
    private final YouTubeServiceHolder youTubeService;

    public YouTubeFetcher(InputStream clientSecretsIn) throws GeneralSecurityException, IOException {
        this.youTubeService = YouTubeServiceHolder.getInstance(clientSecretsIn);
    }

    public List<VideoDetails> fetchByChannelId(String channelId) throws IOException {
        return fetchByChannelId(channelId, DEFAULT_MAX_RESULTS);
    }

    public List<VideoDetails> fetchByChannelId(String channelId, Long maxResults) throws IOException {
        final var request = youTubeService.getService().channels().list("contentDetails");
        final var response = request.setId(channelId).execute();

        final var channel = response.getItems().get(0);
        final var uploadId = channel.getContentDetails().getRelatedPlaylists().getUploads();

        return fetchByPlaylistId(uploadId, maxResults);
    }

    public List<VideoDetails> fetchByPlaylistId(String playlistId) throws IOException {
        return fetchByPlaylistId(playlistId, DEFAULT_MAX_RESULTS);
    }

    public List<VideoDetails> fetchByPlaylistId(String playlistId, Long maxResults) throws IOException {
        final var playlistRequest = youTubeService.getService().playlistItems().list("snippet");
        final var playlistResponse = playlistRequest.setPlaylistId(playlistId).setMaxResults(maxResults).execute();

        return playlistResponse
                .getItems()
                .stream()
                .map(VideoDetails::new)
                .collect(toUnmodifiableList());
    }

}
