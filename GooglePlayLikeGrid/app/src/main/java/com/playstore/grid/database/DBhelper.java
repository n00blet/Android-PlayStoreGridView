package com.playstore.grid.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


public class DBhelper {

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private final Context mCtx;
    private static final String DATABASE_NAME = "Test.db";
    private static final int DATABASE_VERSION = 1;
    private static final String GRID_TABLE = "albums";
    public static final String KEY_ID = "id"; //Auto-increment
    public static final String GRID_NAME = "name"; //Name of album created


    //Create Statement
    private static final String CREATE_GRID_TABLE = "create table " + GRID_TABLE + " ("
            + KEY_ID + " integer primary key autoincrement, "
            + GRID_NAME + " text not null unique);";


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_GRID_TABLE);

            Log.d("Database created", "--" + db.getPath());
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + GRID_TABLE);
            onCreate(db);
        }
    }

    public void insertGrid(String gridName) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GRID_NAME, gridName);
        Log.d("KEY_NAME ", "" + gridName);
        db.insert(GRID_TABLE, null, values);
        db.close();
    }

    public ArrayList<String> getGrid() {
        Log.d("List ", "Inside List");
        ArrayList<String> albumList = new ArrayList<String>();
        String select_query = "SELECT * FROM " + GRID_TABLE;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(select_query, null);
            if (cursor.moveToFirst()) {
                do {
                    albumList.add(cursor.getString(1));
                    Log.d("List cursor", "" + cursor.getString(1));
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        Log.d("List Size", "" + albumList.size());
        return albumList;
    }


    public void Reset() {
        mDbHelper.onUpgrade(this.mDb, 1, 1);
    }

    public DBhelper(Context ctx) {
        mCtx = ctx;
        mDbHelper = new DatabaseHelper(mCtx);
    }

    public DBhelper open() throws SQLException {
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

}

