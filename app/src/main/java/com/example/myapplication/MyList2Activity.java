package com.example.myapplication;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyList2Activity extends ListActivity implements Runnable, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private  final String TAG ="mylist2";
    Handler handler;
    private List<HashMap<String,String>>listItem;
    private SimpleAdapter listItemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initListView();
        this.setListAdapter(listItemAdapter);

        Thread thread =new Thread(this);
        thread.start();

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what==7){
                    listItem =(List<HashMap<String,String>>)msg.obj;
                    listItemAdapter = new SimpleAdapter(MyList2Activity.this,listItem,
                            R.layout.list_item,
                            new String[]{"ItemTitle","ItemDetail"},
                            new int[]{R.id.itemTitle,R.id.itemDetail}
                    );
                    setListAdapter(listItemAdapter);
                }
                super.handleMessage(msg);
            }
        };
        getListView().setOnItemClickListener(this);
        getListView().setOnItemLongClickListener(this);
    }

   /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initListView();
         MyAdapter myAdapter = new MyAdapter(this,R.layout.list_item,listItem);
       this.setListAdapter(myAdapter);
    }*/
    private void initListView(){
        listItem = new ArrayList<HashMap<String, String>>();
        for(int i=0 ; i<10;i++){
            HashMap<String,String>map =new HashMap<String, String>();
            map.put("ItemTitle","Rate:"+i);
            map.put("ItemDetail","detail"+i);
            listItem.add(map);
        }
        listItemAdapter = new SimpleAdapter(this,listItem,
                R.layout.list_item,
                new String[]{"ItemTitle","ItemDetail"},
                new int[]{R.id.itemTitle,R.id.itemDetail}
                );
    }
    public void run() {
        //获得网络数据，放入list带回主线程中
        List<HashMap<String,String>> retList =new ArrayList<HashMap<String,String>>();
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
                Log.i(TAG,"run: "+str1 + "==>"+val);
                HashMap<String,String>map =new HashMap<String,String>();
                map.put("ItemTitle",str1);
                map.put("ItemDetail",val);
                retList.add(map);
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "OnItemClick: paraent="+ parent );
        Log.i(TAG, "OnItemClick: view"+view);
        Log.i(TAG, "OnItemClick: position"+position);
        Log.i(TAG, "OnItemClick: id"+id);
        HashMap<String,String>map = (HashMap<String, String>) getListView().getItemAtPosition(position);
        String titleStr =map.get("ItemTitle");
        String detailStr =map.get("ItemDetail");
        Log.i(TAG, "OnItemClick: =titleStr "+ titleStr  );
        Log.i(TAG, "OnItemClick: =detailStr "+detailStr);


         TextView title = (TextView) view.findViewById(R.id.itemTitle);
        TextView detail = (TextView) view.findViewById(R.id.itemDetail);
        String title2=String.valueOf(title.getText());
        String detail2=String.valueOf(detail.getText());
        Log.i(TAG, "OnItemClick: =title2 "+ title2  );
        Log.i(TAG, "OnItemClick: =detail2 "+detail2);


        //打开新的页面传入数据
        Intent rateCalc =new Intent(this,RateCalcActivity.class);
        rateCalc.putExtra("title",titleStr);
        rateCalc.putExtra("rate",Float.valueOf(detailStr));

        startActivity(rateCalc);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        Log.i(TAG, "OnItemLongClick:长按列表项position "+position);

        //构造对话框进行提示
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("请确认是否删除当前数据").setPositiveButton("是",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "OnClick:对话框事件处理");
                        listItem.remove(position);
                        listItemAdapter.notifyDataSetChanged();
                    }
                }).
                setNegativeButton("否",null);
        builder.create().show();
        Log.i(TAG, "OnItemLongClick: size= "+listItem.size());
        return true;
    }
}
