package com.intelixence.lastfm1.CustomUi.Modals;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.intelixence.lastfm1.R;

public class ModalGeneral {

    public interface OnModalGeneral{
        void onPressTitle();
        void onPressMessage();
        void onPressBtn1();
        void onPressBtn2();
    }

    private static AlertDialog alertDialog = null;

    public static void ShowModalGeneral(Context context, String title, String message, String btn1, String btn2, final OnModalGeneral onModalGeneral){
        HideModalGeneral();
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.modal_general, null);

        TextView tv_title = view.findViewById(R.id.mg_tv_title);
        TextView tv_message = view.findViewById(R.id.mg_tv_message);
        Button btn_btn1 = view.findViewById(R.id.mg_btn_btn1);
        Button btn_btn2 = view.findViewById(R.id.mg_btn_btn2);

        tv_title.setText(Html.fromHtml(title));
        tv_message.setText(Html.fromHtml(message));

        if (btn1 == null){
            btn_btn1.setVisibility(View.GONE);
        }else{
            btn_btn1.setText(Html.fromHtml(btn1));
        }
        if (btn2 == null){
            btn_btn2.setVisibility(View.GONE);
        }else{
            btn_btn2.setText(Html.fromHtml(btn2));
        }

        tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onModalGeneral != null) {
                    onModalGeneral.onPressTitle();
                }
            }
        });
        tv_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onModalGeneral != null) {
                    onModalGeneral.onPressMessage();
                }
            }
        });
        btn_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onModalGeneral != null) {
                    onModalGeneral.onPressBtn1();
                }
            }
        });
        btn_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onModalGeneral != null) {
                    onModalGeneral.onPressBtn2();
                }
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

    public static void HideModalGeneral(){
        if (alertDialog != null){
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

}
