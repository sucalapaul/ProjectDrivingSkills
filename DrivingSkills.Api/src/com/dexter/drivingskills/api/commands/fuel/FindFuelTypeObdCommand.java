/*
 * TODO put header 
 */
package com.dexter.drivingskills.api.commands.fuel;

import com.dexter.drivingskills.api.commands.ObdCommand;
import com.dexter.drivingskills.api.commands.utils.ObdUtils;
import com.dexter.drivingskills.api.enums.AvailableCommandNames;

/**
 * This command is intended to determine the vehicle fuel type.
 */
public class FindFuelTypeObdCommand extends ObdCommand {

	private int fuelType = 0;

	/**
	 * Default ctor.
	 */
	public FindFuelTypeObdCommand() {
		super("10 51");
	}

	/**
	 * Copy ctor
	 * 
	 * @param other
	 */
	public FindFuelTypeObdCommand(FindFuelTypeObdCommand other) {
		super(other);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dexter.drivingskills.api.command.ObdBaseCommand#getFormattedResult()
	 */
	@Override
	public String getFormattedResult() {
		String res = getResult();

		if (!"NODATA".equals(res)) {
			// ignore first two bytes [hh hh] of the response
			fuelType = buffer.get(2);
			res = getFuelTypeName();
		}

		return res;
	}
	
	/**
	 * @return Fuel type name.
	 */
	public final String getFuelTypeName() {
		return ObdUtils.getFuelTypeName(fuelType);
	}

	@Override
	public String getName() {
		return AvailableCommandNames.FUEL_TYPE.getValue();
	}

}