package com.tool.tetsu2kasen.tnv_eg_v2;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tetsu2kasen on 2018/02/08.
 */

public class Fileread {

    public List<String> text= new ArrayList<>();
    public void fileRead(Context context, String filePath) {

        text.clear();
        AssetManager as = context.getResources().getAssets();
        InputStream st = null;
        try {
            st = as.open(filePath+".Dat");
            byte[] buffer = new byte[st.available()];
            while ((st.read(buffer)) != -1) {
            }
            String s = new String(buffer);// この中にテキストの内容が入る
            String[] str=s.split("\n");

            for (String item : str ) {
                text.add(item);
            }
            st.close();

        } catch (IOException e) {
            e.printStackTrace();
        }



    }
    public String textgetter(int nt)
    {
        return text.get(nt);
    }
    public int textsizegetter()
    {
        return text.size();
    }
}
