/*
 * TODO put header
 */
package com.dexter.drivingskills.api.commands.protocol;

import com.dexter.drivingskills.api.enums.ObdProtocols;

/**
 * Select the protocol to use.
 */
public class SelectProtocolObdCommand extends ObdProtocolCommand {
	
	private final ObdProtocols _protocol;

	/**
	 * @param protocol
	 */
	public SelectProtocolObdCommand(ObdProtocols protocol) {
		super("AT SP " + protocol.getValue());
		_protocol = protocol;
	}

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
		return "Select Protocol " + _protocol.name();
	}

}