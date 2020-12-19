package com.example.newone.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.newone.Model.Place;
import com.example.newone.Model.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {


    public DatabaseHandler(@Nullable Context context) {
        super(context,Utils.DATABASE_NAME, null, Utils.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        //------------Create table in DB

        String CREATE_PLACES_TABLE="CREATE TABLE " +Utils.TABLE_NAME+" ("+
                Utils.KEY_ID+" INTEGER PRIMARY KEY, "+
                Utils.LATITUDE+" TEXT, "+
                Utils.LONGITUDE+" TEXT, "+
                Utils.TITLE+" TEXT )";
        db.execSQL(CREATE_PLACES_TABLE);

        String CREATE_USERS_TABLE="CREATE TABLE " +Utils.TABLE_NAME1+" ("+
                Utils.KEY_ID1+" INTEGER PRIMARY KEY, "+
                Utils.USERNAME+" TEXT, "+
                Utils.PASSWORD+" TEXT )";
        db.execSQL(CREATE_USERS_TABLE);
    }

    // on upgrade table for DB
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+Utils.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+Utils.TABLE_NAME1);
        onCreate(db);
    }

    // add new place to DB
    public void addPlace(Place place){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(Utils.LATITUDE,place.getPlatitude());
        contentValues.put(Utils.LONGITUDE,place.getpLongitude());
        contentValues.put(Utils.TITLE,place.getTitle());

        database.insert(Utils.TABLE_NAME,null,contentValues);
        database.close();

    }
    //add new user to DB
    public void addUser(User user){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(Utils.KEY_ID1,user.getId());
        contentValues.put(Utils.USERNAME,user.getUsername());
        contentValues.put(Utils.PASSWORD,user.getPassword());

        database.insert(Utils.TABLE_NAME1,null,contentValues);
        database.close();

    }
    // get all places from DB
    public List<Place> getAllPlaces(){
        SQLiteDatabase database=this.getReadableDatabase();
        List<Place> placeList=new ArrayList<>();
        String getAll="SELECT * FROM "+Utils.TABLE_NAME;
        Cursor cursor=database.rawQuery(getAll,null);
        if(cursor.moveToFirst()){
            do{
                Place place=new Place();
                place.setId(cursor.getInt(0));
                place.setPlatitude(cursor.getString(1));
                place.setpLongitude(cursor.getString(2));
                place.setTitle(cursor.getString(3));

                placeList.add(place);
            }while(cursor.moveToNext());
        }

        return placeList;

    }

    // get place number from DB
    public int getPlacesNum(){
        SQLiteDatabase database=this.getReadableDatabase();
        String getAll="SELECT * FROM "+Utils.TABLE_NAME;
        Cursor cursor =database.rawQuery(getAll,null);
        return cursor.getCount();
    }

    //delete place from DB
    public void deletePlace(Place place){
        SQLiteDatabase database=this.getWritableDatabase();
        database.delete(Utils.TABLE_NAME,Utils.TITLE+" =? ",
                new String[]{String.valueOf(place.getTitle())});
        database.close();

    }

    //edit place from DB
    public int editPlace(Place place){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(Utils.LATITUDE,place.getPlatitude());
        contentValues.put(Utils.LONGITUDE,place.getpLongitude());
        contentValues.put(Utils.TITLE,place.getTitle());

        int result= database.update(Utils.TABLE_NAME,contentValues,Utils.TITLE+" =? ",
                new String[]{String.valueOf(place.getTitle())});
        database.close();
        return result;

    }
}
