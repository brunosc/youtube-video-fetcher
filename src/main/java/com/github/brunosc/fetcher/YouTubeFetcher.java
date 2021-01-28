package com.github.brunosc.fetcher;

import com.github.brunosc.fetcher.domain.YouTubeFetcherParams;
import com.github.brunosc.fetcher.domain.VideoDetails;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.PlaylistItemListResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class YouTubeFetcher {

    private static final Long DEFAULT_MAX_RESULTS = 5L;
    private final YouTubeServiceHolder youTubeService;

    public YouTubeFetcher(YouTubeFetcherParams params) throws GeneralSecurityException, IOException {
        this.youTubeService = YouTubeServiceHolder.getInstance(params);
    }

    public List<VideoDetails> fetchByChannelId(String channelId) throws IOException {
        return fetchByChannelId(channelId, DEFAULT_MAX_RESULTS);
    }

    public List<VideoDetails> fetchByChannelId(String channelId, Long maxResults) throws IOException {
        YouTube.Channels.List request = youTubeService.getService().channels().list("contentDetails");
        ChannelListResponse response = request.setId(channelId).execute();

        Channel channel = response.getItems().get(0);
        String uploadId = channel.getContentDetails().getRelatedPlaylists().getUploads();

        return fetchByPlaylistId(uploadId, maxResults);
    }

    public List<VideoDetails> fetchByPlaylistId(String playlistId) throws IOException {
        return fetchByPlaylistId(playlistId, DEFAULT_MAX_RESULTS);
    }

    public List<VideoDetails> fetchByPlaylistId(String playlistId, Long maxResults) throws IOException {
        YouTube.PlaylistItems.List playlistRequest = youTubeService.getService().playlistItems().list("snippet");
        PlaylistItemListResponse playlistResponse = playlistRequest.setPlaylistId(playlistId).setMaxResults(maxResults).execute();

        return playlistResponse
                .getItems()
                .stream()
                .map(VideoDetails::new)
                .collect(toList());
    }

}
