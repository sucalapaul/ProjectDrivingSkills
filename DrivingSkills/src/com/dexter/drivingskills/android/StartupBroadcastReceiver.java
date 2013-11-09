/**
 * 
 */
package com.dexter.drivingskills.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author dexter
 * Start the Bluetooth Service automatically at system startup
 */
public class StartupBroadcastReceiver extends BroadcastReceiver {

	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
//		Intent startServiceIntent = new Intent(context, BluetoothChatService.class);
//		context.startService(startServiceIntent);
	}

}
