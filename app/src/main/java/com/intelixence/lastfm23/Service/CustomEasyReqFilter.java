package com.intelixence.lastfm23.Service;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.volley.VolleyError;
import com.intelixence.lastfm23.CustomUi.Modals.ModalGeneral;
import com.intelixence.lastfm23.CustomUi.ProgressBar.ProgressBarGeneral;
import com.intelixence.lastfm23.Database.ManagerDatabase;
import com.intelixence.lastfm23.Database.SqlFunctions;
import com.intelixence.lastfm23.Fragments.TopArtists;
import com.intelixence.lastfm23.Fragments.TopTracks;
import com.intelixence.lastfm23.Utils.CustomLog;
import com.intelixence.lastfm23.Utils.Network;
import com.intelixence.peticiones.EasyReq;
import com.intelixence.peticiones.EasyReqFilter;
import com.intelixence.peticiones.EasyReqLastRequest;

import org.json.JSONObject;

import java.util.ArrayList;

public class CustomEasyReqFilter extends EasyReqFilter {

    @Override
    public void Filter_response(Context context, String response, int code_request, EasyReq.Event event) {
        CustomLog.i("Auto_clasificar", "Gestionar_respuesta:---------------------------------------------------------------------------------------------"+response);
        if (!SqlFunctions.dataAlreadyExist(context, code_request)){
            SqlFunctions.insertData(context, code_request, response);
        }else{
            SqlFunctions.updateData(context, code_request, response);
        }
        event.Response(response, code_request);
    }

    @Override
    public void Filter_error(final Context context, VolleyError volleyError, int code_request, final EasyReq.Event event) {
        CustomLog.i("Auto_clasificar", "Filter_error:---------------------------------------------------------------------------------------------"+volleyError);
        if (SqlFunctions.dataAlreadyExist(context, code_request)){
            event.Response(SqlFunctions.getData(context, code_request), code_request);
        }else {
            if (Network.enabled(context)) {
                if (Network.internet_access(context)) {
                    try {
                        JSONObject jsonObject = new JSONObject(new String(volleyError.networkResponse.data, "UTF-8"));
                        ModalGeneral.ShowModalGeneral(context, "Error", "Error del servidor \"" + jsonObject.getString("message") + "\"", "Salir", "Reintentar", new ModalGeneral.OnModalGeneral() {
                            @Override
                            public void onPressTitle() {
                            }

                            @Override
                            public void onPressMessage() {
                            }

                            @Override
                            public void onPressBtn1() {
                                ((Activity) context).startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                ((Activity) context).finish();
                                System.exit(0);
                            }

                            @Override
                            public void onPressBtn2() {
                                ArrayList<EasyReqLastRequest> easyReqLastRequests = EasyReq.getHistoryRequests();
                                EasyReq.ExecuteHistoryRequests(easyReqLastRequests.get(0));
                                EasyReq.ExecuteHistoryRequests(easyReqLastRequests.get(1));
                                ModalGeneral.HideModalGeneral();
                            }
                        });
                    } catch (Exception e) {
                        show_modal_without_internet(context);
                    }
                } else {
                    show_modal_without_internet(context);
                }
            } else {
                show_modal_enabled_internet(context);
            }
            ProgressBarGeneral.HideProgressBarGeneral();
        }
    }


    void show_modal_enabled_internet(final Context context) {
        ModalGeneral.OnModalGeneral onModalGeneral = new ModalGeneral.OnModalGeneral() {
            @Override
            public void onPressTitle() {
            }

            @Override
            public void onPressMessage() {
            }

            @Override
            public void onPressBtn1() {
                ((Activity) context).startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                ((Activity) context).finish();
                System.exit(0);
            }

            @Override
            public void onPressBtn2() {
                ArrayList<EasyReqLastRequest> easyReqLastRequests = EasyReq.getHistoryRequests();
                EasyReq.ExecuteHistoryRequests(easyReqLastRequests.get(0));
                EasyReq.ExecuteHistoryRequests(easyReqLastRequests.get(1));
                ModalGeneral.HideModalGeneral();
            }
        };
        ModalGeneral.ShowModalGeneral(context, "Sin internet", "Activa el <a href='http'>wifi</a> o <a href='http'>datos moviles</a>.", "Salir", "Reintentar", onModalGeneral);
    }

    void show_modal_without_internet(final Context context) {
        ModalGeneral.OnModalGeneral onModalGeneral = new ModalGeneral.OnModalGeneral() {
            @Override
            public void onPressTitle() {
            }

            @Override
            public void onPressMessage() {
            }

            @Override
            public void onPressBtn1() {
                ((Activity) context).startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                ((Activity) context).finish();
                System.exit(0);
            }

            @Override
            public void onPressBtn2() {
                ArrayList<EasyReqLastRequest> easyReqLastRequests = EasyReq.getHistoryRequests();
                EasyReq.ExecuteHistoryRequests(easyReqLastRequests.get(0));
                EasyReq.ExecuteHistoryRequests(easyReqLastRequests.get(1));
                ModalGeneral.HideModalGeneral();
            }
        };
        ModalGeneral.ShowModalGeneral(context, "Sin internet", "Sin conexion a internet, verifica tu <a href='http'>wifi</a> o <a href='http'>datos moviles</a>.", "Salir", "Reintentar", onModalGeneral);
    }

    public static void mostrar_modal_error(final Activity activity, Exception e){
        ModalGeneral.ShowModalGeneral(activity, "Error", "Error de la aplicacion \""+e.toString()+"\"", "Salir", "Reintentar", new ModalGeneral.OnModalGeneral() {
            @Override
            public void onPressTitle() {
            }
            @Override
            public void onPressMessage() {
            }
            @Override
            public void onPressBtn1() {
                activity.startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                activity.finish();
                System.exit(0);
            }
            @Override
            public void onPressBtn2() {
                ArrayList<EasyReqLastRequest> easyReqLastRequests = EasyReq.getHistoryRequests();
                EasyReq.ExecuteHistoryRequests(easyReqLastRequests.get(0));
                EasyReq.ExecuteHistoryRequests(easyReqLastRequests.get(1));
                ModalGeneral.HideModalGeneral();
            }
        });
    }
}
