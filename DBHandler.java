package com.firstapp.speech_to_text1;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "vmdb1";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "vm";
    private static final String ID_COL = "id";
    private static final String USERNAME_COL = "gmail";
    private static final String PWD_COL = "password";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USERNAME_COL + " TEXT,"
                + PWD_COL + " TEXT)";
        db.execSQL(query);
    }
    public void adduser(String username, String pwd) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USERNAME_COL, username);
        values.put(PWD_COL, pwd);
        db.insert(TABLE_NAME, null, values);
    }
    public String isuserpresent(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        if(cursor.getCount()==0)
            return "0";
        return "1";
    }
    @SuppressLint("Range")
    public String getusername(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex("gmail"));
    }
    @SuppressLint("Range")
    public String getpwd(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex("password"));
    }
    public void removeuser(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
