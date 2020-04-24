package com.intelixence.lastfm23.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.intelixence.lastfm23.Configs.Required;
import com.intelixence.lastfm23.CustomUi.ListViews.TopArtist.ItemTopArtist;
import com.intelixence.lastfm23.CustomUi.ListViews.TopArtist.ListViewTopArtist;
import com.intelixence.lastfm23.CustomUi.ListViews.TopTrack.ItemTopTrack;
import com.intelixence.lastfm23.CustomUi.ListViews.TopTrack.ListViewTopTrack;
import com.intelixence.lastfm23.CustomUi.ProgressBar.ProgressBarGeneral;
import com.intelixence.lastfm23.R;
import com.intelixence.lastfm23.Service.CustomEasyReqFilter;
import com.intelixence.lastfm23.Utils.CustomTime;
import com.intelixence.peticiones.EasyReq;

import java.util.ArrayList;

public class TopArtists extends Fragment implements EasyReq.Event{

    ListView lv_top_artist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_artists, container, false);

        //starts the view components
        lv_top_artist = view.findViewById(R.id.fta_lv_top_artists);

        //process
        getTopTracks();
        ArrayList<ItemTopArtist> itemsTopArtists = new ArrayList<>();
        itemsTopArtists.add(new ItemTopArtist("hola", 500000, "http://google.com"));
        ListViewTopArtist listViewTopTrack = new ListViewTopArtist(getActivity(), itemsTopArtists);
        lv_top_artist.setAdapter(listViewTopTrack);
        return view;
    }

    public void getTopTracks(){
        EasyReq.POST_FORM_URL_ENCODED(getContext(), Required.getUrl_geo_get_top_artist(), new CustomEasyReqFilter(), 1, null, this, new EasyReq.State() {
            @Override
            public void Start() {
                ProgressBarGeneral.ShowProgressBarGeneral(getContext(), "Cargando");
            }
            @Override
            public void End() {
            }
        }, 13000);
    }

    @Override
    public void Response(String response, int code_request) {

    }

    @Override
    public void Error(VolleyError error, int code_request) {

    }
}
