package com.example;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Storage extends SQLiteOpenHelper {

    public static final int SQLITE_DATABASE_VERSION = 1;

    private static Storage instance;

    public static Storage getInstance(Context ctx) {
        if (instance == null) {
            instance = new Storage(ctx);
        }
        return instance;
    }

    private Storage(Context context) {
        super(context, "Counter", null, SQLITE_DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table if not exists count (_id integer primary key, count integer)");
        ContentValues contentValues = new ContentValues();
        contentValues.put("count", 0);
        contentValues.put("_id", 0);
        sqLiteDatabase.insert("count", null, contentValues);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists count");
        onCreate(sqLiteDatabase);
    }

    public void increment() {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", 0);
        contentValues.put("count", getCount() + 1);
        db.update("count", contentValues, null, null);
    }

    public void decrement() {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", 0);
        contentValues.put("count", getCount() - 1);
        db.update("count", contentValues, null, null);
    }

    public void reset() {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", 0);
        contentValues.put("count", 0);
        db.update("count", contentValues, null, null);
    }

    public int getCount() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query("count", new String[]{"count"}, "_id = ?",
                new String[]{"0"}, null, null, null);
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex("count"));
    }
}