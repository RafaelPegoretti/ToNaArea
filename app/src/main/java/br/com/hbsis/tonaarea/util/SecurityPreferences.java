package br.com.hbsis.tonaarea.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SecurityPreferences {

    Context context;
    private SharedPreferences sharedPreferences;

    public SecurityPreferences(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("tonaarea", Context.MODE_PRIVATE);
    }


    public void storeString(String key, String value){
        sharedPreferences.edit().putString(key, value).apply();
    }

    public String getStoredString(String key){
        return sharedPreferences.getString(key, "");
    }

}
