/*
 * TODO put header
 */
package com.dexter.drivingskills.api.commands;

/**
 * Abstract class for percentage commands.
 */
public abstract class PercentageObdCommand extends ObdCommand {

	/**
	 * @param command
	 */
	public PercentageObdCommand(String command) {
		super(command);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param other
	 */
	public PercentageObdCommand(PercentageObdCommand other) {
		super(other);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	@Override
	public String getFormattedResult() {
		String res = getResult();

		if (!"NODATA".equals(res)) {
			// ignore first two bytes [hh hh] of the response
			float tempValue = (buffer.get(2) * 100.0f) / 255.0f;
			res = String.format("%.1f%s", tempValue, "%");
		}

		return res;
	}
	
	public double getParsedResult() {
		String res = getResult();
		
		if (!"NODATA".equals(res)) {
			// ignore first two bytes [hh hh] of the response
			double tempValue = (buffer.get(2) * 100.0) / 255.0;
			return tempValue;
		}
		else
		{
			return -1;
		}
	}

}