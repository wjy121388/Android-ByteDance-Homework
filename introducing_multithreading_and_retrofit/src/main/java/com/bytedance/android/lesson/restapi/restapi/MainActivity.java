package com.bytedance.android.lesson.restapi.restapi;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bytedance.android.lesson.restapi.restapi.bean.Joke;
import com.bytedance.android.lesson.restapi.restapi.bean.OSList;
import com.bytedance.android.lesson.restapi.restapi.utils.NetworkUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private static final int GET_JOKE = 0;
    public static String RAW =
            "{\"os\":[{\"name\":\"Pie\",\"code\":28}," +
                    "{\"name\":\"Oreo\",\"code\":27}]}";
    public TextView mTv;
    private View mBtn;
    private Joke j;
    private String responsDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTv = findViewById(R.id.tv);
        mBtn = findViewById(R.id.btn);
//        mTv.setText(parseFirstNameWithJSON()); // json test
//        mTv.setText(parseFirstNameWithGson()); // json test
        initListeners();
    }

    private void initListeners() {
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                requestData(v);
            }
        });
    }

    private static String parseFirstNameWithGson() {
        OSList list = new Gson()
                .fromJson(RAW, OSList.class);
        return list.getOs()[0].getName();
    }

    private static String parseFirstNameWithJSON() {
        String result = null;
        try {
            JSONObject root = new JSONObject(RAW);
            JSONArray os = root.optJSONArray("os");
            result = os.optJSONObject(0).
                    optString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void requestData(View view) {
        // HttpURLConnection
//        String s = NetworkUtils.getResponseWithHttpURLConnection("https://api.icndb.com/jokes/random");
//        mTv.setText(s);

        //Retrofit
        new Thread(new Runnable() {
            @Override
            public void run() {
                j = NetworkUtils.getResponseWithRetrofit();
                //responsDate = NetworkUtils.getResponseWithHttpURLConnection("https://api.icndb.com/jokes/random");
                mHandler.sendMessage(Message.obtain(mHandler,GET_JOKE));
            }
        }).start();


    }
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_JOKE:
                    mTv.setText(j.getValue().getJoke());
                    //mTv.setText(responsDate);
                    break;
            }
        }
    };
}