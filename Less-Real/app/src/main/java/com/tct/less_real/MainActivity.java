package com.tct.less_real;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import uk.co.deanwild.flowtextview.FlowTextView;


public class MainActivity extends ActionBarActivity {
    SharedPreferences pref;
    String user;
    ActionBar actionBar;
    MenuItem mn;
    Context act=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] aa=new String[]{"http://opnz.freeiz.com/","http://www.less-real.com/imagevault/uploaded/quotefaces/Saito-18056.jpg","http://opnz.freeiz.com/un.php"};
        ListView mainList = (ListView)findViewById(R.id.listView1);
        // ImageView img=(ImageView)findViewById(R.id.img);
      //  Log.d("bar",""+getActionBar().toString());

        new Connect(mainList,this,getActionBar()).execute(aa);
        pref = getSharedPreferences("cookie", Context.MODE_PRIVATE);
        if (pref.contains("user"))
        {
            user=pref.getString("user","");
            Log.d("State", "Found: " + user);
        }
        else
            Log.d("State","USer NOTFOUND: "+user);
       // new Connect(mainList,this,actionBar).execute(aa);



        mainList.setLongClickable(true);
        mainList.setClickable(true);
        mainList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(act, "Added to favourites", Toast.LENGTH_LONG).show();
                Log.d("long clicked", "pos: " + pos);

                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if(user==null)
        {
            mn= menu.findItem(R.id.login);
                    mn.setTitle("Login");
        }
        else
        {
            MenuItem mn= menu.findItem(R.id.login);
            mn.setTitle(user);
        }
        Log.d("State","changed:");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.login) {
            if(user==null)
            {
                loginSeq();

                pref = getSharedPreferences("cookie", Context.MODE_PRIVATE);
                if (pref.contains("user")) {
                    user = pref.getString("user", "");
                    mn.setTitle(user);
                    finish();
                }
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void loginSeq()
    {

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        /*
        if(code==1)
            findViewById(R.id.login).setVisibility(View.GONE);
        */
    }
}
