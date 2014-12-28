package com.sachinshinde.theweatherapp.db;

/**
 * Created by sachin on 2/11/14.
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Locations {

    // Database table
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "city_name";
    public static final String KEY_IS_MY_LOC = "is_my_loc";
    public static final String KEY_GMT = "gmt";
    public static final String KEY_LAT = "lat";
    public static final String KEY_LON = "lon";
    public static final String TABLE_NAME = "Locations";

    // For database projection so order is consistent
    public static final String[] FIELDS = {KEY_ID, KEY_NAME, KEY_IS_MY_LOC, KEY_GMT, KEY_LAT, KEY_LON};

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NAME
            + " TEXT NOT NULL DEFAULT '', " + KEY_IS_MY_LOC + " TEXT NOT NULL DEFAULT '', " + KEY_GMT
            + " TEXT NOT NULL DEFAULT '', " + KEY_LAT + " TEXT NOT NULL DEFAULT '', "
            + KEY_LON + " TEXT NOT NULL DEFAULT '');";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(Locations.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

    // Fields corresponding to database columns
    public long id = -1;
    public String city_name = "";
    public String is_my_loc = "";
    public String gmt = "";
    public String lat = "";
    public String lon = "";

    /**
     * No need to do anything, fields are already set to default values above
     */
    public Locations() {
    }

    public Locations(String city_name, String is_my_loc, String gmt, String lat, String lon, long id) {
        this.id = id;
        this.city_name = city_name;
        this.is_my_loc = is_my_loc;
        this.gmt = gmt;
        this.lat = lat;
        this.lon = lon;
    }

    /**
     * Convert information from the database into a Person object.
     */
    public Locations(final Cursor cursor) {
        // Indices expected to match order in FIELDS!
        this.id = cursor.getLong(0);
        this.city_name = cursor.getString(1);
        this.is_my_loc = cursor.getString(2);
        this.gmt = cursor.getString(3);
        this.lat = cursor.getString(4);
        this.lon = cursor.getString(5);
    }

    /**
     * Return the fields in a ContentValues object, suitable for insertion
     * into the database.
     */
    public ContentValues getContent() {
        final ContentValues values = new ContentValues();
        // Note that ID is NOT included here
        values.put(KEY_NAME, city_name);
        values.put(KEY_IS_MY_LOC, is_my_loc);
        values.put(KEY_GMT, gmt);
        values.put(KEY_LAT, lat);
        values.put(KEY_LON, lon);

        return values;
    }

    public String toString(){
        return "Location: { " +
                "'id': " + id + ", " +
                "'name': " + city_name + ", " +
                "'is_my_loc': " + is_my_loc + ", " +
                "'gmt': " + gmt + ", " +
                "'lat': " + lat + ", " +
                "'lon':" + lon + " }";
    }

}