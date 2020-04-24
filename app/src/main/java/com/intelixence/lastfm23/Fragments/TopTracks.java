package com.intelixence.lastfm23.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.intelixence.lastfm23.Configs.Required;
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

        //process
        getTopTracks();
        return view;
    }

    public void getTopTracks(){
        EasyReq.POST_FORM_URL_ENCODED(getContext(), Required.getUrl_geo_get_top_tracks(), new CustomEasyReqFilter(), 1, null, this, new EasyReq.State() {
            @Override
            public void Start() {
                ProgressBarGeneral.ShowProgressBarGeneral(getContext(), "Cargando");
            }
            @Override
            public void End() {
            }
        }, 13000);
    }

    static int paginacion = 0;

    @Override
    public void Response(String response, int code_request) {
        try {
            final JSONArray json_tracks = new JSONObject(new String(response.getBytes("ISO-8859-1"), "UTF-8")).getJSONObject("tracks").getJSONArray("track");
            final ArrayList<ItemTopTrack> itemsTopTracks = new ArrayList<>();
            for (int i = 0; i < 10; i++){
                CustomLog.i("CoreResponse", json_tracks.getJSONObject(i).toString()+"\n");
                final int rank_track = (json_tracks.getJSONObject(i).getJSONObject("@attr").getInt("rank"))+1;
                final String track_name = json_tracks.getJSONObject(i).getString("name");
                final int track_duration = json_tracks.getJSONObject(i).getInt("duration");
                final String track_url = json_tracks.getJSONObject(i).getString("url");
                final String artist_name = json_tracks.getJSONObject(i).getJSONObject("artist").getString("name");
                final String artist_url = json_tracks.getJSONObject(i).getJSONObject("artist").getString("url");
                final int track_listeners = json_tracks.getJSONObject(i).getInt("listeners");
                String url_image = json_tracks.getJSONObject(i).getJSONArray("image").getJSONObject(1).getString("#text");
                itemsTopTracks.add(new ItemTopTrack(rank_track, track_name, CustomTime.minutes_seconds(track_duration),track_url,artist_name, artist_url, track_listeners));
            }
            for (int i = 0; i < json_tracks.length(); i++){

                final int rank_track = (json_tracks.getJSONObject(i).getJSONObject("@attr").getInt("rank"))+1;
                String url_image = json_tracks.getJSONObject(i).getJSONArray("image").getJSONObject(1).getString("#text").replace("https", "http");
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
                                mostrar_modal_error(e);
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
                                mostrar_modal_error(e);
                            }
                        }
                    }
                }
            });
            ListViewTopTrack listViewTopTrack = new ListViewTopTrack(getActivity(), itemsTopTracks);
            lv_top_tracks.setAdapter(listViewTopTrack);
            ProgressBarGeneral.HideProgressBarGeneral();
        }catch (Exception e){
            mostrar_modal_error(e);
        }
    }

    @Override
    public void Error(VolleyError error, int code_request) {
    }

    public void mostrar_modal_error(Exception e){
        ModalGeneral.ShowModalGeneral(getContext(), "Error", "Error de la aplicacion \""+e.toString()+"\"", "Salir", "Reintentar", new ModalGeneral.OnModalGeneral() {
            @Override
            public void onPressTitle() {
            }
            @Override
            public void onPressMessage() {
            }
            @Override
            public void onPressBtn1() {
                startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                getActivity().finish();
                System.exit(0);
            }
            @Override
            public void onPressBtn2() {
                EasyReq.ExecuteLastRequest();
                ModalGeneral.HideModalGeneral();
            }
        });
    }
}
