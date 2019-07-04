package com.bytedance.android.lesson.restapi.solution;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bytedance.android.lesson.restapi.solution.bean.Cat;
import com.bytedance.android.lesson.restapi.solution.newtork.ICatService;
import com.bytedance.android.lesson.restapi.solution.newtork.RetrofitManager;
import com.bytedance.android.lesson.restapi.solution.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.support.v7.widget.RecyclerView.Adapter;
import static android.support.v7.widget.RecyclerView.ViewHolder;

public class Solution2C1Activity extends AppCompatActivity {

    private static final String TAG = Solution2C1Activity.class.getSimpleName();
    public Button mBtn;
    public RecyclerView mRv;
    private List<Cat> mCats = new ArrayList<>();

    private String responsDate = "";
    private Response<Cat[]> response;
    private static final int LOAD_PIC = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution2_c1);
        mBtn = findViewById(R.id.btn);
        mRv = findViewById(R.id.rv);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(new Adapter() {
            @NonNull @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                ImageView imageView = new ImageView(viewGroup.getContext());
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                imageView.setAdjustViewBounds(true);
                return new MyViewHolder(imageView);
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
                ImageView iv = (ImageView) viewHolder.itemView;

                // TODO-C1 (4) Uncomment these 2 lines, assign image url of Cat to this url variable
                String url = mCats.get(i).getUrl();
                Glide.with(iv.getContext()).load(url).into(iv);
                //restoreBtn();
            }

            @Override public int getItemCount() {
                return mCats.size();
            }
        });
    }

    public static class MyViewHolder extends ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void requestData(View view) {
        mBtn.setText(R.string.requesting);
        mBtn.setEnabled(false);
        // TODO-C1 (3) Send request for 5 random cats here, don't forget to use {@link retrofit2.Call#enqueue}
        // Call restoreBtn() and loadPics(response.body()) if success
        // Call restoreBtn() if failure
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Retrofit r = RetrofitManager.get("https://api.thecatapi.com/");
                    Call<Cat[]> call = r.create(ICatService.class).randomCat();
                    call.enqueue(new Callback<Cat[]>() {
                        @Override
                        public void onResponse(@NonNull Call<Cat[]> call, @NonNull Response<Cat[]> response) {
                           // Cat[] cats = response.body();
                            List<Cat> cats1;
                            Log.d(TAG, "onResponse: ");
                            loadPics(new ArrayList<>(Arrays.asList(response.body())));
                            restoreBtn();
                        }
                        @Override
                        public void onFailure(Call<Cat[]> call, Throwable t) {
                            Log.d(TAG, "onFailure: ");
                            restoreBtn();
                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }).start();


    }



    private void loadPics(List<Cat> cats) {
        mCats = cats;
        mRv.getAdapter().notifyDataSetChanged();
    }

    private void restoreBtn() {
        mBtn.setText(R.string.request_data);
        mBtn.setEnabled(true);
    }
}
