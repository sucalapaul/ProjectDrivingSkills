/**
 * 
 */
package com.dexter.drivingskills.android.data;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * @author dexter
 * This class represent the objects saved in Trips table
 */
public class Trip {
	 
	private long 		id;	
	private String 	startLocation;
	private String		endLocation;
	private long		startTime;
	private long		endTime;
	private double		distance;
	private double 	price;
	private int		brakeType;
	private int 		speedType;
	private int		throttleType;
	private int		drivingType;
	
	//TODO: put these in a database or XML
	public static final String[] DRIVE = {
		"Aggressive",
		"Anxious",
		"Economical",
		"Keen",
		"Sedate"
	};
	
	public static final String[] BRAKE = {
		"Normal\nbraking",
		"Medium\nbraking",
		"Hard\nbraking"
	};
	
	public static final String[] THROTTLE = {
		"Normal\nthrottle",
		"Medium\nthrottle",
		"Heavy\nfoot"
	};	
	
	public static final String[] SPEED = {
		"Under\nspeed",
		"Normal\nspeed",
		"Over\nspeed"
	};

	public static final int DRIVE_AGGRESSIVE 	= 0;
	public static final int DRIVE_ANXIOUS 	= 1;
	public static final int DRIVE_ECONOMICAL	= 2;
	public static final int DRIVE_KEEN 		= 3;
	public static final int DRIVE_SEDATE 		= 4;

	public static final int BRAKE_EASY	= 0;
	public static final int BRAKE_MEDIUM	= 1;
	public static final int BRAKE_HARD	= 2;

	public static final int THROTTLE_EASY		= 0;
	public static final int THROTTLE_MEDIUM	= 1;
	public static final int THROTTLE_HARD		= 2;
	
	public static final int SPEED_UNDER	= 0;
	public static final int SPEED_NORMAL	= 1;
	public static final int SPEED_OVER	= 2;
	
	
	

	/**
	 * constructor
	 */
	public Trip() {
		
	}
	
	
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}


	/**
	 * @return the startLocation
	 */
	public String getStartLocation() {
		return startLocation;
	}


	/**
	 * @param startLocation the startLocation to set
	 */
	public void setStartLocation(String startLocation) {
		this.startLocation = startLocation;
	}


	/**
	 * @return the startTime
	 */
	public long getStartTime() {
		return startTime;
	}


	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}


	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}


	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}


	/**
	 * @return the speedType
	 */
	public int getSpeedType() {
		return speedType;
	}


	/**
	 * @param speedType the speedType to set
	 */
	public void setSpeedType(int speedType) {
		this.speedType = speedType;
	}


	/**
	 * @return the throttleType
	 */
	public int getThrottleType() {
		return throttleType;
	}


	/**
	 * @param throttleType the throttleType to set
	 */
	public void setThrottleType(int throttleType) {
		this.throttleType = throttleType;
	}



	/**
	 * @return the endLocation
	 */
	public String getEndLocation() {
		return endLocation;
	}



	/**
	 * @param endLocation the endLocation to set
	 */
	public void setEndLocation(String endLocation) {
		this.endLocation = endLocation;
	}



	/**
	 * @return the endTime
	 */
	public long getEndTime() {
		return endTime;
	}



	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}



	/**
	 * @return the distance
	 */
	public double getDistance() {
		return distance;
	}



	/**
	 * @param distance the distance to set
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}



	/**
	 * @return the brakeType
	 */
	public int getBrakeType() {
		return brakeType;
	}



	/**
	 * @param brakeType the brakeType to set
	 */
	public void setBrakeType(int brakeType) {
		this.brakeType = brakeType;
	}



	/**
	 * @return the drivingType
	 */
	public int getDrivingType() {
		return drivingType;
	}



	/**
	 * @param drivingType the drivingType to set
	 */
	public void setDrivingType(int drivingType) {
		this.drivingType = drivingType;
	}



	public CharSequence getEndDateTimeString() {
		Date date = new Date( endTime *1000L );
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String formattedDate = sdf.format(date);
		
		return formattedDate;
	}



	public CharSequence getEndDateString() {
		Date date = new Date( endTime *1000L );
		SimpleDateFormat sdf = new SimpleDateFormat("E");
		String formattedDate = sdf.format(date);
		
		return formattedDate;
	}



	public CharSequence getDistanceString() {
		return String.format("%.1f", distance);
	}



	public CharSequence getPriceString() {
		return String.format("%.1f", price);
	}



	public CharSequence getDrivingTypeString() {
		return DRIVE[drivingType] + " driving";
	}



	public CharSequence getBrakeTypeString() {
		return BRAKE[brakeType];
	}



	public CharSequence getSpeedTypeString() {
		return SPEED[speedType];
	}



	public CharSequence getThrottleTypeString() {
		return THROTTLE[throttleType];
	}
	


}
