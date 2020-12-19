package com.example.newone.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context) {
        super(context, "Login.db", null,1);
    }

    // creatin user table in DB
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE USER(username text primary key,password text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user");
    }


    //         inserting in database

    public boolean insert(String username,String password){
        // write to the database- insert to the database
        SQLiteDatabase db= this.getWritableDatabase();
        // for inser values writeable
        ContentValues contentValues=new ContentValues();
        contentValues.put("username",username);
        contentValues.put("password",password);
        long ins=db.insert("user",null,contentValues);
        //
        if(ins==1) return false;
        else return true;
    }

    //      check if username is exists
    public Boolean checkUsername(String username){
        // link with database - sqlite
        // check-read-
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cursor=db.rawQuery("SELECT * FROM user WHERE USERNAME=?",new String[]{username});
        if(cursor.getCount()>0) return false;
        else return true;
    }

    //     check the email and password

    public boolean emailPassword(String username,String password){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from user where username=? and password=?",new String[]{username,password});
        if(cursor.getCount()>0) return true;
        else return false;
    }
}
