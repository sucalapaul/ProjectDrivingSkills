/*
 * TODO put header
 */
package com.dexter.drivingskills.api.commands.control;

import com.dexter.drivingskills.api.commands.PercentageObdCommand;
import com.dexter.drivingskills.api.enums.AvailableCommandNames;

/**
 * TODO put description
 * 
 * Timing Advance
 */
public class TimingAdvanceObdCommand extends PercentageObdCommand {

	public TimingAdvanceObdCommand() {
		super("01 0E");
	}

	public TimingAdvanceObdCommand(TimingAdvanceObdCommand other) {
		super(other);
	}

	@Override
	public String getName() {
		return AvailableCommandNames.TIMING_ADVANCE.getValue();
	}
}