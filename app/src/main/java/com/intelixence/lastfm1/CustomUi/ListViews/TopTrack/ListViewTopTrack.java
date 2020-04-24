package com.intelixence.lastfm1.CustomUi.ListViews.TopTrack;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.intelixence.lastfm1.CustomUi.Modals.ModalDetails;
import com.intelixence.lastfm1.R;

import java.util.ArrayList;

public class ListViewTopTrack extends BaseAdapter {

    private Activity activity;
    private ArrayList<ItemTopTrack> itemsTopTrack = new ArrayList<>();

    public ListViewTopTrack(Activity activity, ArrayList<ItemTopTrack> itemsTopTrack) {
        this.activity = activity;
        this.itemsTopTrack = itemsTopTrack;
    }

    @Override
    public int getCount() {
        return itemsTopTrack.size();
    }

    @Override
    public Object getItem(int position) {
        return itemsTopTrack.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if ( convertView == null ) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_listview_top_tracks, null);
        }

        TextView tv_name_track = convertView.findViewById(R.id.iltt_tv_name_track);
        TextView tv_name_artist = convertView.findViewById(R.id.iltt_tv_name_artist);
        TextView tv_duration = convertView.findViewById(R.id.iltt_tv_duration);
        TextView tv_rank = convertView.findViewById(R.id.iltt_tv_rank);
        Button btn_show_details = convertView.findViewById(R.id.iltt_btn_show_details);

        tv_name_track.setText(itemsTopTrack.get(position).getTrack_name());
        tv_name_artist.setText(itemsTopTrack.get(position).getArtist_name());
        tv_duration.setText("Duration "+itemsTopTrack.get(position).getTrack_duration());
        tv_rank.setText("Rank "+itemsTopTrack.get(position).getRank_track());
        btn_show_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModalDetails.ShowModalDetails(activity, itemsTopTrack.get(position));
            }
        });
        return convertView;
    }
}
