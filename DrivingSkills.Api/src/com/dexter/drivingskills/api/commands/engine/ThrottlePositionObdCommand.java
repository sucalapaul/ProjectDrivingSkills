/*
 * TODO put header
 */
package com.dexter.drivingskills.api.commands.engine;

import com.dexter.drivingskills.api.commands.PercentageObdCommand;
import com.dexter.drivingskills.api.enums.AvailableCommandNames;

/**
 * Read the throttle position in percentage.
 */
public class ThrottlePositionObdCommand extends PercentageObdCommand {

	/**
	 * Default ctor.
	 */
	public ThrottlePositionObdCommand() {
		super("01 11");
	}

	/**
	 * Copy ctor.
	 * 
	 * @param other
	 */
	public ThrottlePositionObdCommand(ThrottlePositionObdCommand other) {
		super(other);
	}

	/**
	 * 
	 */
	@Override
	public String getName() {
		return AvailableCommandNames.THROTTLE_POS.getValue();
	}
	
}