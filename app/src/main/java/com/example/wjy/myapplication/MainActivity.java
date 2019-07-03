package com.example.wjy.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView tv1;
    private EditText etv1;
    private Button bt1;
    private Button bt2;
    private RadioGroup rg;
    private RadioButton rb1;
    private RadioButton rb2;
    private ImageView img1;
    private final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tv1 = findViewById(R.id.textView);
        etv1 = findViewById(R.id.editText);
        bt1 = findViewById(R.id.button);
        bt2 = findViewById(R.id.button2);
        rg = findViewById(R.id.rg);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        img1 = findViewById(R.id.imageView);
        etv1.setVisibility(View.GONE);

        bt1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d(TAG,"button 'click now' clicked");
                Toast.makeText(MainActivity.this,"我被点了一下", Toast.LENGTH_SHORT).show();
                tv1.setText(R.string.button_click);
                img1.setImageDrawable(getDrawable(R.drawable.ic_launcher_background));
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"button 'back' clicked");
                tv1.setText(R.string.text1_str);
                img1.setImageDrawable(getDrawable(R.mipmap.ic_launcher));
            }
        });

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkdId) {
                RadioButton rb = findViewById(checkdId);
                if(checkdId == R.id.rb1){
                    Log.d(TAG,"radiobutton 1 clicked");
                    tv1.setVisibility(View.VISIBLE);
                    bt1.setVisibility(View.VISIBLE);
                    bt2.setVisibility(View.VISIBLE);
                    etv1.setVisibility(View.GONE);
                }else if(checkdId == R.id.rb2){
                    Log.d(TAG,"radiobutton 2 clicked");
                    tv1.setVisibility(View.GONE);
                    bt1.setVisibility(View.GONE);
                    bt2.setVisibility(View.GONE);
                    etv1.setVisibility(View.VISIBLE);
                }else{
                    Log.e(TAG,"error checkdId");
                }
            }
        });
        
        startActivity(new Intent(this,MyActivity.class));

    }

}
