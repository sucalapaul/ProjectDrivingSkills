/*
 * TODO put header
 */
package com.dexter.drivingskills.api.commands.engine;

import com.dexter.drivingskills.api.commands.PercentageObdCommand;
import com.dexter.drivingskills.api.enums.AvailableCommandNames;

/**
 * Calculated Engine Load value.
 */
public class EngineLoadObdCommand extends PercentageObdCommand {

	public EngineLoadObdCommand() {
		super("01 04");
	}

	/**
	 * @param other
	 */
	public EngineLoadObdCommand(EngineLoadObdCommand other) {
		super(other);
	}

	/* (non-Javadoc)
	 * @see com.dexter.drivingskills.api.commands.ObdBaseCommand#getName()
	 */
	@Override
	public String getName() {
		return AvailableCommandNames.ENGINE_LOAD.getValue();
	}

}