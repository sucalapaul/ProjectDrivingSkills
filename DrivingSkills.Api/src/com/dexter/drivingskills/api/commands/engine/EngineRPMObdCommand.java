/*
 * TODO put header
 */
package com.dexter.drivingskills.api.commands.engine;

import com.dexter.drivingskills.api.commands.ObdCommand;
import com.dexter.drivingskills.api.enums.AvailableCommandNames;

/**
 * Displays the current engine revolutions per minute (RPM).
 */
public class EngineRPMObdCommand extends ObdCommand {
	
	private int _rpm = -1;
	
	/**
	 * Default ctor.
	 */
	public EngineRPMObdCommand() {
		super("01 0C");
	}

	/**
	 * Copy ctor.
	 * 
	 * @param other
	 */
	public EngineRPMObdCommand(EngineRPMObdCommand other) {
		super(other);
	}

	/**
	 * @return the engine RPM, string format: "Value RPM"
	 */
	@Override
	public String getFormattedResult() {
		getParsedResult();
		return String.format("%d%s", _rpm, " RPM");
	}
	
	/**
	 * @return the engine RPM, raw int value;
	 */
	public int getParsedResult() {
		if (!"NODATA".equals(getResult())) {
			// ignore first two bytes [41 0C] of the response
			int a = buffer.get(2);
			int b = buffer.get(3);
			_rpm = (a * 256 + b) / 4;
		}
		else
		{
			_rpm = -1;
		}
		return _rpm;
		
	}

	@Override
	public String getName() {
		return AvailableCommandNames.ENGINE_RPM.getValue();
	}
	
	public int getRPM() {
		getParsedResult();
		return _rpm;
	}
}