package com.intelixence.lastfm23.CustomUi.ListViews.TopArtist;

public class ItemTopArtist {

    private String name_artist;
    private int listeners;
    private String url;

    public ItemTopArtist(String name_artist, int listeners, String url) {
        this.name_artist = name_artist;
        this.listeners = listeners;
        this.url = url;
    }

    public String getName_artist() {
        return name_artist;
    }

    public int getListeners() {
        return listeners;
    }

    public String getUrl() {
        return url;
    }
}
