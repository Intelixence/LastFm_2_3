package com.intelixence.lastfm1.CustomUi.ListViews.TopTrack;

import android.graphics.Bitmap;

public class ItemTopTrack {

    private int rank_track;
    private String track_name, track_duration, track_url, artist_name, artist_url;
    private int track_listeners;
    private String url_image_track;

    public ItemTopTrack(int rank_track, String track_name, String track_duration, String track_url, String artist_name, String artist_url, int track_listeners, String url_image_track) {
        this.rank_track = rank_track;
        this.track_name = track_name;
        this.track_duration = track_duration;
        this.track_url = track_url;
        this.artist_name = artist_name;
        this.artist_url = artist_url;
        this.track_listeners = track_listeners;
        this.url_image_track = url_image_track;
    }

    public int getRank_track() {
        return rank_track;
    }

    public String getTrack_name() {
        return track_name;
    }

    public String getTrack_duration() {
        return track_duration;
    }

    public String getTrack_url() {
        return track_url;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public String getArtist_url() {
        return artist_url;
    }

    public int getTrack_listeners() {
        return track_listeners;
    }

    public String getUrl_image_track() {
        return url_image_track;
    }
}
