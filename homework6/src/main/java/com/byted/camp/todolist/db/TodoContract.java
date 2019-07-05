package com.byted.camp.todolist.db;

import android.provider.BaseColumns;

/**
 * Created on 2019/1/22.
 *
 * @author xuyingyi@bytedance.com (Yingyi Xu)
 */
public final class TodoContract {

    // TODO 定义表结构和 SQL 语句常量

    private TodoContract() {
    }

    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry2";
        public static final String COLUMN_NAME_TITLE_ID = "id";
        public static final String COLUMN_NAME_TITLE_DATA = "data";
        public static final String COLUMN_NAME_TITLE_STATE = "state";
        public static final String COLUME_NAME_TITLE_CONTENT = "content";
        public static final String COLUMN_NAME_TITLE_PRIORITY = "priority";
    }

    private static final String SQL_CREATE_ENTRIER = "CREATE TABLE " + FeedEntry.TABLE_NAME +
            "(" + FeedEntry._ID + " INTEGER PRIMARY KEY," +
                  FeedEntry.COLUMN_NAME_TITLE_ID + " LONG,"+
                  FeedEntry.COLUMN_NAME_TITLE_DATA + " INTEGER,"+
                  FeedEntry.COLUMN_NAME_TITLE_STATE + " INTEGER,"+
                  FeedEntry.COLUME_NAME_TITLE_CONTENT + " TEXT,"+
                  FeedEntry.COLUMN_NAME_TITLE_PRIORITY + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    public static String getSqlCreateEntrier() {
        return SQL_CREATE_ENTRIER;
    }

    public static String getSqlDeleteEntries() {
        return SQL_DELETE_ENTRIES;
    }
}
