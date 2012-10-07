package fi.jyu.ties425.geotrack;

import com.google.android.maps.GeoPoint;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LocationDatabaseHandler extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "locationHistory.db";
	private static final String TABLE_NAME = "locations";
	private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (_id integer primary key autoincrement, _timestamp integer not null, _latitude real not null, _longitude real not null);";

	public LocationDatabaseHandler(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
	public void insertLocation(long time, double latitude, double longitude) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("_timestamp", time);
		values.put("_latitude", latitude);
		values.put("_longitude", longitude);
		db.insert(TABLE_NAME, null, values);
		db.close();
		Log.i("LocationDatabasehandler - insertLocation", "insert location " + time + ", " + latitude + ", " + longitude);
	}
	
	public void clearDatabase(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NAME, null, null);
		db.close();
		Log.i("LocationDatabaseHandler - clearDatabase", "database cleared");
	}
	
	public boolean isEmpty(){
		SQLiteDatabase db = this.getReadableDatabase();
		//db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit)
		Cursor cursor = db.query(TABLE_NAME, new String[] {"_timestamp"}, null, null, null, null, null);
		if (cursor.getCount() == 0) {
			return true;
		} else {
			return false;
		}
	}
	
			//GeoPoint
	public double[] GetTopEntries(){
		
		SQLiteDatabase db = this.getReadableDatabase();
		double[] topEntries=null;
		//GeoPoint points[]=null;
		int apu1, apu2;
		Cursor cursor = db.query(TABLE_NAME, new String[] {"_latitude","_longitude"},null, null, null, null, "_timestamp DESC", "50");
		if(cursor !=null){
			//int numberOfEntries = cursor.getCount();
			if (cursor.getCount() != 0) {
				cursor.moveToFirst();
			//	points = new GeoPoint[cursor.getCount()*2];
				topEntries = new double [cursor.getCount()*2];
				for (int i=0; i<cursor.getCount()*2;i +=2){
			//		apu1 = (int) (Integer.parseInt(cursor.getString(0))* 1E6); Throws a number format exception 
			//		apu2 = (int) (Integer.parseInt(cursor.getString(1))* 1E6);
			//		points[i]= new GeoPoint (apu1,apu2);
					topEntries[i] = Double.parseDouble(cursor.getString(0))* 1e6;
					topEntries[i+1]= Double.parseDouble(cursor.getString(1))*1e6;
					cursor.moveToNext();
				}
			}
			
		}
		Log.i("LocationDatabasehandler - getTopEntries", "Entries: "+ topEntries.length + "Latitude: "+ topEntries[0] + " Longitude: " + topEntries[1]);
		db.close();
		//return topEntries;
		return topEntries;
		
		
	}
	
//	public GeoPoint[] 
//	public String[] getTopThree(){
//		SQLiteDatabase db = this.getReadableDatabase();
//		String[] topThree;
//		//db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit)
//		Cursor cursor = db.query(TABLE_NAME, new String[] {"_name","_highscore"}, null, null, null, null, "_highscore DESC, _name ASC", "3");
//		if (cursor != null){
//			if (cursor.getCount() != 0){
//				cursor.moveToFirst();
//				topThree = new String[cursor.getCount()*2];
//				for (int i = 0; i < cursor.getCount() * 2; i+=2){
//					topThree[i] = cursor.getString(0);
//					topThree[i+1] = Integer.toString(cursor.getInt(1));
//					cursor.moveToNext();
//				}
//			} else {
//				topThree = new String[]{"No scores available."};
//			}
//			
//		} else {
//			topThree = new String[]{"ERROR"};
//		}
//		db.close();	
//		return topThree;
//	}
		
		
	
}	
	

