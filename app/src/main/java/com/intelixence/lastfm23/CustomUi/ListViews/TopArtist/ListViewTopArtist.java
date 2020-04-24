package com.intelixence.lastfm23.CustomUi.ListViews.TopArtist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.intelixence.lastfm23.R;
import com.intelixence.lastfm23.Utils.Cache;
import com.intelixence.lastfm23.Utils.ImageUtils;

import java.util.ArrayList;

public class ListViewTopArtist extends BaseAdapter {

    private Activity activity;
    private ArrayList<ItemTopArtist> itemsTopArtists = new ArrayList<>();

    public ListViewTopArtist(Activity activity, ArrayList<ItemTopArtist> itemsTopArtists) {
        this.activity = activity;
        this.itemsTopArtists = itemsTopArtists;
    }

    @Override
    public int getCount() {
        return itemsTopArtists.size();
    }

    @Override
    public Object getItem(int position) {
        return itemsTopArtists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if ( convertView == null ) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_listview_top_artists, null);
        }

        Cache cache = new Cache(activity.getSharedPreferences("imagenes", Context.MODE_PRIVATE));
        ImageView iv_icon =convertView.findViewById(R.id.ilta_iv_icon);
        TextView tv_name_asrtist = convertView.findViewById(R.id.ilta_tv_name_artist);
        TextView tv_listeners_artist = convertView.findViewById(R.id.ilta_tv_listeners_artist);
        Button btn_open_web = convertView.findViewById(R.id.ilta_btn_open_web);

        String string_base64_track = cache.leer().getString(itemsTopArtists.get(position).getName_artist(), null);
        if (string_base64_track != null) {
            iv_icon.setImageBitmap(ImageUtils.convert(string_base64_track));
        }
        tv_name_asrtist.setText(itemsTopArtists.get(position).getName_artist());
        tv_listeners_artist.setText("Listeners "+itemsTopArtists.get(position).getListeners());
        btn_open_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(itemsTopArtists.get(position).getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                activity.startActivity(intent);
            }
        });

        return convertView;
    }
}
