package com.github.brunosc.fetcher.domain;

import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.ThumbnailDetails;

import java.util.Optional;

public class VideoThumbnails {

    private final ThumbnailItem defaultThumbnail;
    private final ThumbnailItem high;
    private final ThumbnailItem maxres;
    private final ThumbnailItem medium;
    private final ThumbnailItem standard;

    public VideoThumbnails(ThumbnailDetails thumbnailDetails) {
        this.defaultThumbnail = Optional.of(thumbnailDetails.getDefault()).map(ThumbnailItem::new).orElse(null);
        this.high = Optional.of(thumbnailDetails.getHigh()).map(ThumbnailItem::new).orElse(null);
        this.maxres = Optional.of(thumbnailDetails.getMaxres()).map(ThumbnailItem::new).orElse(null);
        this.medium = Optional.of(thumbnailDetails.getMedium()).map(ThumbnailItem::new).orElse(null);
        this.standard = Optional.of(thumbnailDetails.getStandard()).map(ThumbnailItem::new).orElse(null);
    }

    public ThumbnailItem getDefaultThumbnail() {
        return defaultThumbnail;
    }

    public ThumbnailItem getHigh() {
        return high;
    }

    public ThumbnailItem getMaxres() {
        return maxres;
    }

    public ThumbnailItem getMedium() {
        return medium;
    }

    public ThumbnailItem getStandard() {
        return standard;
    }

    public static class ThumbnailItem {
        private final int height;
        private final int width;
        private final String url;

        public ThumbnailItem(Thumbnail thumbnail) {
            this.height = Math.toIntExact(thumbnail.getHeight());
            this.width = Math.toIntExact(thumbnail.getWidth());
            this.url = thumbnail.getUrl();
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }

        public String getUrl() {
            return url;
        }
    }

}
