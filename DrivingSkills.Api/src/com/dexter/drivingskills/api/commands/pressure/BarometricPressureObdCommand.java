/*
 * TODO put header
 */
package com.dexter.drivingskills.api.commands.pressure;

import com.dexter.drivingskills.api.enums.AvailableCommandNames;


/**
 * Barometric pressure.
 */
public class BarometricPressureObdCommand extends PressureObdCommand {

	public BarometricPressureObdCommand() {
		super("01 33");
	}

	/**
	 * @param other
	 */
	public BarometricPressureObdCommand(PressureObdCommand other) {
		super(other);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.dexter.drivingskills.api.commands.ObdBaseCommand#getName()
	 */
	@Override
	public String getName() {
		return AvailableCommandNames.BAROMETRIC_PRESSURE.getValue();
	}

}