package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Main3Activity extends AppCompatActivity implements  Runnable {
    private  final String TAG ="Rate";
    private  float dollarRate =0.1f;
    private  float euroRate =0.2f;
    private  float wonRate =0.3f;
    private  String updateDate="";
     EditText rmb;
     TextView show;
     Handler handler;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        rmb= (EditText) findViewById(R.id.rmb);
        show =(TextView)findViewById(R.id.showout);
        SharedPreferences sharedPreferences= getSharedPreferences("myrate", AppCompatActivity.MODE_PRIVATE);
        dollarRate = sharedPreferences.getFloat("dollar_rate",0.0f);
        euroRate = sharedPreferences.getFloat("euro_rate",0.0f);
        wonRate = sharedPreferences.getFloat("won_rate",0.0f);
        updateDate =sharedPreferences.getString("update_date","");

        Date today =Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String todayStr=sdf.format(today);


        Log.i(TAG,"onCareate:sp dollarRate="+ dollarRate);
        Log.i(TAG,"onCareate:sp euroRate="+ euroRate);
        Log.i(TAG,"onCareate:sp wonRate="+ wonRate);
        Log.i(TAG,"onCareate:sp updateDate="+ updateDate);
        Log.i(TAG,"onCareate:sp todayStr="+ todayStr);
        if(!todayStr.equals(updateDate)){
            Log.i(TAG,"onCareate:需要更新");
            Thread t = new Thread(this);
            t.start();
        }else{
            Log.i(TAG,"onCareate:不需要更新");
        }



         handler =new Handler(){
            @Override
            public  void handleMessage(Message msg){
                if(msg.what==5){
                    Bundle bd1=(Bundle) msg.obj;
                    dollarRate=bd1.getFloat("dollar-rate");
                    euroRate=bd1.getFloat("euro-rate");
                    wonRate=bd1.getFloat("won-rate");

                    Log.i(TAG,"handleMessage:dollarRate:"+dollarRate);
                    Log.i(TAG,"handleMessage:euroRate:"+euroRate);
                    Log.i(TAG,"handleMessage:wonRate:"+wonRate);

                    SharedPreferences sharedPreferences= getSharedPreferences("myrate", AppCompatActivity.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putFloat("dollar_rate",dollarRate);
                    editor.putFloat("euro_rate",euroRate);
                    editor.putFloat("won_rate",wonRate);
                    editor.putString("update_date",todayStr);
                    editor.apply();
                    Toast.makeText(Main3Activity.this,"汇率已更新",Toast.LENGTH_SHORT).show();
                }
                super.handleMessage(msg);
            }
        };

    }



    public void onClick(View btn){
        Log.i(TAG,"onClick:");
        String str = rmb.getText().toString();
        Log.i(TAG,"onClick:get str="+ str);
        float r=0;
        if(str.length()>0){
         r= Float.parseFloat(str);}
        else {
            Toast.makeText(this,"qinshuru",Toast.LENGTH_SHORT).show();

        }
        Log.i(TAG,"onClick: r="+ r);

        if(btn.getId()==R.id.dollar){
             show.setText(String.format("%.2f",r*dollarRate));
        }else  if(btn.getId()==R.id.euro){
            show.setText(String.format("%.2f",r*euroRate));
        }else {
            show.setText(String.format("%.2f",r*wonRate));
        }
    }



    public void openOne(View btn) {
        Log.d("open", "openOne");
        openConfig();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_set){

            openConfig();
        }else if(item.getItemId()==R.id.open_list){
            Intent list = new Intent(this, MyList2Activity.class);
            startActivity(list);
        }
        return super.onOptionsItemSelected(item);
    }

    private void openConfig() {
        Intent hello = new Intent(this, Main4Activity.class);
        hello.putExtra("dollar2", dollarRate);
        hello.putExtra("euro2", euroRate);
        hello.putExtra("won2", wonRate);

        Log.i(TAG, "openOne:dollar2=" + dollarRate);
        Log.i(TAG, "openOne:euro2=" + euroRate);
        Log.i(TAG, "openOne:won2=" + wonRate);
        startActivity(hello);
        startActivityForResult(hello, 1);
    }

    @Override
    protected void  onActivityResult(int requestCode,int resultCode ,Intent data){
        if(requestCode==1 && resultCode==2 ){
            Bundle bundle =data.getExtras();
            dollarRate = bundle.getFloat("key_dollar",0.1f);
            euroRate=bundle.getFloat("key_euro",0.1f);
            wonRate=bundle.getFloat("key_won",0.1f);
            Log.i(TAG,"onActtivityResult:dollarRate="+dollarRate);
            Log.i(TAG,"onActtivityResult:euroRate="+euroRate);
            Log.i(TAG,"onActtivityResult:wonRate="+wonRate);

            SharedPreferences sharedPreferences= getSharedPreferences("myrate", AppCompatActivity.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putFloat("dollar_rate",dollarRate);
            editor.putFloat("euro_rate",euroRate);
            editor.putFloat("won_rate",wonRate);
            editor.commit();
            Log.i(TAG,"onActivity:数据已经保存到sharedPreferences");

        }
     super.onActivityResult(requestCode,resultCode,data);
    }
    @Override
    public  void  run() {
        Log.i(TAG, "run: run().....");
        for (int i = 1; i < 6; i++) {
            Log.i(TAG, "run:i=" + i);
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Bundle bundle = new Bundle();


        URL url = null;
        /*try {
            url = new URL("http://www.usd-cny.com/bankofchina.htm");
            HttpURLConnection http = (HttpURLConnection) url .openConnection();
            InputStream in = http.getInputStream();

            String html = inputStream2String(in);
            Log.i(TAG,"run:html="+html);
           Document doc=Jsoup.parse(html);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        Document doc = null;
        try {
            doc = (Document) Jsoup.connect("http://www.boc.cn/sourcedb/whpj/").get();

            Elements tables = (Elements) doc.getElementsByTagName("table");
            Element table2=tables.get(1);
            Elements tds = table2.getElementsByTag("td");
            for (int i = 0; i < tds.size(); i += 8) {
                Element td1 = tds.get(i);
                Element td2 = tds.get(i + 5);
                Log.i(TAG, "run:text=" + td1.text());
                Log.i(TAG, "run:val=" + td2.text());
                String str1 = td1.text();
                String val = td2.text();
                if ("美元".equals(str1)) {
                    bundle.putFloat("dollar-rate", 100f / Float.parseFloat(val));
                } else if ("欧元".equals(str1)) {
                    bundle.putFloat("euro-rate", 100f / Float.parseFloat(val));
                } else if ("韩国元".equals(str1)) {
                    bundle.putFloat("won-rate", 100f / Float.parseFloat(val));
                }

            }

            /*for(Elements td: tds){
                Log.i(TAG,"run:td="+ td);
                Log.i(TAG,"run:text="+ td.text());
                Log.i(TAG,"run:html="+ td.html());
            }
            Elements newHeadlines = doc.select("#mp-itn b a");
            for(Elements headline: newsHeadlines){
                Log.i(TAG,"%s\n\t5s"+headline.attr("title")+headline.absUrl("href");
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }

        Message msg = handler.obtainMessage();
        // msg.what=5;
        //msg.obj="hello";
        msg.obj = bundle;
        handler.sendMessage(msg);
    }

        private String inputStream2String (InputStream inputStream) throws IOException {
            final int bufferSize = 1024;
            final char[] buffer = new char[bufferSize];
            final StringBuilder out = new StringBuilder();
            Reader in = new InputStreamReader(inputStream, "gb2312");
            for (; ; ) {
                int rsz = in.read(buffer, 0, buffer.length);
                if (rsz < 0)
                    break;
                out.append(buffer, 0, rsz);
            }
            return out.toString();
        }


}


