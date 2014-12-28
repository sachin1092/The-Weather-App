package com.sachinshinde.theweatherapp.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by sachin on 28/12/14.
 */
public class LocationDBHandler extends SQLiteOpenHelper {

    private static LocationDBHandler singleton;

    public static LocationDBHandler getInstance(final Context context) {
        if (singleton == null) {
            singleton = new LocationDBHandler(context);
        }
        return singleton;
    }

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "locationsTable.db";

    private final Context context;

    public LocationDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // Good idea to use process context here
        this.context = context.getApplicationContext();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Locations.onCreate(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        Locations.onUpgrade(sqLiteDatabase, i, i2);
    }

    public synchronized Locations getLocation(final long id) {
        final SQLiteDatabase db = this.getReadableDatabase();
        final Cursor cursor = db.query(Locations.TABLE_NAME,
                Locations.FIELDS, Locations.KEY_ID + " IS ?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor == null || cursor.isAfterLast()) {
            return null;
        }

        Locations item = null;
        if (cursor.moveToFirst()) {
            item = new Locations(cursor);
        }
        cursor.close();

        return item;
    }

    public synchronized Locations getLocation(final String name) {
        final SQLiteDatabase db = this.getReadableDatabase();
        final Cursor cursor = db.query(Locations.TABLE_NAME,
                Locations.FIELDS, Locations.KEY_NAME + " IS ?",
                new String[]{String.valueOf(name)}, null, null, null, null);
        if (cursor == null || cursor.isAfterLast()) {
            return null;
        }

        Locations item = null;
        if (cursor.moveToFirst()) {
            item = new Locations(cursor);
        }
        cursor.close();

        return item;
    }


    public synchronized boolean putLocation(final Locations locations) {
        boolean success = false;
        int result = 0;
        final SQLiteDatabase db = this.getWritableDatabase();

        if (locations.is_my_loc.equals("1")) {
            Log.d("TheWeatherApp", "Checking if my location exists");
            int del_result = db.delete(Locations.TABLE_NAME,
                    Locations.KEY_IS_MY_LOC + " IS ?",
                    new String[]{"1"});
            Log.d("TheWeatherApp", "Result is " + del_result);
        }

        result += db.update(Locations.TABLE_NAME, locations.getContent(),
                Locations.KEY_NAME + " IS ?",
                new String[]{String.valueOf(locations.city_name)});
        Log.d("TheWeatherApp", "trying to find and update location by name" + locations + " result " + result);

        if (result <= 0)
            if (locations.id > -1) {
                result = 0;
                result += db.update(Locations.TABLE_NAME, locations.getContent(),
                        Locations.KEY_ID + " IS ?",
                        new String[]{String.valueOf(locations.id)});
                Log.d("TheWeatherApp", "location exists trying to update" + locations);
            } else {
                Log.d("TheWeatherApp", "location doesn't exists" + locations);
            }

        if (result > 0) {
            Log.d("TheWeatherApp", "update successful" + locations);
            success = true;
        } else {
            Log.d("TheWeatherApp", "result is -1 so will insert a new row" + locations);
            // Update failed or wasn't possible, insert instead
            final long id = db.insert(Locations.TABLE_NAME, null,
                    locations.getContent());

            if (id > -1) {
                locations.id = id;
                success = true;
            }
        }

        if (success) {
            notifyProviderOnPersonChange();
        }

        return success;
    }

    public synchronized int removeLocation(final Locations locations) {

        Log.d("TheWeatherApp", "Trying to delete " + locations);

        final SQLiteDatabase db = this.getWritableDatabase();
        final int result = db.delete(Locations.TABLE_NAME,
                Locations.KEY_ID + " IS ?",
                new String[]{Long.toString(locations.id)});

        if (result > 0) {
            Log.d("TheWeatherApp", "delete successful");
            notifyProviderOnPersonChange();
        }

        return result;
    }

    private void notifyProviderOnPersonChange() {
        context.getContentResolver().notifyChange(
                LocationsProvider.URI_LOCATIONS, null, false);
    }
}
