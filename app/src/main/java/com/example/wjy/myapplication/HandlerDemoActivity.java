package com.example.wjy.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class HandlerDemoActivity extends AppCompatActivity {

    private static final String TAG = "HandlerDemoActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread t1 =  new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while(i<5){
                    i++;
                    mHandler.sendMessage(Message.obtain(mHandler,1));
                    mHandler.sendMessage(Message.obtain(mHandler,2));
                    mHandler.sendMessage(Message.obtain(mHandler,3));
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while(i<10){
                    i++;
                    mHandler.sendMessage(Message.obtain(mHandler,4));
                    mHandler.sendMessage(Message.obtain(mHandler,5));
                    mHandler.sendMessage(Message.obtain(mHandler,6));
                }
            }
        });
        t1.start();
        t2.start();

    }
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Log.i(TAG, "handleMessage: 1");
                    break;
                case 2:
                    Log.i(TAG, "handleMessage: 2");
                    break;
                case 3:
                    Log.i(TAG, "handleMessage: 3");
                    break;
                case 4:
                    Log.i(TAG, "handleMessage: 4");
                    break;
                case 5:
                    Log.i(TAG, "handleMessage: 5");
                    break;
                case 6:
                    Log.i(TAG, "handleMessage: 6");
                    break;
            }
        }
    };
}
