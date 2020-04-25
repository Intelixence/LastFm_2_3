package com.intelixence.lastfm23.Fragments;

import android.content.Context;
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
import com.intelixence.lastfm23.CustomUi.ListViews.TopArtist.ItemTopArtist;
import com.intelixence.lastfm23.CustomUi.ListViews.TopArtist.ListViewTopArtist;
import com.intelixence.lastfm23.CustomUi.ListViews.TopTrack.ListViewTopTrack;
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

public class TopArtists extends Fragment implements EasyReq.Event{

    ListView lv_top_artist;
    Button btn_left_pagination, btn_right_pagination;
    TextView tv_pagination;
    EditText et_search;
    Cache cache;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_artists, container, false);
        cache = new Cache(getContext().getSharedPreferences("imagenes", Context.MODE_PRIVATE));

        //starts the view components
        lv_top_artist = view.findViewById(R.id.fta_lv_top_artists);
        btn_left_pagination = view.findViewById(R.id.fta_btn_left_pagination);
        tv_pagination = view.findViewById(R.id.fta_tv_pagination);
        btn_right_pagination = view.findViewById(R.id.fta_btn_right_pagination);
        et_search = view.findViewById(R.id.fta_et_search);

        //process
        getTopArtists();
        return view;
    }

    public void getTopArtists(){
        EasyReq.POST_FORM_URL_ENCODED(getContext(), Required.getUrl_geo_get_top_artist(), new CustomEasyReqFilter(), 2, null, this, new EasyReq.State() {
            @Override
            public void Start() {
                ProgressBarGeneral.ShowProgressBarGeneral(getContext(), "Cargando");
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
            final JSONArray json_artists = new JSONObject(new String(response.getBytes("ISO-8859-1"), "UTF-8")).getJSONObject("topartists").getJSONArray("artist");
            final ArrayList<ItemTopArtist> itemsTopArtists = new ArrayList<>();
            final ArrayList<ItemTopArtist> itemsTopArtistsSearch = new ArrayList<>();
            for (int i = 0; i < 10; i++){
                CustomLog.i("CoreResponse", json_artists.getJSONObject(i).toString()+"\n");
                final String artist_name = json_artists.getJSONObject(i).getString("name");
                final int artist_listeners = json_artists.getJSONObject(i).getInt("listeners");
                final String artist_url = json_artists.getJSONObject(i).getString("url");
                itemsTopArtists.add(new ItemTopArtist(artist_name, artist_listeners, artist_url));
            }
            for (int i = 0; i < json_artists.length(); i++){

                final String artist_name = json_artists.getJSONObject(i).getString("name");
                final int artist_listeners = json_artists.getJSONObject(i).getInt("listeners");
                final String artist_url = json_artists.getJSONObject(i).getString("url");
                final String url_image = json_artists.getJSONObject(i).getJSONArray("image").getJSONObject(1).getString("#text").replace("https", "http");
                itemsTopArtistsSearch.add(new ItemTopArtist(artist_name, artist_listeners, artist_url));

                EasyReq.READ_IMAGE(url_image, new EasyReq.EventReadImage() {
                    @Override
                    public void Start() {
                    }
                    @Override
                    public void Downloaded(Bitmap bitmap) {
                        cache.leer().edit().putString(artist_name.trim()+"_image", ImageUtils.convert(bitmap)).apply();
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
                    ArrayList<ItemTopArtist> itemTopArtistsSearchs_new =  new ArrayList<>();

                    if (textlength > 0) {
                        btn_left_pagination.setEnabled(false);
                        btn_right_pagination.setEnabled(false);
                        for (int i = 0; i < itemsTopArtistsSearch.size(); i++) {
                            if (textlength <= itemsTopArtistsSearch.get(i).getName_artist().length()) {
                                if (itemsTopArtistsSearch.get(i).getName_artist().toLowerCase().contains(et_search.getText().toString().toLowerCase().trim())) {
                                    itemTopArtistsSearchs_new.add(itemsTopArtistsSearch.get(i));
                                }
                            }
                        }
                        ListViewTopArtist listViewTopTrack = new ListViewTopArtist(getActivity(), itemTopArtistsSearchs_new);
                        lv_top_artist.setAdapter(listViewTopTrack);
                    }else{
                        btn_left_pagination.setEnabled(true);
                        btn_right_pagination.setEnabled(true);
                        itemsTopArtists.clear();
                        for (int i = 0; i < 10; i++) {
                            paginacion = 0;
                            tv_pagination.setText("1 - 10");
                            itemsTopArtists.add(itemsTopArtistsSearch.get(i));
                        }
                        ListViewTopArtist listViewTopTrack = new ListViewTopArtist(getActivity(), itemsTopArtists);
                        lv_top_artist.setAdapter(listViewTopTrack);
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
                        itemsTopArtists.clear();
                        for (int i = paginacion*10; i < (paginacion * 10) + 10; i++) {
                            try {
                                CustomLog.i("CoreResponse", json_artists.getJSONObject(i).toString() + "\n");
                                final String artist_name = json_artists.getJSONObject(i).getString("name");
                                final int artist_listeners = json_artists.getJSONObject(i).getInt("listeners");
                                final String artist_url = json_artists.getJSONObject(i).getString("url");
                                itemsTopArtists.add(new ItemTopArtist(artist_name, artist_listeners, artist_url));
                                ListViewTopArtist listViewTopTrack = new ListViewTopArtist(getActivity(), itemsTopArtists);
                                lv_top_artist.setAdapter(listViewTopTrack);
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
                        itemsTopArtists.clear();
                        for (int i = paginacion*10; i < (paginacion * 10) + 10; i++) {
                            try {
                                CustomLog.i("CoreResponse", json_artists.getJSONObject(i).toString() + "\n");
                                final String artist_name = json_artists.getJSONObject(i).getString("name");
                                final int artist_listeners = json_artists.getJSONObject(i).getInt("listeners");
                                final String artist_url = json_artists.getJSONObject(i).getString("url");
                                itemsTopArtists.add(new ItemTopArtist(artist_name, artist_listeners, artist_url));
                                ListViewTopArtist listViewTopTrack = new ListViewTopArtist(getActivity(), itemsTopArtists);
                                lv_top_artist.setAdapter(listViewTopTrack);
                            }catch (Exception e){
                                CustomEasyReqFilter.mostrar_modal_error(getActivity(), e);
                            }
                        }
                    }
                }
            });
            ListViewTopArtist listViewTopTrack = new ListViewTopArtist(getActivity(), itemsTopArtists);
            lv_top_artist.setAdapter(listViewTopTrack);
            ProgressBarGeneral.HideProgressBarGeneral();
        }catch (Exception e){
            CustomLog.stacktrace(e);
            CustomEasyReqFilter.mostrar_modal_error(getActivity(), e);
        }
    }

    @Override
    public void Error(VolleyError error, int code_request) {

    }
}
