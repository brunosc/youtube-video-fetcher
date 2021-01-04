package com.github.brunosc.fetcher.domain;

import com.google.api.services.youtube.model.PlaylistItem;

import java.time.LocalDateTime;

import static com.github.brunosc.fetcher.domain.DateTimeConverter.toLocalDateTime;

public class VideoDetails {

    private static final String VIDEO_PREFIX = "https://www.youtube.com/watch?v=";

    private final String id;
    private final String url;
    private final String title;
    private final String description;
    private final VideoThumbnails thumbnails;
    private final LocalDateTime publishedAt;


    public VideoDetails(PlaylistItem item) {
        this.id = item.getSnippet().getResourceId().getVideoId();
        this.url = VIDEO_PREFIX.concat(id);
        this.title = item.getSnippet().getTitle();
        this.description = item.getSnippet().getDescription();
        this.thumbnails = new VideoThumbnails(item.getSnippet().getThumbnails());
        this.publishedAt = toLocalDateTime(item.getSnippet().getPublishedAt());
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public VideoThumbnails getThumbnails() {
        return thumbnails;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    @Override
    public String toString() {
        return "VideoDetails{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", publishedAt=" + publishedAt +
                '}';
    }
}
