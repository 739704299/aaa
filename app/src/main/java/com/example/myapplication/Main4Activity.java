package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class Main4Activity extends AppCompatActivity {
   public  final  String TAG ="ConfigActivity";
   EditText dollarText;
   EditText euroText;
   EditText wonText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
       Intent intent = getIntent();
       float dollara=intent.getFloatExtra("dollar2",0.0f);
       float euroa=intent.getFloatExtra("euro2",0.0f);
       float wona=intent.getFloatExtra("won2",0.0f);
       Log.i(TAG,"onCreate:dollara="+ dollara);
       Log.i(TAG,"onCreate:euroa="+ euroa);
       Log.i(TAG,"onCreate:wona="+ wona);
       dollarText =(EditText)findViewById(R.id.dollar2);
       euroText=(EditText)findViewById(R.id.euro2);
       wonText=(EditText)findViewById(R.id.won2);

       dollarText.setText(String.valueOf(dollara));
       euroText.setText(String.valueOf(euroa));
       wonText.setText(String.valueOf(wona));
    }
    public  void  save(View btn ){
         Log.i(TAG,"save: ");
        float newDollar = Float.parseFloat(dollarText.getText().toString());
        float newEuro = Float.parseFloat(euroText.getText().toString());
        float newWon = Float.parseFloat(wonText.getText().toString());
        Log.i(TAG,"save:获取到新的值");
        Log.i(TAG,"onCreate:newDollar="+ newDollar);
        Log.i(TAG,"onCreate:newEuro="+ newEuro);
        Log.i(TAG,"onCreate:newWon="+ newWon);

        Intent intent =getIntent();
        Bundle bdl =new Bundle();
        bdl.putFloat("key_dollar",newDollar);
        bdl.putFloat("key_euro",newEuro);
        bdl.putFloat("key_won",newWon);
        intent.putExtras(bdl);
        setResult(2,intent);

        finish();
    }
}
