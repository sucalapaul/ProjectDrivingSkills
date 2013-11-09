/**
 * 
 */
package com.dexter.drivingskills.android.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author dexter
 * Database shit
 * I so need a tool for this...
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_TRIPS = "trips";
	public static final String TRIP_ID = "_id";
	public static final String TRIP_START_LOCATION = "start_location";
	public static final String TRIP_END_LOCATION = "comment";
	public static final String TRIP_START_TIME = "start_time";
	public static final String TRIP_END_TIME = "end_time";
	public static final String TRIP_DISTANCE = "distance";
	public static final String TRIP_PRICE = "price";
	public static final String TRIP_BRAKE_TYPE = "brake_type";
	public static final String TRIP_SPEED_TYPE = "speed_type";
	public static final String TRIP_THROTTLE_TYPE = "throttle_type";
	public static final String TRIP_DRIVING_TYPE = "driving_type";
	public static final String TRIP_SCORE = "score";

	private static final String DATABASE_NAME = "driving_skills.db";
	private static final int DATABASE_VERSION = 2;
	
	private static MySQLiteHelper mInstance;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_TRIPS + "(" 
			+ TRIP_ID 				+ " integer primary key autoincrement, " 
			+ TRIP_START_LOCATION 	+ " text 	not null, "
			+ TRIP_END_LOCATION 	+ " text 	not null, "
			+ TRIP_START_TIME 		+ " integer not null, "
			+ TRIP_END_TIME 		+ " integer not null, "			
			+ TRIP_DISTANCE 		+ " real 	not null, "
			+ TRIP_PRICE 			+ " real 	not null, "
			+ TRIP_BRAKE_TYPE 		+ " integer	not null, "
			+ TRIP_SPEED_TYPE	 	+ " integer	not null, "
			+ TRIP_THROTTLE_TYPE 	+ " integer	not null, "
			+ TRIP_DRIVING_TYPE 	+ " integer	not null, "
			+ TRIP_SCORE		 	+ " real	not null);";

	
    private Context mCxt;

    public static MySQLiteHelper getInstance(Context ctx) {
        /** 
         * use the application context as suggested by CommonsWare.
         * this will ensure that you dont accidentally leak an Activitys
         * context (see this article for more information: 
         * http://developer.android.com/resources/articles/avoiding-memory-leaks.html)
         */
        if (mInstance == null) {
            mInstance = new MySQLiteHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }
    
	private MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIPS);
		onCreate(db);
	}

}
