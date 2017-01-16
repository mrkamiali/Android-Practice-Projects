package com.example.kamranali.geofencefinale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamranali on 16/01/2017.
 */

public class DataBase extends SQLiteOpenHelper {

    private static final String TAG = "GEOFENCEDB";
    private static final String GEOFENCES_DATABASE = "GeoFencesDatabase";
    private static final String COL_ID = "_id";
    public static final int DB_VERSION = 1;
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longtitude";
    public static final String PLACE_NAME = "placeName";
    public static final String RADIUS = "radius";
    public static final String PUSH_KEY = "pushKey";

//    private static final String CREATE_TABLE = "create table "+GEOFENCES_DATABASE+" ( "+
//        COL_ID+" INTEGER primary key AUTOINCREMENT, "+
//        COLUMN_LATITUDE+" REAL, "+
//        COLUMN_LONGITUDE+" REAL, "+
//        RADIUS+" REAL, "+
//        PLACE_NAME+" TEXT, "+
//            PUSH_KEY+" TEXT )";

    public DataBase(Context context) {
        super(context, "GeoFencesDatabase.db", null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("create table %s (%s INTEGER primary key AUTOINCREMENT,%s TEXT,%s REAL,%s REAL,%s REAL,%s TEXT)"
                ,GEOFENCES_DATABASE,COL_ID,PUSH_KEY,COLUMN_LATITUDE,COLUMN_LONGITUDE,RADIUS,PLACE_NAME);

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void writeFences(Model model){
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LATITUDE,model.getLatitude());
        values.put(COLUMN_LONGITUDE,model.getLongitued());
        values.put(PLACE_NAME,model.getPlaceName());
        values.put(RADIUS,model.getRadius());
        values.put(PUSH_KEY,model.getPushkey());

        Log.d(TAG,model.toString());
        writableDatabase.insert(GEOFENCES_DATABASE,null,values);
        writableDatabase.close();
    }
    public List<Model> reterivingList(){
        List<Model> fencesList = new ArrayList<Model>();
        SQLiteDatabase readableDatabase = getReadableDatabase();

        String sql = String.format("select %s,%s,%s,%s,%s,%s from %s order by %s", COL_ID, COLUMN_LATITUDE, COLUMN_LONGITUDE, PLACE_NAME,RADIUS,PUSH_KEY, GEOFENCES_DATABASE, COL_ID);
        Cursor cursor = readableDatabase.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String latitude = cursor.getString(1);
            String longitude = cursor.getString(2);
            String placeName = cursor.getString(3);
            String radius = cursor.getString(4);
            String pushkey = cursor.getString(5);

            fencesList.add(new Model(Double.valueOf(longitude),Double.valueOf(latitude),Float.valueOf(radius),pushkey,placeName));
            Log.d(TAG," "+fencesList);
        }


        readableDatabase.close();

        return fencesList;
    }


}
