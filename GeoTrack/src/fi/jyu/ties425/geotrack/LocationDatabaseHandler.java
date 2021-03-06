package fi.jyu.ties425.geotrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.maps.GeoPoint;

/**
 * class to store the different locations in a db
 * 
 * @author Philipp Kyas and Jouni Laitinen
 * @version 1.0
 */
public class LocationDatabaseHandler extends SQLiteOpenHelper {
	/*
	 * definition of the necessary constants
	 */
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "locationHistory.db";
	private static final String TABLE_NAME = "locations";
	private static final String TABLE_CREATE = "CREATE TABLE "
			+ TABLE_NAME
			+ " (_id integer primary key autoincrement, _timestamp text not null, _latitude integer not null, _longitude integer not null);";

	/*
	 * create db when class instance is created
	 */
	public LocationDatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/*
	 * create the above defined table
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
	}

	/*
	 * define the actions to be done at an update, here nothing
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
	 * .SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	/*
	 * insert a new location into the db
	 */
	public void insertLocation(String time, double latitude, double longitude) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("_timestamp", time);
		values.put("_latitude", latitude * 1e6);
		values.put("_longitude", longitude * 1e6);
		db.insert(TABLE_NAME, null, values);
		db.close();
	}

	/*
	 * clear the database - no location entities remain
	 */
	public void clearDatabase() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NAME, null, null);
		db.close();
		Log.i("LocationDatabaseHandler - clearDatabase", "database cleared");
	}

	/*
	 * check if the db is empty
	 */
	public boolean isEmpty() {
		SQLiteDatabase db = this.getReadableDatabase();
		// db.query(table, columns, selection, selectionArgs, groupBy, having,
		// orderBy, limit)
		Cursor cursor = db.query(TABLE_NAME, new String[] { "_timestamp" },
				null, null, null, null, null);
		if (cursor.getCount() == 0) {
			db.close();
			return true;
		} else {
			db.close();
			return false;
		}
	}

	/*
	 * get the 50 latest locations
	 */
	public GeoPoint[] getTop50Locations() {

		SQLiteDatabase db = this.getReadableDatabase();
		GeoPoint locations[] = null;
		int numberOfLocations;

		Cursor cursor = db
				.query(TABLE_NAME, new String[] { "_latitude", "_longitude" },
						null, null, null, null, "_timestamp DESC", "50");

		if (cursor != null) {
			numberOfLocations = cursor.getCount();
			if (numberOfLocations != 0) {
				cursor.moveToFirst();
				locations = new GeoPoint[numberOfLocations];

				for (int i = 0; i < numberOfLocations; i++) {
					locations[i] = new GeoPoint(cursor.getInt(0),
							cursor.getInt(1));
					cursor.moveToNext();
				}
			}
		}

		db.close();
		return locations;
	}

	/*
	 * get the 50 latest timestamps
	 */
	public String[] getTop50Timestamps() {

		SQLiteDatabase db = this.getReadableDatabase();
		String timestamps[] = null;
		int numberOfTimestamps;

		Cursor cursor = db.query(TABLE_NAME, new String[] { "_timestamp" },
				null, null, null, null, "_timestamp DESC", "50");

		if (cursor != null) {
			numberOfTimestamps = cursor.getCount();
			if (numberOfTimestamps != 0) {
				cursor.moveToFirst();
				timestamps = new String[numberOfTimestamps];

				for (int i = 0; i < numberOfTimestamps; i++) {
					timestamps[i] = cursor.getString(0);
					cursor.moveToNext();
				}
			}
		}

		db.close();
		return timestamps;
	}
}
