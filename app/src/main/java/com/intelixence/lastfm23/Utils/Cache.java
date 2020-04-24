package com.intelixence.lastfm23.Utils;

import android.content.SharedPreferences;

public class Cache {

    private SharedPreferences preferences;

    public Cache(SharedPreferences sharedPreferences){
        this.preferences = sharedPreferences;
    }

    public void escribir(String variables){
        SharedPreferences.Editor editor = preferences.edit();
        String[] array = variables.split(",");
        for (int i = 0; i < array.length; i++){
            String[] datos = array[i].split(":");
            editor.putString(datos[0],datos[1]);
        }
        editor.apply();
    }

    public SharedPreferences leer(){
        return preferences;
    }
}
