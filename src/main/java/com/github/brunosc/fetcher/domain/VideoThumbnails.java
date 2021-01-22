package com.github.brunosc.fetcher.domain;

import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.ThumbnailDetails;

import static java.util.Optional.ofNullable;

public class VideoThumbnails {

    private ThumbnailItem defaultThumbnail;
    private ThumbnailItem high;
    private ThumbnailItem maxres;
    private ThumbnailItem medium;
    private ThumbnailItem standard;

    public VideoThumbnails(){}

    public VideoThumbnails(ThumbnailItem defaultThumbnail, ThumbnailItem high, ThumbnailItem maxres, ThumbnailItem medium, ThumbnailItem standard) {
        this.defaultThumbnail = defaultThumbnail;
        this.high = high;
        this.maxres = maxres;
        this.medium = medium;
        this.standard = standard;
    }

    public VideoThumbnails(ThumbnailDetails thumbnailDetails) {
        this.defaultThumbnail = ofNullable(thumbnailDetails.getDefault()).map(ThumbnailItem::new).orElse(null);
        this.high = ofNullable(thumbnailDetails.getHigh()).map(ThumbnailItem::new).orElse(null);
        this.maxres = ofNullable(thumbnailDetails.getMaxres()).map(ThumbnailItem::new).orElse(null);
        this.medium = ofNullable(thumbnailDetails.getMedium()).map(ThumbnailItem::new).orElse(null);
        this.standard = ofNullable(thumbnailDetails.getStandard()).map(ThumbnailItem::new).orElse(null);
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
        private int height;
        private int width;
        private String url;

        public ThumbnailItem() {

        }

        public ThumbnailItem(int height, int width, String url) {
            this.height = height;
            this.width = width;
            this.url = url;
        }

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
