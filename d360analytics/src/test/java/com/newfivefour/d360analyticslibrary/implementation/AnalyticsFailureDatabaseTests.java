package com.newfivefour.d360analyticslibrary.implementation;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.newfivefour.d360analyticslibrary.exceptions.DatabaseFailureException;
import com.newfivefour.d360analyticslibrary.pojos.D360AnalyticsPayload;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AnalyticsFailureDatabaseTests {

  AnalyticsFailureDatabase db;
  @Mock SQLiteDatabase writableDb;
  @Mock Cursor cursor;
  D360AnalyticsPayload pl = new D360AnalyticsPayload("hi", null);

  @Before
  public void before() {
    db = new AnalyticsFailureDatabase(
            writableDb
    );

  }

  @Test
  public void run_doWeGetToInsertStatement() throws Exception {
    db.add(500, pl);
    verify(writableDb, times(1)).insert(any(String.class), any(String.class), any(ContentValues.class));
  }

  @Test
  public void run_doThrowExceptionOnDodgyDbAdd() throws Exception {
    when(writableDb.insert(any(String.class), any(String.class), any(ContentValues.class)))
            .thenThrow(new DatabaseFailureException("Hiya", new Exception()));
    try {
      db.add(500, pl);
    } catch(DatabaseFailureException e){
      assertThat("Found exception", true);
      return;
    }
    assertThat("Wanted exception", false);
  }

  @Test
  public void run_doThrowExceptionOnDodgyDbQuery() throws Exception {
    when(writableDb.rawQuery(any(String.class), any(String[].class)))
            .thenThrow(new DatabaseFailureException("Hiya", new Exception()));
    try {
      db.removeFailurePayloads();
    } catch(DatabaseFailureException e){
      assertThat("Found exception", true);
      return;
    }
    assertThat("Wanted exception", false);
  }

  @Test
  public void run_doThrowExceptionOnDodgyDbDelete() throws Exception {
    when(writableDb.delete(any(String.class), any(String.class), any(String[].class)))
            .thenThrow(new DatabaseFailureException("Hiya", new Exception()));
    try {
      db.removeFailurePayloads();
    } catch(DatabaseFailureException e){
      assertThat("Found exception", true);
      return;
    }
    assertThat("Wanted exception", false);
  }

  @Test
  public void run_doWeGetToQueryStatement() throws Exception {
    when(writableDb.rawQuery(any(String.class), any(String[].class))).thenReturn(cursor);
    db.removeFailurePayloads();
    verify(writableDb, times(1)).rawQuery(any(String.class), any(String[].class));
  }

  @Test
  public void run_doWeDeleteAFailure() throws Exception {
    when(writableDb.rawQuery(any(String.class), any(String[].class))).thenReturn(cursor);
    when(cursor.getCount()).thenReturn(1);
    db.removeFailurePayloads();
    verify(writableDb, times(1)).delete(any(String.class), any(String.class), any(String[].class));

  }
}