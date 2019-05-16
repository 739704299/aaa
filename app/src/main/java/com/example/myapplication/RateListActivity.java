package com.example.myapplication;

import android.app.ListActivity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListResourceBundle;

public class RateListActivity extends ListActivity implements Runnable {
    private  final String TAG ="Rate";
     String data[]={"one","two","three"};
     Handler handler;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_rate_list);
        final List<String> list1 =new ArrayList<String>();
        for(int i=1;i<100;i++){
            list1.add("item"+i);
        }
        ListAdapter adapter =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list1);
        setListAdapter(adapter);

        Thread thread =new Thread(this);

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what==7){
                    List<String> list2 =(List<String>)msg.obj;
                    ListAdapter adapter =new ArrayAdapter<String>(RateListActivity.this,android.R.layout.simple_list_item_1,list2);
                    setListAdapter(adapter);
                }
                super.handleMessage(msg);
            }
        };
    }

    @Override
    public void run() {
        List<String> retList =new ArrayList<String>();
        Document doc = null;
        try {
            Thread.sleep(3000);
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
                retList.add(str1 +"==>"+val);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Message msg = handler.obtainMessage(7);
        msg.obj = retList;
        handler.sendMessage(msg);
    }
}
