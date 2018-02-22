//filereadの中の要素数を取得する。　エスケープ文字を認識させる。
package com.tool.tetsu2kasen.tnv_eg_v2;
/*
//
//Next Do ...
//
// TextViewに表示
//
// CSV で読み込み
//
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.io.*;

import static com.tool.tetsu2kasen.tnv_eg_v2.R.id.button;


public class MainActivity extends Activity {

    TextView ra;
    TextView rb;
    TextView rc;
    TextView snr;
    TextView s1;
    TextView s2;
    TextView s3;
    TextView s4;
    LinearLayout slc;

    public int i;

    Handler mHandler;//るいぱんこ
    Timer mTimer;

    int GC = 0;//行カウント
    public int Bcot = 0;
    public String TMPTXT[] = {"", "", ""};
    public boolean AA = false;
    public String TMPKKK = "";//カキコ用テンプ
    public int NTC =0;
    public boolean TLDE=false;
    public int gyoukaunt=-1;
    public boolean saiseityu=false;
    public List<Integer> nst= new ArrayList<>();
    public List<String> BTx= new ArrayList<>();
    public List<String> BLnk= new ArrayList<>();
    public List<String> File= new ArrayList<>();
    public List<String> Yoko= new ArrayList<>();
    public boolean slc_md=false;
    public String Loaded="";
    Fileread fr = new Fileread();
    String txt ="hogehoge";
    public int BM;
    int reqcolbtn=0;//選択肢用エンジンのボタン数
    public void Init_TNVENG(){
        loadscene("serif");
    }
    //0C,9e,0a,0.330A,0.332d,0.88asdfg

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        mHandler = new Handler();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ra = (TextView) findViewById(R.id.row1);
        rb = (TextView) findViewById(R.id.row2);
        rc = (TextView) findViewById(R.id.row3);
        snr = (TextView) findViewById(R.id.snr);

        ra.setText("hoge");
        rb.setText("hogehoge");
        rc.setText("hogehogehoge");

        Log.d("AASZ",BM+"");
        Init_TNVENG();

    }

    public void texEngine(String nyo){

            slc_md=true;
            String buttmp[]=nyo.split("\uF0FE");
            reqcolbtn=buttmp.length;

            for(int i=0;i< buttmp.length;i++){
                String LD[]=buttmp[i].split(",");
                BLnk.add(LD[0]);
                BTx.add(LD[1]);
            }

    }

    public void selEngine(String nyo){
        slc_md=true;
        BTx.clear();
        BLnk.clear();
        String buttmp[]=nyo.split("\uF0FD");
        reqcolbtn=buttmp.length;

        for(int i=0;i< buttmp.length;i++){
            String LD[]=buttmp[i].split(",");
            File.add(LD[0]);
            Yoko.add(LD[1]);
        }
        String[] items=new String[BTx.size()];
        for(int n=0;n<BTx.size();n++){
            items[n]= BTx.get(n);
        }
        // チェックボックスの状態よりダイアログのIDを取得する

        // リスト表示用のアラートダイアログ
        final AlertDialog.Builder listDlg = new AlertDialog.Builder(this);

        listDlg.setTitle("選択肢");
        listDlg.setItems(
                items,
                new DialogInterface.OnClickListener() {
                    public void onDismiss(DialogInterface dialog) {
                        Log.v("",String.format("onDismiss dialog.toString=%s,",dialog.toString()));
                        // ダイアログの破棄時に必要な処理がある場合は、ここに記述する。
                    }
                    public void onClick(DialogInterface dialog, final int which) {
                        slc_md=false;
                        // リスト選択時の処理
                        // which は、選択されたアイテムのインデックス
                        //Log.d("Dialog","Wh"+which);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                selector(which);
                            }
                        }).start();


                    }
                });

        // 表示
        listDlg.create().show();



    }

    public void selector(int hogeru){
        if(hogeru==0) {
            Log.d("Pushed:", "0");
            loadscene(BLnk.get(0));
            Log.d("Loaded:", BLnk.get(0));
        }else if(hogeru==1){
                Log.d("Pushed:","1");
                loadscene(BLnk.get(1));
                Log.d("Loaded:",BLnk.get(1));
        }else if(hogeru==2){
                Log.d("Pushed:","2");
                loadscene(BLnk.get(2));
                Log.d("Loaded:",BLnk.get(2));
        }else if(hogeru==3) {
            Log.d("Pushed:", "3");
            loadscene(BLnk.get(3));
            Log.d("Loaded:", BLnk.get(3));
        }

    }
    public void scriptloader(String loaded){
        loaded=loaded.replace("\r","");
        loaded=loaded.replace("\r","");
        if(loaded.charAt(0)=='\uF0FA')
        {
            snr.setText("背景:"+loaded.substring(1));
        }
        else if(loaded.charAt(0)=='\uF0EA')
        {
            snr.setText("アニメーション:"+loaded.substring(1));
        }
        else if (loaded.charAt(0)=='\uF0EF')
        {
            slc_md=true;
            snr.setText("選択肢:"+loaded.substring(1));
            Loaded=loaded;
            selEngine(loaded.substring(1));
        }
        else if (loaded.charAt(0)=='\uF0EC')
        {
            snr.setText("シーン切り替え:"+loaded.substring(1));
            loadscene(loaded.substring(1));
        }
        else if (loaded.charAt(0)=='\uF0FB')
        {

        }
    }
    public void load_head() {
        NTC=0;
        nst.clear();
        BM = fr.textsizegetter();
        for (int l = 0; l < BM; l++) {
            String head_text;
            Log.d("LOAD:", "NAIYO:" + l + "Kaniko");
            textenc = fr.textgetter(l);
            head_text = textenc.substring(0, 1);
            Log.d("LOAD:", "Head:" + head_text + "Kaniko");
            //nst.add(3);
            if (head_text.charAt(0) == '\uF0FC') {
                nst.add(l);
                Log.d("nst", "loaded " + l + "," + nst.size());

            }

        }
    }
    public void loadscene(String title){
        initV();
        NTC=0;
        fr.fileRead(this,title);
        BM =  fr.textsizegetter();
        Log.d("asdf","loaded ");
        load_head();
    }


    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(!slc_md) {
                    if (NTC >= nst.size() - 1) {
                        NTC = 0;
                    }
                        if (NTC == 0 && nst.get(0) >= 1) {
                            int gyoumax = nst.get(0);
                            for (int ss = 0; ss < gyoumax; ss++) {
                            scriptloader(fr.textgetter(ss));
                            }
                    } else {
                    }
                    if (NTC > 0) {
                        int retumax = nst.get(NTC);
                        for (int sn = nst.get(NTC - 1) + 1; sn < retumax; sn++) {
                            scriptloader(fr.textgetter(sn));
                        }

                    }
                    loadtxt(nst.get(NTC));
                    NTC++;
                }else{
                    selEngine(Loaded.substring(1));
                }
                Log.d("TouchEvent", "getAction()" + "ACTION_DOWN");
                break;
            case MotionEvent.ACTION_UP:
                Log.d("TouchEvent", "getAction()" + "ACTION_UP");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("TouchEvent", "getAction()" + "ACTION_MOVE");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d("TouchEvent", "getAction()" + "ACTION_CANCEL");
                break;
        }
        return true;
    }

    public void initV() {
        i=0;
        GC = 0;
        TMPTXT[0] = "";
        TMPTXT[1] = "";
        TMPTXT[2] = "";
        TMPKKK = "";
    }
    String textenc="A";
    public void loadtxt(int nt) {
        String head_text;
        Log.d("LOAD:", "NAIYO:" + NTC);
        textenc = fr.textgetter(nt);
        head_text=textenc.substring(0,1);
        textenc = textenc.substring(1);
        if(head_text.charAt(0)=='\uF0FC'){
            initV();
            String result = "";
            if (TLDE) {
                mTimer.cancel();
                for (int l = 0; l < textenc.length(); l++) {
                    TLDE = true;
                    if (textenc.charAt(l) == '\uF0FA') {
                        GC++;
                    } else if (textenc.charAt(l) == '\uF000') {
                        TMPTXT[GC] += "<" + getString(R.string.RED);
                    } else if (textenc.charAt(l) == '\uF001') {
                        TMPTXT[GC] += "<" + getString(R.string.GREEN);
                    } else if (textenc.charAt(l) == '\uF002') {
                        TMPTXT[GC] += "<" + getString(R.string.BLUE);
                    } else if (textenc.charAt(l) == '\uF003') {
                        TMPTXT[GC] += "<" + getString(R.string.CYAN);
                    } else if (textenc.charAt(l) == '\uF004') {
                        TMPTXT[GC] += "<" + getString(R.string.MAGENTA);
                    } else if (textenc.charAt(l) == '\uF005') {
                        TMPTXT[GC] += "<" + getString(R.string.YELLOW);
                    } else if (textenc.charAt(l) == '\uF006') {
                        TMPTXT[GC] += "<" + getString(R.string.CC0);
                    } else if (textenc.charAt(l) == '\uF007') {
                        TMPTXT[GC] += "<" + getString(R.string.CC1);
                    } else if (textenc.charAt(l) == '\uF008') {
                        TMPTXT[GC] += "<" + getString(R.string.CC2);
                    } else if (textenc.charAt(l) == '\uF009') {
                        TMPTXT[GC] += "<" + getString(R.string.CC3);
                    } else if (textenc.charAt(l) == '\uF00A') {
                        TMPTXT[GC] += "<" + getString(R.string.CC4);
                    } else if (textenc.charAt(l) == '\uF00B') {
                        TMPTXT[GC] += "<" + getString(R.string.CC5);
                    } else if (textenc.charAt(l) == '\uF00c') {
                        TMPTXT[GC] += "<" + getString(R.string.CC6);
                    } else if (textenc.charAt(l) == '\uF00d') {
                        TMPTXT[GC] += "<" + getString(R.string.CC7);
                    } else if (textenc.charAt(l) == '\uF00e') {
                        TMPTXT[GC] += "<" + getString(R.string.CC8);
                    } else if (textenc.charAt(l) == '\uF00f') {
                        TMPTXT[GC] += "<" + getString(R.string.CC9);
                    } else if (textenc.charAt(l) == '\uF010') {
                        TMPTXT[GC] += "<" + getString(R.string.CC10);
                    } else if (textenc.charAt(l) == '\uF011') {
                        TMPTXT[GC] += "<" + getString(R.string.CC11);
                    } else if (textenc.charAt(l) == '\uF012') {
                        TMPTXT[GC] += "<" + getString(R.string.CC12);
                    } else if (textenc.charAt(l) == '\uF013') {
                        TMPTXT[GC] += "<" + getString(R.string.CC13);
                    } else if (textenc.charAt(l) == '\uF014') {
                        TMPTXT[GC] += "<" + getString(R.string.CC14);
                    } else if (textenc.charAt(l) == '\uF015') {
                        TMPTXT[GC] += "<" + getString(R.string.CC15);
                    } else if (textenc.charAt(l) == '\uF016') {
                        TMPTXT[GC] += "<" + getString(R.string.CC16);
                    } else if (textenc.charAt(l) == '\uF017') {
                        TMPTXT[GC] += "<" + getString(R.string.CC17);
                    } else if (textenc.charAt(l) == '\uF018') {
                        TMPTXT[GC] += "<" + getString(R.string.CC18);
                    } else if (textenc.charAt(l) == '\uF019') {
                        TMPTXT[GC] += "<" + getString(R.string.CC19);
                    } else if (textenc.charAt(l) == '\uF01A') {
                        TMPTXT[GC] += "<" + getString(R.string.CC20);
                    } else if (textenc.charAt(l) == '\uF01B') {
                        TMPTXT[GC] += "<" + getString(R.string.CC21);
                    } else if (textenc.charAt(l) == '\uF01C') {
                        TMPTXT[GC] += "<" + getString(R.string.CC22);
                    } else if (textenc.charAt(l) == '\uF01D') {
                        TMPTXT[GC] += "<" + getString(R.string.CC23);
                    } else if (textenc.charAt(l) == '\uF01E') {
                        TMPTXT[GC] += "<" + getString(R.string.CC24);
                    } else if (textenc.charAt(l) == '\uF01F') {
                        TMPTXT[GC] += "<" + getString(R.string.BLACK);
                    } else if (textenc.charAt(l) == '\uF020') {
                        TMPTXT[GC] += "<" + getString(R.string.end);
                    } else {
                        TMPTXT[GC] += textenc.charAt(l);
                        //Log.d("LOADEDTEXT:", "lows:" + GC + 1 + "NAIYO:" + TMPTXT[GC]);
                    }

                    ra.setText(Html.fromHtml(TMPTXT[0]));
                    rb.setText(Html.fromHtml(TMPTXT[1]));
                    rc.setText(Html.fromHtml(TMPTXT[2]));
                    i++;
                }

                TLDE=false;




            } else {
                initV();
                hyoujimoji(250);


            }}else{
            scriptloader(fr.textgetter(nt));
            Log.d("Other:", "ERROR");


        }

        }

        public void hyoujimoji(int sokudo){

        mTimer = new Timer(false);
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                saiseityu=true;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if ((i < textenc.length())) {
                            TLDE=true;
                            if (textenc.charAt(i) == '\uF0FA') {
                                GC++;
                            }else if(textenc.charAt(i) == '\uF000'){TMPTXT[GC]+="<"+getString(R.string.RED);
                                TMPTXT[GC] += textenc.charAt(i+1);
                                i++;
                            }else if(textenc.charAt(i) == '\uF001'){TMPTXT[GC]+="<"+getString(R.string.GREEN);
                                TMPTXT[GC] += textenc.charAt(i+1);
                                i++;
                            }else if(textenc.charAt(i) == '\uF002'){TMPTXT[GC]+="<"+getString(R.string.BLUE);
                                TMPTXT[GC] += textenc.charAt(i+1);
                                i++;
                            }else if(textenc.charAt(i) == '\uF003'){TMPTXT[GC]+="<"+getString(R.string.CYAN);TMPTXT[GC] += textenc.charAt(i+1);
                                i++;
                            }else if(textenc.charAt(i) == '\uF004'){TMPTXT[GC]+="<"+getString(R.string.MAGENTA);
                                TMPTXT[GC] += textenc.charAt(i+1);
                                i++;
                            }else if(textenc.charAt(i) == '\uF005'){TMPTXT[GC]+="<"+getString(R.string.YELLOW);
                                TMPTXT[GC] += textenc.charAt(i+1);
                                i++;
                            }else if(textenc.charAt(i) == '\uF006'){TMPTXT[GC]+="<"+getString(R.string.CC0);
                                TMPTXT[GC] += textenc.charAt(i+1);
                                i++;
                            }else if(textenc.charAt(i) == '\uF007'){TMPTXT[GC]+="<"+getString(R.string.CC1);
                                TMPTXT[GC] += textenc.charAt(i+1);
                                i++;
                            }else if(textenc.charAt(i) == '\uF008'){TMPTXT[GC]+="<"+getString(R.string.CC2);TMPTXT[GC] += textenc.charAt(i+1);
                                i++;
                            }else if(textenc.charAt(i) == '\uF009'){TMPTXT[GC]+="<"+getString(R.string.CC3);
                                TMPTXT[GC] += textenc.charAt(i+1);
                                i++;
                            }else if(textenc.charAt(i) == '\uF00A'){TMPTXT[GC]+="<"+getString(R.string.CC4);
                                TMPTXT[GC] += textenc.charAt(i+1);
                                i++;
                            }else if(textenc.charAt(i) == '\uF00B'){TMPTXT[GC]+="<"+getString(R.string.CC5);
                                TMPTXT[GC] += textenc.charAt(i+1);
                                i++;
                            }else if(textenc.charAt(i) == '\uF00c'){TMPTXT[GC]+="<"+getString(R.string.CC6);
                                TMPTXT[GC] += textenc.charAt(i+1);
                                i++;
                            }else if(textenc.charAt(i) == '\uF00d'){TMPTXT[GC]+="<"+getString(R.string.CC7);
                                TMPTXT[GC] += textenc.charAt(i+1);
                                i++;
                            }else if(textenc.charAt(i) == '\uF00e'){TMPTXT[GC]+="<"+getString(R.string.CC8);
                                TMPTXT[GC] += textenc.charAt(i+1);
                                i++;
                            }else if(textenc.charAt(i) == '\uF00f'){TMPTXT[GC]+="<"+getString(R.string.CC9);
                                TMPTXT[GC] += textenc.charAt(i+1);
                                i++;
                            }else if(textenc.charAt(i) == '\uF010'){TMPTXT[GC]+="<"+getString(R.string.CC10);
                                TMPTXT[GC] += textenc.charAt(i+1);
                                i++;
                            }else if(textenc.charAt(i) == '\uF011'){TMPTXT[GC]+="<"+getString(R.string.CC11);
                                TMPTXT[GC] += textenc.charAt(i+1);
                                i++;
                            }else if(textenc.charAt(i) == '\uF012'){TMPTXT[GC]+="<"+getString(R.string.CC12);
                                TMPTXT[GC] += textenc.charAt(i+1);
                                i++;
                            }else if(textenc.charAt(i) == '\uF013'){TMPTXT[GC]+="<"+getString(R.string.CC13);
                                TMPTXT[GC] += textenc.charAt(i+1);
                                i++;
                            }else if(textenc.charAt(i) == '\uF014'){TMPTXT[GC]+="<"+getString(R.string.CC14);
                                TMPTXT[GC] += textenc.charAt(i+1);
                                i++;
                            }else if(textenc.charAt(i) == '\uF015'){TMPTXT[GC]+="<"+getString(R.string.CC15);
                                TMPTXT[GC] += textenc.charAt(i+1);
                                i++;
                            }else if(textenc.charAt(i) == '\uF016'){TMPTXT[GC]+="<"+getString(R.string.CC16);
                                TMPTXT[GC] += textenc.charAt(i+1);
                                i++;
                            }else if(textenc.charAt(i) == '\uF017'){TMPTXT[GC]+="<"+getString(R.string.CC17);
                                TMPTXT[GC] += textenc.charAt(i+1);
                                i++;
                            }else if(textenc.charAt(i) == '\uF018'){TMPTXT[GC]+="<"+getString(R.string.CC18);
                                TMPTXT[GC] += textenc.charAt(i+1);
                                i++;
                            }else if(textenc.charAt(i) == '\uF019'){TMPTXT[GC]+="<"+getString(R.string.CC19);
                                TMPTXT[GC] += textenc.charAt(i+1);
                                i++;
                            }else if(textenc.charAt(i) == '\uF01A'){TMPTXT[GC]+="<"+getString(R.string.CC20);
                                TMPTXT[GC] += textenc.charAt(i+1);
                                i++;
                            }else if(textenc.charAt(i) == '\uF01B'){TMPTXT[GC]+="<"+getString(R.string.CC21);
                                TMPTXT[GC] += textenc.charAt(i+1);
                                i++;
                            }else if(textenc.charAt(i) == '\uF01C'){TMPTXT[GC]+="<"+getString(R.string.CC22);
                                TMPTXT[GC] += textenc.charAt(i+1);
                                i++;
                            }else if(textenc.charAt(i) == '\uF01D'){TMPTXT[GC]+="<"+getString(R.string.CC23);
                                TMPTXT[GC] += textenc.charAt(i+1);
                                i++;
                            }else if(textenc.charAt(i) == '\uF01E'){TMPTXT[GC]+="<"+getString(R.string.CC24);
                                TMPTXT[GC] += textenc.charAt(i+1);
                                i++;
                            }else if(textenc.charAt(i) == '\uF01F'){TMPTXT[GC]+="<"+getString(R.string.BLACK);
                                TMPTXT[GC] += textenc.charAt(i+1);
                                i++;
                            }else if(textenc.charAt(i) == '\uF020'){TMPTXT[GC]+="<"+getString(R.string.end);
                            } else {
                                TMPTXT[GC] += textenc.charAt(i);
                                //Log.d("LOADEDTEXT:", "lows:" + GC + 1 + "NAIYO:" + TMPTXT[GC]);
                            }

                            //Log.d("LOADEDTEXT:", "lows:" + GC + 1 + "NAIYO:" + TMPTXT[GC]);
                            //TMPTXT[0]+="</font>";
                            //TMPTXT[1]+="</font>";
                            //TMPTXT[2]+="</font>";
                            ra.setText(Html.fromHtml(TMPTXT[0]));
                            rb.setText(Html.fromHtml(TMPTXT[1]));
                            rc.setText(Html.fromHtml(TMPTXT[2]));
                            i++;

                        }else{
                            TLDE=false;
                            Log.d("LOADEDTEXT:", "lows counted"  );
                            cancel();
                            saiseityu=false;


                        }
                    }
                });

            }
        },0,sokudo);







            /*
            mTimer = new Timer(false);
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {


                            }
                        }
                    });

                }
            }, 0, 500);*/



            //Switch文の使った場合も書いておくが、結構使い物にならない（caseとdefaultが同時に実行され、\nがエスケープされなくなる（誰かに教えるときのためのメモ、見る人いるかな？（笑い

            /*
            switch(txt[0].charAt(i))
            {
                case '\b':
                    //result += "\\b";break;
                case '\t':
                    //result += "\\t";break;
                case '\n':
                    GC++;
                    //result += "\\n";break;
                case '\f':
                    //result += "\\f";break;
                case '\r':
                    //result += "\\r";break;
                case '\"':
                    //result += "\\\"";break;
                case '\'':
                    //result += "\\\'";break;
                case '\\':
                    //result += "\\\\";break;
                default:
                    TMPTXT[GC] += txt[0].charAt(i);

            }*/

            Log.d("LOADEDTEXT:", "lows:" + 1 + "NAIYO:" + TMPTXT[0]);
            Log.d("LOADEDTEXT:", "lows:" + 2 + "NAIYO:" + TMPTXT[1]);
            Log.d("LOADEDTEXT:", "lows:" + 3 + "NAIYO:" + TMPTXT[2]);
            //r1.setText(TMPTXT[0]);
            //r2.setText(TMPTXT[1]);
            //r3.setText(TMPTXT[2]);
    }

}
