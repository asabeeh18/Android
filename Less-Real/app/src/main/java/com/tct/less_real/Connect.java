package com.tct.less_real;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 2/18/2015.
 */
public class Connect extends AsyncTask<String,Integer,String>
{
    static List<Quote> objList=new ArrayList<>();
    static ListView mainList;
    static LIster customAdapter;
    static boolean set=false;
    Context act;
    ActionBar bar;
    static ProgressBar pBar;
    public Connect(ListView mainList,Context act,ActionBar bar)
    {
        pBar=(new MainActivity()).mProgress;
        this.act=act;
        this.bar=bar;
        //this.txt=txt;
        //this.img=img;
        //this.says=says;
        this.mainList=mainList;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
     //   pBar.setProgress(0);
        if(values[0].intValue()==0)
            pBar.setVisibility(View.VISIBLE);
        else
            pBar.setVisibility(View.GONE);
    }

    @Override
    protected String doInBackground(String... arg) {

        publishProgress(0);
        String link = arg[0];
        Log.d("State", "Calling Http at: " + link);
        String res= getText(link);
        res="{Quote:"+res+"}";
        try{
            objList=parseJSON(new JSONObject(res));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        publishProgress(1);
        return "";
        /*
        Log.d("State", "Calling Http1");
        send.says = getText(says);
        Log.d("State", "Calling Http2");
        try {
            URL newurl = new URL(imgLInk);
            send.img = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            return send;
        } catch (Exception e) {
            Log.d("State", "Error!!!!..: "+e.toString());
            return send;
        }
        */
    }

    protected void onPostExecute(String res) {
        Log.d("State", "Downloaded");

		//ading it in 10 times


        if(customAdapter==null) {

            Log.d("State", "Added in List");
            customAdapter = new LIster(objList, act);
            Log.d("State", "Constructor Lister");
            mainList.setAdapter(customAdapter);
            Log.d("State", "SET listner");
            mainList.setOnScrollListener(new EndlessScrollListener(mainList,act,bar));
        }
        else
        {

            customAdapter.notifyDataSetChanged();

            int firstVisibleItem = mainList.getFirstVisiblePosition();
            int oldCount = customAdapter.getCount();
            View view = mainList.getChildAt(0);
            int pos = (mainList == null ? 0 :  mainList.getTop());
            customAdapter.notifyDataSetChanged();
            mainList.setSelectionFromTop(firstVisibleItem, 0);

        }
        //  img.setImageBitmap(res.img);
        Log.d("State", "ALL Done !! STOp");

    }

    private List<Quote> parseJSON(JSONObject mySon)
    {

        String imgLInk;
        try
        {
            JSONArray qArray= mySon.getJSONArray("Quote");
            for(int i=0;i<qArray.length();i++)
            {
                Quote send = new Quote();
                JSONObject jQuote =qArray.getJSONObject(i);
                send.says=jQuote.getString("author");
                send.text=jQuote.getString("quote");


                imgLInk=jQuote.getString("image");
                Log.d("State", "Calling Http2");
                try {
                    URL newurl = new URL(imgLInk);
                    send.img = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());

                } catch (Exception e) {
                    e.printStackTrace();

                }
                objList.add(send);
            }
            MainActivity.start+=10;
            //Log.d("JSON",jQuote.getString("author"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return objList;
    }


    protected String getText(String link) {
        try {
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter
                    (conn.getOutputStream());
            wr.write(" ");
            wr.flush();
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line=null;
            // Read Server Response
            while((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }
            return sb.toString();
        } catch (Exception e) {
            return e.toString();
        }
    }
}