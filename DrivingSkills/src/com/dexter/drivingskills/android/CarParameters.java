package com.dexter.drivingskills.android;

import com.dexter.drivingskills.android.data.Trip;

import android.R.bool;
import android.widget.TableRow;

/**
 * This class contains data about car, obtained through OBD terminal (speed, rpm)
 * or computed (acceleration)
 * @author dexter
 */
public class CarParameters implements Cloneable {
	
	//Member Fields
	private double speed;
	private double rpm;
	private double throttle;
	private double acceleration;
	private int 	 gear;
	private boolean isRecording;
	private double distance;
	
	private long speed_updated_at;
	private long rpm_updated_at;
	private long throttle_updated_at;
	private long created_at;
	
	private double raport;
	private double score;
	
	private int connectionStatus;
	private int retryCount;
	
	private String status = "";
	
	private Trip trip;

	/**
	 * Constructor. Initializes data.
	 */
	public CarParameters() {
		speed 	 = 0;
		rpm		 = 0;
		throttle = 0;
		score 	 = 10;
		acceleration = 0;
		gear 	 	 = 0;
		distance 	 = 0;
		isRecording  = false;
		created_at 	 = System.currentTimeMillis() / 1000;
		trip = new Trip();
		connectionStatus = 0;
		retryCount = 0;
	}

	/**
	 * Derivate speed and obtains acceleration
	 * @param previousUpdateTime UNIX*1000
	 * @param previousSpeed km/h
	 */
	private void updateAcceleration(long previousUpdateTime, double previousSpeed) {
		if (previousUpdateTime == 0 || speed == -1 || previousSpeed == -1){
			this.acceleration = 0;
			return;
		}
		
		double deltaSpeedMS = (this.speed - previousSpeed);
		deltaSpeedMS = deltaSpeedMS / 3.6;
		double deltaTime 	 = (this.speed_updated_at - previousUpdateTime);
		deltaTime = deltaTime / 1000;
		
		double d = (previousSpeed + speed) / 2;
		d = (d / 3.6) * deltaTime;
		this.distance += d;
		this.acceleration = (deltaSpeedMS / deltaTime);
		
		status = "";
		if (speed > 15) {
			if (acceleration > 0.5 && acceleration < 1.5) {
				status = "Normal Acceleration";
			}
			if (acceleration >= 1.5 && acceleration < 2.2) {
				status = "Hard Acceleration";
				//TODO: Too heuristic
				if (trip.getThrottleType() == trip.THROTTLE_EASY)
					trip.setThrottleType(Trip.THROTTLE_MEDIUM);
			}
			if (acceleration >= 2.2 ) {
				status = "Heavy foot";
				trip.setThrottleType(Trip.THROTTLE_HARD);
			}		
			if (acceleration < -1.8 && acceleration > -2.8) {
				status = "Normal Braking";
				if (trip.getBrakeType() == Trip.BRAKE_EASY)
					trip.setBrakeType(Trip.BRAKE_MEDIUM);
			}		
			if (acceleration < -2.8) {
				status = "Hard Braking";
				trip.setBrakeType(Trip.BRAKE_HARD);
			}	
		}
	}
	

	private void updateGear() {
		// TODO Compute ratio Speed/rpm
		// if RPM < 1500 -> N
		raport = (speed / rpm) * 100;
		int g = 0;
		if (rpm > 1000)
		{
			if (raport > 0.45 && raport < 0.8){
				g = 1;
			}
			if (raport > 1.1 && raport < 1.4) {
				g = 2;
			}
			if (raport > 1.5 && raport < 1.9) {
				g = 3;
			}
			if (raport > 2.2 && raport < 2.8) {
				g = 4;
			}
			if (raport > 3 && raport < 4) {
				g = 5;
			}
		}
		gear = g;
		
		
	}
	
	//TODO Add synchronized to all getters and setters ??
	
	public double getSpeed() {
		return speed;
	}
	
	public String getStringSpeed() {
		return Integer.toString( (int) speed );
	}

	public void setSpeed(double newSpeed) {
		double prevSpeed = this.speed;
		long prevUpdate = speed_updated_at;
		
		speed_updated_at = System.currentTimeMillis();
		this.speed = newSpeed;
		
		updateAcceleration(prevUpdate, prevSpeed);
		updateGear();
		
		if (speed > 55) {
			trip.setSpeedType(Trip.SPEED_OVER);
		}
		
		
		if (speed != -1) {
			isRecording = true;
			retryCount = 0;
		} else {
			retryCount++;
		}
	}
	
	public void setSpeed(String speed) {
		setSpeed(Double.parseDouble(speed));
	}

	public double getRpm() {
		return rpm;
	}
	
	public String getStringRpm() {
		if (isEngineON()){
			return Integer.toString( (int) rpm );
		}
		return "OFF";
	}
	
	public boolean isEngineON() {
		if (rpm > 350) 
			return true;
		return false;
	}

	public void setRpm(double rpm) {
		this.rpm = rpm;
		updateGear();
	}

	public void setRpm(String rpm) {
		setRpm(Double.parseDouble(rpm));
	}

	public double getThrottle() {
		return throttle;
	}
	
	public String getStringThrottle() {
		return Integer.toString( (int) throttle ) + "%";
	}
	
	public void setThrottle(double th) {
		this.throttle = (th - 16) * 1.42;
		if (throttle < -1)
			throttle = -1;
		if (throttle > 70) {
			score = (score * 9 + 2) / 10;
		}
		
	}

	public double getAcceleration() {
		return acceleration;
	}
	
	public long getCreatedAt() {
		return this.created_at;
	}
	
	/**
	 * Trip distance
	 * @return distance in Km
	 */
	public double getDistance() {
		return this.distance / 1000;
	}

	public String getStringAcceleration() {
		return String.format("%.1f", acceleration);
	}

	public int getGear() {
		return gear;
	}
	
	public boolean isRecording() {
		return isRecording;
	}
	
	public String getStringGear() {
		//return String.format("%.2f", raport);
		
		if (gear >= 1 && gear <=6)
			return Integer.toString(gear);
		if (gear == 0)
			return "N";
		if (gear == -1)
			return "R";
		
		return "Na";
	}
	
	public double getScore() {
		return this.score;
	}
	
	public String getNote() {
		String connStatus = "";
		switch (connectionStatus) {
			case BluetoothChatService.STATE_CONNECTED:
				connStatus = "Connected";
				break;
			case BluetoothChatService.STATE_CONNECTING:
				connStatus = "Connecting...";
				break;
			case BluetoothChatService.STATE_CONNECTION_LOST:
				connStatus = "Connection Lost";
				break;
			case BluetoothChatService.STATE_NONE:
			default:
			connStatus = "?? Unknown State ??";
		}
		return "\n" + connStatus + "\n" + Double.toString(this.score) + "   " + String.format("%.0f", distance) + "m  " 
				+ "\n " + status;
	}
	
	public Trip getTrip() {
		return this.trip;
	}
	
	public void setConnectionStatus(int status){
		connectionStatus = status;
	}
	
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

	public int getRetryCount() {
		return retryCount;
	}


}
