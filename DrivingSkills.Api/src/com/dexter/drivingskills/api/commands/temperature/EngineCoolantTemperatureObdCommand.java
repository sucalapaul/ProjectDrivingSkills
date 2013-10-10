/*
 * TODO put header
 */
package com.dexter.drivingskills.api.commands.temperature;

import com.dexter.drivingskills.api.enums.AvailableCommandNames;

/**
 * Engine Coolant Temperature.
 */
public class EngineCoolantTemperatureObdCommand extends TemperatureObdCommand {

	/**
	 * 
	 */
	public EngineCoolantTemperatureObdCommand() {
		super("01 05");
	}

	/**
	 * @param other
	 */
	public EngineCoolantTemperatureObdCommand(TemperatureObdCommand other) {
		super(other);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dexter.drivingskills.api.commands.ObdBaseCommand#getName()
	 */
	@Override
	public String getName() {
		return AvailableCommandNames.ENGINE_COOLANT_TEMP.getValue();
	}

}