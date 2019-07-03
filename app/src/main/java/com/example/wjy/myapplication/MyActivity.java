package com.example.wjy.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MyActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        myView myView = new myView(this);

    }


    public static class myView extends View{
        private Paint paint;
        public myView(Context context) {
            super(context);
            initPaint();
        }
        private  void initPaint(){
            paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStrokeWidth(40);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(200,200,100,paint);
        }
    }



}
