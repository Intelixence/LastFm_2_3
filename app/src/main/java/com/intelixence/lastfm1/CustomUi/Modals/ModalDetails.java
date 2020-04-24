package com.intelixence.lastfm1.CustomUi.Modals;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.intelixence.lastfm1.CustomUi.ListViews.TopTrack.ItemTopTrack;
import com.intelixence.lastfm1.R;
import com.intelixence.lastfm1.Utils.CustomLog;
import com.intelixence.peticiones.EasyReq;

public class ModalDetails {

    private static AlertDialog alertDialog = null;

    public static void ShowModalDetails(final Context context, final ItemTopTrack itemTopTrack){
        HideModalDetails();
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.modal_details, null);

        //starts the view components
        final ImageView iv_icono = view.findViewById(R.id.md_iv_icono);
        TextView tv_rank = view.findViewById(R.id.md_tv_rank);
        TextView tv_listeners = view.findViewById(R.id.md_tv_listeners);
        TextView tv_track = view.findViewById(R.id.md_tv_track);
        TextView tv_artist = view.findViewById(R.id.md_tv_artist);
        Button btn_open_page_track = view.findViewById(R.id.md_btn_open_page_track);
        Button btn_open_page_artist = view.findViewById(R.id.md_btn_open_page_artist);
        Button btn_cerrar = view.findViewById(R.id.md_btn_cerrar);

        //listeners and sets
        EasyReq.READ_IMAGE(itemTopTrack.getUrl_image_track().replace("https", "http"), new EasyReq.EventReadImage() {
            @Override
            public void Start() {
            }
            @Override
            public void Downloaded(Bitmap bitmap) {
                iv_icono.setImageBitmap(bitmap);
            }
            @Override
            public void Error(String error) {
                CustomLog.i("ShowModalDetailsError", error);
            }
        });

        tv_rank.setText("Rank "+itemTopTrack.getRank_track());
        tv_listeners.setText("Listeners "+itemTopTrack.getTrack_listeners());
        tv_track.setText(itemTopTrack.getTrack_name());
        tv_artist.setText(itemTopTrack.getArtist_name());
        btn_open_page_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(itemTopTrack.getTrack_url());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                ((Activity) context).startActivity(intent);
            }
        });
        btn_open_page_artist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(itemTopTrack.getArtist_url());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                ((Activity) context).startActivity(intent);
            }
        });
        btn_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideModalDetails();
            }
        });

        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.setView(view);
        alertDialog.show();
    }

    public static void HideModalDetails(){
        if (alertDialog != null){
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

}
