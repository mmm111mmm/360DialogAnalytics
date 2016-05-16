package com.newfivefour.d360analyticslibrary.implementation;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.newfivefour.d360analyticslibrary.dbtables.FailureDatabaseHelper;
import com.newfivefour.d360analyticslibrary.exceptions.DatabaseFailureException;
import com.newfivefour.d360analyticslibrary.interfaces.RemoveAndRetrieveFailedSends;
import com.newfivefour.d360analyticslibrary.pojos.D360AnalyticsPayload;

import java.util.ArrayList;
import java.util.List;

/**
 * Adds things to our failures database
 * and removes and list them, for when something wants to retry them.
 */
public class AnalyticsFailureDatabase implements com.newfivefour.d360analyticslibrary.interfaces.FailureDatabase, RemoveAndRetrieveFailedSends {

  private final SQLiteDatabase writableDb;
  private static final String TAG = AnalyticsFailureDatabase.class.getSimpleName();

  public AnalyticsFailureDatabase(SQLiteDatabase dbHelper) {
    writableDb = dbHelper;
  }

  /**
   *
   * @param code the HTTP response, like 500 or something much more exciting and exotic.
   * @throws DatabaseFailureException
   */
  @Override
  public void add(int code, D360AnalyticsPayload payload) throws DatabaseFailureException {
    ContentValues cv = new ContentValues();
    cv.put("code", code);
    cv.put("payload", new Gson().toJson(payload));
    dbAdd(cv);
  }

  /**
   * Used when we want to retry all the failed payloads - removes them all from the failure database
   * @throws DatabaseFailureException
   */
  @Override public List<D360AnalyticsPayload> removeFailurePayloads() throws DatabaseFailureException {
    List<D360AnalyticsPayload> removedPayloads = new ArrayList<>();
    Cursor query = dbQuery();
    if(query.getCount()<=0) return removedPayloads;
    query.moveToFirst();
    boolean isNext = true;
    while(isNext) {
      int id = query.getInt(query.getColumnIndex("_id"));
      String payload = query.getString(query.getColumnIndex("payload"));
      D360AnalyticsPayload payloadObject;
      try {
        payloadObject = new Gson().fromJson(payload, D360AnalyticsPayload.class);
      } catch (Exception e) {
        Log.d(TAG, "Skipping. Couldn't not extract payload to D360AnalyticsPayload object: " + e);
        isNext = query.moveToNext();
        continue;
      }
      Log.d(TAG, "Retrieving a removing from failure db: "+payload);
      removedPayloads.add(payloadObject);
      dbDel(id);
      isNext = query.moveToNext();
    }
    return removedPayloads;
  }

  private long dbAdd(@NonNull ContentValues cv) {
    try {
      long insert = writableDb.insert(FailureDatabaseHelper.TABLENAME, "null", cv);
      Log.d(TAG, "Inserted a failure into the database: "+insert);
      return insert;
    } catch(Exception e) {
      Log.d(TAG, "Couldn't add to database");
      throw new DatabaseFailureException("Couldn't add to database", e);
    }
  }

  private long dbDel(@NonNull long id) {
    try {
      int delete = writableDb.delete(FailureDatabaseHelper.TABLENAME, "_id=" + id, null);
      Log.d(TAG, "Inserted a failure into the database: "+delete);
      return delete;
    } catch (Exception e) {
      Log.d(TAG, "Couldn't delete from database");
      throw new DatabaseFailureException("Couldn't query from database", e);
    }
  }

  private Cursor dbQuery() {
    try {
      Cursor cursor = writableDb.rawQuery("SELECT * FROM " + FailureDatabaseHelper.TABLENAME, null);
      if(cursor==null) {
        throw new DatabaseFailureException("Couldn't query database", new Exception());
      } else {
        return cursor;
      }
    } catch (Exception e) {
      Log.d(TAG, "Couldn't query database");
      throw new DatabaseFailureException("Couldn't query database", e);
    }
  }

}
