package com.intelixence.lastfm1.CustomUi.ProgressBar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.intelixence.lastfm1.R;

public class ProgressBarGeneral {

    private static AlertDialog alertDialog = null;

    public static void ShowProgressBarGeneral(Context context, String message){
        HideProgressBarGeneral();
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.progressbar_general, null);
        TextView tv_message = view.findViewById(R.id.pg_tv_message);
        tv_message.setText(message);
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT,WindowManager.LayoutParams.FILL_PARENT);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.setView(view);
        alertDialog.show();
    }

    public static void HideProgressBarGeneral(){
        if (alertDialog != null){
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

}
