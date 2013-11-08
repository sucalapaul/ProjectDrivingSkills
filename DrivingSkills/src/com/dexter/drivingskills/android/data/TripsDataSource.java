/**
 * 
 */
package com.dexter.drivingskills.android.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author dexter
 * Class for adding and fetching Trips to/from database
 */
public class TripsDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { 
			MySQLiteHelper.TRIP_ID,
			MySQLiteHelper.TRIP_START_LOCATION,
			MySQLiteHelper.TRIP_END_LOCATION,
			MySQLiteHelper.TRIP_START_TIME,
			MySQLiteHelper.TRIP_END_TIME,
			MySQLiteHelper.TRIP_DISTANCE,
			MySQLiteHelper.TRIP_PRICE,
			MySQLiteHelper.TRIP_BRAKE_TYPE,
			MySQLiteHelper.TRIP_SPEED_TYPE,
			MySQLiteHelper.TRIP_THROTTLE_TYPE,
			MySQLiteHelper.TRIP_DRIVING_TYPE,
	};


	public TripsDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Trip createTrip(
			 String 	startLocation,
			 String		endLocation,
			 long		startTime,
			 long		endTime,
			 double		distance,
			 double 	price,
			 int		brakeType,
			 int 		speedType,
			 int		throttleType,
			 int		drivingType) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.TRIP_START_LOCATION, startLocation);
		values.put(MySQLiteHelper.TRIP_END_LOCATION, endLocation);
		values.put(MySQLiteHelper.TRIP_START_TIME, startTime);
		values.put(MySQLiteHelper.TRIP_END_TIME, endTime);
		values.put(MySQLiteHelper.TRIP_DISTANCE, distance);
		values.put(MySQLiteHelper.TRIP_PRICE, price);
		values.put(MySQLiteHelper.TRIP_BRAKE_TYPE, brakeType);
		values.put(MySQLiteHelper.TRIP_SPEED_TYPE, speedType);
		values.put(MySQLiteHelper.TRIP_THROTTLE_TYPE, throttleType);
		values.put(MySQLiteHelper.TRIP_DRIVING_TYPE, drivingType);

		long insertId = database.insert(MySQLiteHelper.TABLE_TRIPS, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_TRIPS,
				allColumns, MySQLiteHelper.TRIP_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		Trip newTrip = cursorToTrip(cursor);
		cursor.close();
		return newTrip;
	}

	public void deleteTrip(Trip trip) {
		long id = trip.getId();
		database.delete(MySQLiteHelper.TABLE_TRIPS, MySQLiteHelper.TRIP_ID
				+ " = " + id, null);
	}

	public List<Trip> getAllTrips() {
		List<Trip> trips = new ArrayList<Trip>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_TRIPS,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Trip trip = cursorToTrip(cursor);
			trips.add(trip);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return trips;
	}

	private Trip cursorToTrip(Cursor c) {
		Trip trip = new Trip();
		trip.setId			(c.getLong		( c.getColumnIndex( MySQLiteHelper.TRIP_ID )));
		trip.setStartLocation(c.getString	( c.getColumnIndex( MySQLiteHelper.TRIP_START_LOCATION )));
		trip.setEndLocation	(c.getString	( c.getColumnIndex( MySQLiteHelper.TRIP_END_LOCATION )));
		trip.setStartTime	(c.getLong		( c.getColumnIndex( MySQLiteHelper.TRIP_START_TIME )));
		trip.setEndTime		(c.getLong		( c.getColumnIndex( MySQLiteHelper.TRIP_END_TIME )));
		trip.setDistance	(c.getDouble	( c.getColumnIndex( MySQLiteHelper.TRIP_DISTANCE )));
		trip.setPrice		(c.getDouble	( c.getColumnIndex( MySQLiteHelper.TRIP_PRICE )));
		trip.setBrakeType	(c.getInt		( c.getColumnIndex( MySQLiteHelper.TRIP_BRAKE_TYPE )));
		trip.setSpeedType	(c.getInt		( c.getColumnIndex( MySQLiteHelper.TRIP_SPEED_TYPE )));
		trip.setThrottleType(c.getInt		( c.getColumnIndex( MySQLiteHelper.TRIP_THROTTLE_TYPE )));
		trip.setDrivingType	(c.getInt		( c.getColumnIndex( MySQLiteHelper.TRIP_DRIVING_TYPE )));

		return trip;
	}

}
