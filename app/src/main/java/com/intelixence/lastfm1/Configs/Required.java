package com.intelixence.lastfm1.Configs;

public class Required {
    private static String url_geo_get_top_tracks = "http://ws.audioscrobbler.com/2.0/?method=geo.gettoptracks&country=colombia&api_key=829751643419a7128b7ada50de590067&format=json";

    public static String getUrl_geo_get_top_tracks() {
        return url_geo_get_top_tracks;
    }
}
