package com.byted.camp.todolist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



/**
 * Created on 2019/1/22.
 *
 * @author xuyingyi@bytedance.com (Yingyi Xu)
 */
public class TodoDbHelper extends SQLiteOpenHelper {

    // TODO 定义数据库名、版本；创建数据库
    public static final int VERSION = 8;

    public TodoDbHelper(Context context) {
        super(context, "todo", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("onCreate", "onCreate: ");
        db.execSQL(TodoContract.getSqlCreateEntrier());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL(TodoContract.getSqlDeleteEntries());
        //db.execSQL(TodoContract.getSqlCreateEntrier());
        //onCreate(db);

        for(int i=oldVersion; i < newVersion ; i++){
            switch (i){
                case 2:
                    //do sth
                    break;
            }
        }
    }

}
