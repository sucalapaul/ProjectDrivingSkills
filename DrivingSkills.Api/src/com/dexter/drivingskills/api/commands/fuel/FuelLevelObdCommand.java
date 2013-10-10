/*
 * TODO put header 
 */
package com.dexter.drivingskills.api.commands.fuel;

import com.dexter.drivingskills.api.commands.ObdCommand;
import com.dexter.drivingskills.api.enums.AvailableCommandNames;

/**
 * Get fuel level in percentage
 */
public class FuelLevelObdCommand extends ObdCommand {

	private float fuelLevel = 0f;

	public FuelLevelObdCommand() {
		super("01 2F");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dexter.drivingskills.api.commands.ObdBaseCommand#getFormattedResult()
	 */
	@Override
	public String getFormattedResult() {
		if (!"NODATA".equals(getResult())) {
			// ignore first two bytes [hh hh] of the response
			fuelLevel = 100.0f * buffer.get(2) / 255.0f;
		}

		return String.format("%.1f%s", fuelLevel, "%");
	}

	@Override
	public String getName() {
		return AvailableCommandNames.FUEL_LEVEL.getValue();
	}

}