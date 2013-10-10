/*
 * TODO put header 
 */
package com.dexter.drivingskills.api.commands.protocol;

import java.io.IOException;
import java.io.InputStream;

/**
 * This method will reset the OBD connection.
 */
public class ObdResetCommand extends ObdProtocolCommand {

	public ObdResetCommand() {
		super("AT Z");
	}

	/**
	 * @param other
	 */
	public ObdResetCommand(ObdResetCommand other) {
		super(other);
	}

	
//	Overriding this methods let the output from ELM in input buffer
//	/**
//	 * Reset command returns an empty string, so we must override the following
//	 * two methods.
//	 * @throws IOException 
//	 */
//	@Override
//	public void readResult(InputStream in) throws IOException {
//		// do nothing
//		return;
//	}
//
//	@Override
//	public String getResult() {
//		return "";
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dexter.drivingskills.api.commands.ObdBaseCommand#getFormattedResult()
	 */
	@Override
	public String getFormattedResult() {
		return getResult();
	}

	@Override
	public String getName() {
		return "Reset OBD";
	}

}