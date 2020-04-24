package com.intelixence.lastfm1.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.intelixence.lastfm1.Configs.Required;
import com.intelixence.lastfm1.CustomUi.ListViews.TopTrack.ItemTopTrack;
import com.intelixence.lastfm1.CustomUi.ListViews.TopTrack.ListViewTopTrack;
import com.intelixence.lastfm1.CustomUi.Modals.ModalGeneral;
import com.intelixence.lastfm1.CustomUi.ProgressBar.ProgressBarGeneral;
import com.intelixence.lastfm1.R;
import com.intelixence.lastfm1.Service.CustomEasyReqFilter;
import com.intelixence.lastfm1.Utils.CustomLog;
import com.intelixence.lastfm1.Utils.CustomTime;
import com.intelixence.peticiones.EasyReq;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Core extends AppCompatActivity implements EasyReq.Event {

    //components
    ListView lv_top_tracks;
    TextView tv_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);

        //starts the view components
        tv_url = findViewById(R.id.ac_tv_url);
        lv_top_tracks = findViewById(R.id.ac_lv_top_tracks);

        //process
        getTopTracks();
        tv_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.last.fm/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

    }

    public void getTopTracks(){
        EasyReq.POST_FORM_URL_ENCODED(this, Required.getUrl_geo_get_top_tracks(), new CustomEasyReqFilter(), 1, null, this, new EasyReq.State() {
            @Override
            public void Start() {
                ProgressBarGeneral.ShowProgressBarGeneral(Core.this, "Cargando");
            }
            @Override
            public void End() {
            }
        }, 13000);
    }

    @Override
    public void Response(String response, int code_request) {
        try {
            JSONArray json_tracks = new JSONObject(new String(response.getBytes("ISO-8859-1"), "UTF-8")).getJSONObject("tracks").getJSONArray("track");
            final ArrayList<ItemTopTrack> itemsTopTracks = new ArrayList<>();
            for (int i = 0; i < json_tracks.length(); i++){
                CustomLog.i("CoreResponse", json_tracks.getJSONObject(i).toString()+"\n");
                final int rank_track = (json_tracks.getJSONObject(i).getJSONObject("@attr").getInt("rank"))+1;
                final String track_name = json_tracks.getJSONObject(i).getString("name");
                final int track_duration = json_tracks.getJSONObject(i).getInt("duration");
                final String track_url = json_tracks.getJSONObject(i).getString("url");
                final String artist_name = json_tracks.getJSONObject(i).getJSONObject("artist").getString("name");
                final String artist_url = json_tracks.getJSONObject(i).getJSONObject("artist").getString("url");
                final int track_listeners = json_tracks.getJSONObject(i).getInt("listeners");
                String url_image = json_tracks.getJSONObject(i).getJSONArray("image").getJSONObject(1).getString("#text");
                itemsTopTracks.add(new ItemTopTrack(rank_track, track_name, CustomTime.minutes_seconds(track_duration),track_url,artist_name, artist_url, track_listeners, url_image));
            }
            ListViewTopTrack listViewTopTrack = new ListViewTopTrack(this, itemsTopTracks);
            lv_top_tracks.setAdapter(listViewTopTrack);
            ProgressBarGeneral.HideProgressBarGeneral();
        }catch (Exception e){
            ModalGeneral.ShowModalGeneral(this, "Error", "Error de la aplicacion \""+e.toString()+"\"", "Salir", "Reintentar", new ModalGeneral.OnModalGeneral() {
                @Override
                public void onPressTitle() {
                }
                @Override
                public void onPressMessage() {
                }
                @Override
                public void onPressBtn1() {
                    startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
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

    @Override
    public void Error(VolleyError error, int code_request) {
    }
}
