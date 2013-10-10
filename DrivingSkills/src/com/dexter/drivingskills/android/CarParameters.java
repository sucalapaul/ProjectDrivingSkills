package com.dexter.drivingskills.android;

/**
 * This class contains data about car, obtained through OBD terminal (speed, rpm)
 * or computed (acceleration)
 * @author dexter
 */
public class CarParameters {
	
	//Member Fields
	private double speed;
	private double rpm;
	private double throttle;
	private double acceleration;
	private int 	 gear;

	/**
	 * Constructor. Initializes data.
	 */
	public CarParameters() {
		speed 	 = 0;
		rpm		 = 0;
		throttle = 0;
		acceleration = 0;
		gear 	 = 0;
		
	}

	
	private void updateAcceleration() {
		// TODO Derivate Speed to obtain acceleration
		
	}
	

	private void updateGear() {
		// TODO Compute ration Speed/rpm
		// if RPM < 1500 -> N
		
	}
	
	//TODO Add synchronized to all getters and setters
	
	public double getSpeed() {
		return speed;
	}
	
	public String getStringSpeed() {
		return Integer.toString( (int) speed );
	}

	public void setSpeed(double speed) {
		this.speed = speed;
		updateAcceleration();
		updateGear();
	}

	public double getRpm() {
		return rpm;
	}
	
	public String getStringRpm() {
		return Integer.toString( (int) rpm );
	}

	public void setRpm(double rpm) {
		this.rpm = rpm;
		updateGear();
	}

	public double getThrottle() {
		return throttle;
	}
	
	public String getStringThrottle() {
		return Integer.toString( (int) throttle ) + "%";
	}

	public double getAcceleration() {
		return acceleration;
	}

	public int getGear() {
		return gear;
	}
	
	public String getStringGear() {
		
		if (gear >= 1 && gear <=6)
			return Integer.toString(gear);
		if (gear == 0)
			return "N";
		if (gear == -1)
			return "R";
		
		return "Na";
	}

}
