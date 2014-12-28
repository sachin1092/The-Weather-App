package com.sachinshinde.theweatherapp.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class LocationsProvider extends ContentProvider {

    // All URIs share these parts
    public static final String AUTHORITY = "com.sachinshinde.theweatherapp.provider";
    public static final String SCHEME = "content://";

    // URIs
    // Used for all locations
    public static final String LOCATIONS = SCHEME + AUTHORITY + "/locations";
    public static final Uri URI_LOCATIONS = Uri.parse(LOCATIONS);
    // Used for a single location, just add the id to the end
    public static final String LOCATION_BASE = LOCATIONS + "/";

    public LocationsProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor result = null;
        if (URI_LOCATIONS.equals(uri)) {
            result = LocationDBHandler
                    .getInstance(getContext())
                    .getReadableDatabase()
                    .query(Locations.TABLE_NAME, Locations.FIELDS, null, null, null,
                            null, null, null);
            result.setNotificationUri(getContext().getContentResolver(), URI_LOCATIONS);
        }
        else if (uri.toString().startsWith(LOCATION_BASE)) {
            final long id = Long.parseLong(uri.getLastPathSegment());
            result = LocationDBHandler
                    .getInstance(getContext())
                    .getReadableDatabase()
                    .query(Locations.TABLE_NAME, Locations.FIELDS,
                            Locations.KEY_ID + " IS ?",
                            new String[] { String.valueOf(id) }, null, null,
                            null, null);
            result.setNotificationUri(getContext().getContentResolver(), URI_LOCATIONS);
        }
        else {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        return result;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
