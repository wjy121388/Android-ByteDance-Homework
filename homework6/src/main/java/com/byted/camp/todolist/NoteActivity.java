package com.byted.camp.todolist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.byted.camp.todolist.db.TodoContract;
import com.byted.camp.todolist.db.TodoDbHelper;

import java.util.Calendar;
import java.util.Date;

public class NoteActivity extends AppCompatActivity {

    private static final String TAG = NoteActivity.class.getSimpleName();
    private EditText editText;
    private Button addBtn;
    private RadioGroup rg;

    private TodoDbHelper mDbHelper ;
    private SQLiteDatabase mDb;
    private int priority = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        setTitle(R.string.take_a_note);

        editText = findViewById(R.id.edit_text);
        editText.setFocusable(true);
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.showSoftInput(editText, 0);
        }

        addBtn = findViewById(R.id.btn_add);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence content = editText.getText();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(NoteActivity.this,
                            "No content to add", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean succeed = saveNote2Database(content.toString().trim(),priority);
                if (succeed) {
                    Toast.makeText(NoteActivity.this,
                            "Note added", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                } else {
                    Toast.makeText(NoteActivity.this,
                            "Error", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

        rg = findViewById(R.id.rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkdId) {
                RadioButton rb = findViewById(checkdId);
                if(checkdId == R.id.rb1){
                    Log.d(TAG,"radiobutton 1 clicked");
                    priority = 2;
                }else if(checkdId == R.id.rb2){
                    Log.d(TAG,"radiobutton 2 clicked");
                    priority = 1;
                }else if(checkdId == R.id.rb3){
                    Log.d(TAG,"radiobutton 3 clicked");
                    priority = 0;
                }
                else{
                    Log.e(TAG,"error checkdId");
                }
            }
        });


        mDbHelper = new TodoDbHelper(this);
        mDb = mDbHelper.getWritableDatabase();

    }

    @Override
    protected void onDestroy() {
        mDbHelper.close();
        super.onDestroy();
    }

    private boolean saveNote2Database(String content,int priority) {
        // TODO 插入一条新数据，返回是否插入成功
        ContentValues values = new ContentValues();
        Date mDate = Calendar.getInstance().getTime();
        //values.put(TodoContract.FeedEntry.COLUMN_NAME_TITLE_ID, );
        values.put(TodoContract.FeedEntry.COLUMN_NAME_TITLE_DATA, mDate.getTime());
        values.put(TodoContract.FeedEntry.COLUMN_NAME_TITLE_STATE, false);
        values.put(TodoContract.FeedEntry.COLUME_NAME_TITLE_CONTENT,content);
        values.put(TodoContract.FeedEntry.COLUMN_NAME_TITLE_PRIORITY,priority);
        long newID = mDb.insert(TodoContract.FeedEntry.TABLE_NAME,null,values);
        return newID != 0;
    }
}
