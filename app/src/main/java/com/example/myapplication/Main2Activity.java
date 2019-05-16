package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Main2Activity extends AppCompatActivity {
     TextView  score;
    TextView  score2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
       score=(TextView) findViewById(R.id.score);
       score2=(TextView) findViewById(R.id.score2);
    }
    public void btn2(View btn){
        if(btn.getId()==R.id.btn2){
        showScore(1);}
        if(btn.getId()==R.id.btn2b){
            showScore2(1);}
    }
    public void btn3(View btn){
        if(btn.getId()==R.id.btn3){
            showScore(2);}
        if(btn.getId()==R.id.btn3b){
            showScore2(2);}
    }
    public void btn4(View btn){
        if(btn.getId()==R.id.btn4){
            showScore(3);}
        if(btn.getId()==R.id.btn4b){
            showScore2(3);}
    }
    public void btn1(View btn){
        TextView out =(TextView)findViewById(R.id.score);
        out.setText("0");
        ((TextView)findViewById(R.id.score2)).setText("0");
    }
    private  void  showScore(int inc){
        Log.i("show","inc="+inc);
    String oldScore=(String)score.getText();
    int newScore=Integer.parseInt(oldScore)+inc;
    score.setText(""+ newScore);
    }
    private  void  showScore2(int inc){
        Log.i("show","inc="+inc);
        String oldScore=(String)score2.getText();
        int newScore=Integer.parseInt(oldScore)+inc;
        score2.setText(""+ newScore);
    }



}




