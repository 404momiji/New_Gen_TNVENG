package com.tool.tetsu2kasen.tnv_eg_v2;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by tetsu2kasen on 2018/02/23.
 */

public class SaveManager {
    public  SharedPreferences ODOPref;
    public void init(Context cont){
        ODOPref = PreferenceManager.getDefaultSharedPreferences(cont);
    }
    public SharedPreferences Savedata;
    public void fileWrite(Context context, String GY,String Loaded) {
        SharedPreferences.Editor Aeditor = ODOPref.edit();
        Aeditor.putString("SAVE",GY+","+Loaded);
        Aeditor.commit();
    }
    public String[] fileRead(Context context){
        Savedata = PreferenceManager.getDefaultSharedPreferences(context);
        String Syt=ODOPref.getString("SAVE","0,Serif");
        String[] aasd =Syt.split(",");
        return aasd;
    }
}