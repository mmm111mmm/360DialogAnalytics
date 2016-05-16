package com.newfivefour.d360analyticslibrary.dbtables;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Creates the tables where we stores the payload failures
 */
public class FailureDatabaseHelper extends SQLiteOpenHelper {
    public static String TABLENAME = "thingsthatdidnotgowell";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE "+TABLENAME+" (" +
                    "_id INTEGER PRIMARY KEY NOT NULL," +
                    "code INTEGER NOT NULL,"+
                    "payload text NOT NULL)";
    public FailureDatabaseHelper(Context context) {
      super(context, "360d", null, 1);
    }
    @Override public void onCreate(SQLiteDatabase sqLiteDatabase) {
      sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }
    @Override public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}
  }