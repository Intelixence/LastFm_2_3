package com.intelixence.lastfm23.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.intelixence.lastfm23.Configs.Required;
import com.intelixence.lastfm23.CustomUi.ListViews.TopArtist.ListViewTopArtist;
import com.intelixence.lastfm23.CustomUi.ListViews.TopTrack.ItemTopTrack;
import com.intelixence.lastfm23.CustomUi.ListViews.TopTrack.ListViewTopTrack;
import com.intelixence.lastfm23.CustomUi.Modals.ModalGeneral;
import com.intelixence.lastfm23.CustomUi.ProgressBar.ProgressBarGeneral;
import com.intelixence.lastfm23.R;
import com.intelixence.lastfm23.Service.CustomEasyReqFilter;
import com.intelixence.lastfm23.Utils.Cache;
import com.intelixence.lastfm23.Utils.CustomLog;
import com.intelixence.lastfm23.Utils.CustomTime;
import com.intelixence.lastfm23.Utils.ImageUtils;
import com.intelixence.peticiones.EasyReq;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TopTracks extends Fragment implements EasyReq.Event{

    ListView lv_top_tracks;
    Button btn_left_pagination, btn_right_pagination;
    TextView tv_pagination;
    Cache cache;
    EditText et_search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_tracks, container, false);
        cache = new Cache(getContext().getSharedPreferences("imagenes", Context.MODE_PRIVATE));

        //starts the view components
        lv_top_tracks = view.findViewById(R.id.ftt_lv_top_tracks);
        btn_left_pagination = view.findViewById(R.id.ftt_btn_left_pagination);
        tv_pagination = view.findViewById(R.id.ftt_tv_pagination);
        btn_right_pagination = view.findViewById(R.id.ftt_btn_right_pagination);
        et_search = view.findViewById(R.id.ftt_et_search);

        //process
        getTopTracks(getContext(), this);
        return view;
    }

    void getTopTracks(final Context context, EasyReq.Event event){
        EasyReq.POST_FORM_URL_ENCODED(context, Required.getUrl_geo_get_top_tracks(), new CustomEasyReqFilter(), 1, null, event, new EasyReq.State() {
            @Override
            public void Start() {
                ProgressBarGeneral.ShowProgressBarGeneral(context, "Cargando");
            }
            @Override
            public void End() {
            }
        }, 13000);
    }

    private static int paginacion = 0;

    @Override
    public void Response(String response, int code_request) {
        try {
            paginacion = 0;
            final JSONArray json_tracks = new JSONObject(new String(response.getBytes("ISO-8859-1"), "UTF-8")).getJSONObject("tracks").getJSONArray("track");
            final ArrayList<ItemTopTrack> itemsTopTracks = new ArrayList<>();
            final ArrayList<ItemTopTrack> itemsTopTracksSearch = new ArrayList<>();
            for (int i = 0; i < 10; i++){
                CustomLog.i("CoreResponse", json_tracks.getJSONObject(i).toString()+"\n");
                final int rank_track = (json_tracks.getJSONObject(i).getJSONObject("@attr").getInt("rank"))+1;
                final String track_name = json_tracks.getJSONObject(i).getString("name");
                final int track_duration = json_tracks.getJSONObject(i).getInt("duration");
                final String track_url = json_tracks.getJSONObject(i).getString("url");
                final String artist_name = json_tracks.getJSONObject(i).getJSONObject("artist").getString("name");
                final String artist_url = json_tracks.getJSONObject(i).getJSONObject("artist").getString("url");
                final int track_listeners = json_tracks.getJSONObject(i).getInt("listeners");
                itemsTopTracks.add(new ItemTopTrack(rank_track, track_name, CustomTime.minutes_seconds(track_duration),track_url,artist_name, artist_url, track_listeners));
            }
            for (int i = 0; i < json_tracks.length(); i++){

                final int rank_track = (json_tracks.getJSONObject(i).getJSONObject("@attr").getInt("rank"))+1;
                final String track_name = json_tracks.getJSONObject(i).getString("name");
                final int track_duration = json_tracks.getJSONObject(i).getInt("duration");
                final String track_url = json_tracks.getJSONObject(i).getString("url");
                final String artist_name = json_tracks.getJSONObject(i).getJSONObject("artist").getString("name");
                final String artist_url = json_tracks.getJSONObject(i).getJSONObject("artist").getString("url");
                final int track_listeners = json_tracks.getJSONObject(i).getInt("listeners");
                String url_image = json_tracks.getJSONObject(i).getJSONArray("image").getJSONObject(1).getString("#text").replace("https", "http");
                itemsTopTracksSearch.add(new ItemTopTrack(rank_track, track_name, CustomTime.minutes_seconds(track_duration),track_url,artist_name, artist_url, track_listeners));

                EasyReq.READ_IMAGE(url_image, new EasyReq.EventReadImage() {
                    @Override
                    public void Start() {
                    }
                    @Override
                    public void Downloaded(Bitmap bitmap) {
                        cache.leer().edit().putString(rank_track+"_track", ImageUtils.convert(bitmap)).apply();
                    }
                    @Override
                    public void Error(String error) {
                    }
                });
            }

            et_search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int textlength = et_search.getText().length();
                    ArrayList<ItemTopTrack> itemTopTracksSearchs_new =  new ArrayList<>();

                    if (textlength > 0) {
                        btn_left_pagination.setEnabled(false);
                        btn_right_pagination.setEnabled(false);
                        for (int i = 0; i < itemsTopTracksSearch.size(); i++) {
                            if (textlength <= itemsTopTracksSearch.get(i).getTrack_name().length() || textlength <= itemsTopTracksSearch.get(i).getArtist_name().length()) {
                                if (itemsTopTracksSearch.get(i).getTrack_name().toLowerCase().contains(et_search.getText().toString().toLowerCase().trim()) || itemsTopTracksSearch.get(i).getArtist_name().toLowerCase().contains(et_search.getText().toString().toLowerCase().trim())) {
                                    itemTopTracksSearchs_new.add(itemsTopTracksSearch.get(i));
                                }
                            }
                        }
                        ListViewTopTrack listViewTopTrack = new ListViewTopTrack(getActivity(), itemTopTracksSearchs_new);
                        lv_top_tracks.setAdapter(listViewTopTrack);
                    }else{
                        btn_left_pagination.setEnabled(true);
                        btn_right_pagination.setEnabled(true);
                        itemsTopTracks.clear();
                        for (int i = 0; i < 10; i++) {
                            paginacion = 0;
                            tv_pagination.setText("1 - 10");
                            itemsTopTracks.add(itemsTopTracksSearch.get(i));
                        }
                        ListViewTopTrack listViewTopTrack = new ListViewTopTrack(getActivity(), itemsTopTracks);
                        lv_top_tracks.setAdapter(listViewTopTrack);
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            btn_right_pagination.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (paginacion < 4) {
                        paginacion++;
                        tv_pagination.setText(((paginacion*10)+1)+" - "+((paginacion * 10) + 10));
                        itemsTopTracks.clear();
                        for (int i = paginacion*10; i < (paginacion * 10) + 10; i++) {
                            try {
                                CustomLog.i("CoreResponse", json_tracks.getJSONObject(i).toString() + "\n");
                                final int rank_track = (json_tracks.getJSONObject(i).getJSONObject("@attr").getInt("rank")) + 1;
                                final String track_name = json_tracks.getJSONObject(i).getString("name");
                                final int track_duration = json_tracks.getJSONObject(i).getInt("duration");
                                final String track_url = json_tracks.getJSONObject(i).getString("url");
                                final String artist_name = json_tracks.getJSONObject(i).getJSONObject("artist").getString("name");
                                final String artist_url = json_tracks.getJSONObject(i).getJSONObject("artist").getString("url");
                                final int track_listeners = json_tracks.getJSONObject(i).getInt("listeners");
                                itemsTopTracks.add(new ItemTopTrack(rank_track, track_name, CustomTime.minutes_seconds(track_duration), track_url, artist_name, artist_url, track_listeners));
                                ListViewTopTrack listViewTopTrack = new ListViewTopTrack(getActivity(), itemsTopTracks);
                                lv_top_tracks.setAdapter(listViewTopTrack);
                            }catch (Exception e){
                                CustomEasyReqFilter.mostrar_modal_error(getActivity(), e);
                            }
                        }
                    }
                }
            });
            btn_left_pagination.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (paginacion > 0) {
                        paginacion--;
                        tv_pagination.setText(((paginacion*10)+1)+" - "+((paginacion * 10) + 10));
                        itemsTopTracks.clear();
                        for (int i = paginacion*10; i < (paginacion * 10) + 10; i++) {
                            try {
                                CustomLog.i("CoreResponse", json_tracks.getJSONObject(i).toString() + "\n");
                                final int rank_track = (json_tracks.getJSONObject(i).getJSONObject("@attr").getInt("rank")) + 1;
                                final String track_name = json_tracks.getJSONObject(i).getString("name");
                                final int track_duration = json_tracks.getJSONObject(i).getInt("duration");
                                final String track_url = json_tracks.getJSONObject(i).getString("url");
                                final String artist_name = json_tracks.getJSONObject(i).getJSONObject("artist").getString("name");
                                final String artist_url = json_tracks.getJSONObject(i).getJSONObject("artist").getString("url");
                                final int track_listeners = json_tracks.getJSONObject(i).getInt("listeners");
                                itemsTopTracks.add(new ItemTopTrack(rank_track, track_name, CustomTime.minutes_seconds(track_duration), track_url, artist_name, artist_url, track_listeners));
                                ListViewTopTrack listViewTopTrack = new ListViewTopTrack(getActivity(), itemsTopTracks);
                                lv_top_tracks.setAdapter(listViewTopTrack);
                            }catch (Exception e){
                                CustomEasyReqFilter.mostrar_modal_error(getActivity(), e);
                            }
                        }
                    }
                }
            });
            ListViewTopTrack listViewTopTrack = new ListViewTopTrack(getActivity(), itemsTopTracks);
            lv_top_tracks.setAdapter(listViewTopTrack);
        }catch (Exception e){
            CustomEasyReqFilter.mostrar_modal_error(getActivity(), e);
        }
    }

    @Override
    public void Error(VolleyError error, int code_request) {
    }
}
