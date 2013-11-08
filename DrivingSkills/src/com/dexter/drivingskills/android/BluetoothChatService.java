/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dexter.drivingskills.android;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

import com.dexter.drivingskills.api.commands.ObdBaseCommand;
import com.dexter.drivingskills.api.commands.SpeedObdCommand;
import com.dexter.drivingskills.api.commands.engine.EngineRPMObdCommand;
import com.dexter.drivingskills.api.commands.engine.ThrottlePositionObdCommand;
import com.dexter.drivingskills.api.commands.protocol.EchoOffObdCommand;
import com.dexter.drivingskills.api.commands.protocol.LineFeedOffObdCommand;
import com.dexter.drivingskills.api.commands.protocol.ObdResetCommand;
import com.dexter.drivingskills.api.commands.protocol.SelectProtocolObdCommand;
import com.dexter.drivingskills.api.commands.protocol.TimeoutObdCommand;
import com.dexter.drivingskills.api.enums.ObdProtocols;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

/**
 * This class does all the work for setting up and managing Bluetooth
 * connections with other devices. It has a thread that listens for
 * incoming connections, a thread for connecting with a device, and a
 * thread for performing data transmissions when connected.
 */
public class BluetoothChatService extends Service {
    // Debugging
    private static final String TAG = "BluetoothChatService";
    private static final boolean D = true;

    // Name for the SDP record when creating server socket
    private static final String NAME = "DrivingSkills";

    // Unique UUID for this application, set the connection as a Serial Port
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

	private static final String address = "11:22:33:DD:EE:FF"; 
    
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();

    // Member fields
    private final BluetoothAdapter mAdapter;
    private Handler mHandler;
    private final CarParameters mCarParameters;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private int mState;
    

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device
    public static final int STATE_CONNECTION_LOST = 4;

    /**
     * Constructor. Prepares a new DrivingSkills session.
     * @param context  The UI Activity Context
     * @param handler  A Handler to send messages back to the UI Activity
     */
    public BluetoothChatService(Context context, Handler handler) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
        mHandler = handler;
        mCarParameters = new CarParameters();
    	BluetoothChatService.this.start();
    }
    
    /**
     * Default constructor, kept for backward compatibility
     */
    public BluetoothChatService() {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mHandler = null;
        mState = STATE_NONE;
    	mCarParameters = new CarParameters();
    	BluetoothChatService.this.start();
    }

    /**
     * Set the current state of the chat connection
     * @param state  An integer defining the current connection state
     */
    private synchronized void setState(int state) {
        if (D) Log.d(TAG, "setState() " + mState + " -> " + state);
        mState = state;

        // Give the new state to the Handler so the UI Activity can update
        //mHandler.obtainMessage(DrivingSkills.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }

    /**
     * Return the current connection state. */
    public synchronized int getState() {
        return mState;
    }
    

	// /**
	// * Try to connect to a bluetooth device
	// * Create ConnectThread
	// */
	public synchronized void start() {
		if (D)
			Log.d(TAG, "Trying to connect");
		
        // Feedback for UI
        if (mHandler != null) {
	        Message msg = mHandler.obtainMessage(CarParametersActivity.MESSAGE_TOAST);
	        Bundle bundle = new Bundle();
	        bundle.putString(CarParametersActivity.TOAST, "Trying to connect");
	        msg.setData(bundle);
	        mHandler.sendMessage(msg);
        }

		// Cancel any thread attempting to make a connection
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		// Cancel any thread currently running a connection
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		// Get the local Bluetooth adapter
		BluetoothAdapter mBluetoothAdapter;
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// TODO: get the address from database, now is hardcoded :(
		// Connect to last device
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

		// Attempt to connect to the device
		connect(device);
		setState(STATE_CONNECTING);
	}

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     * @param device  The BluetoothDevice to connect
     */
    public synchronized void connect(BluetoothDevice device) {
        if (D) Log.d(TAG, "connect to: " + device);

        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}

        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
        setState(STATE_CONNECTING);
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     * @param socket  The BluetoothSocket on which the connection was made
     * @param device  The BluetoothDevice that has been connected
     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        if (D) Log.d(TAG, "connected");

        // Cancel the thread that completed the connection
        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}

        // Cancel the accept thread because we only want to connect to one device
        //if (mAcceptThread != null) {mAcceptThread.cancel(); mAcceptThread = null;}

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();

        // Send the name of the connected device back to the UI Activity
        if (mHandler != null) {
	        Message msg = mHandler.obtainMessage(CarParametersActivity.MESSAGE_TOAST);
	        Bundle bundle = new Bundle();
	        bundle.putString(CarParametersActivity.TOAST, "Connected to " + device.getName());
	        msg.setData(bundle);
	        mHandler.sendMessage(msg);
        }

        setState(STATE_CONNECTED);
    }

    /**
     * Stop all threads
     */
    public synchronized void stop() {
        if (D) Log.d(TAG, "stop");
        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}
        setState(STATE_NONE);
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    @Deprecated
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            r = mConnectedThread;
        }
        // Perform the write unsynchronized
        r.write(out);
    }
    

    @Deprecated
	public void sendCommand1(ObdBaseCommand obdCommand) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            r = mConnectedThread;
        }
        // Perform the write unsynchronized
        //r.runCommand(obdCommand);
		
	}

    /**
     * Indicate that the connection attempt failed
     */
    private void connectionFailed() {
        setState(STATE_CONNECTION_LOST);
        BluetoothChatService.this.start();
    }

    /**
     * Indicate that the connection was lost
     */
    private void connectionLost() {
        setState(STATE_CONNECTION_LOST);
        BluetoothChatService.this.start();

        //TODO: save the trip if we were recording
        // Send a failure message back to the Activity
//        Message msg = mHandler.obtainMessage(DrivingSkills.MESSAGE_TOAST);
//        Bundle bundle = new Bundle();
//        bundle.putString(DrivingSkills.TOAST, "Device connection was lost");
//        msg.setData(bundle);
//        mHandler.sendMessage(msg);
    }


    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;
            
            // Use reflection to obtain a socket
            // Classic method fails on some devices (mine)
            try {
                if (D) Log.i(TAG, "Trying to create RfcommSocket using reflection");
                Method m = device.getClass().getMethod("createRfcommSocket",
                        new Class[] { int.class });
                tmp = (BluetoothSocket)m.invoke(device, 1);
            } catch (Exception e) {
                if (D) Log.w(TAG, "Reflection failed.", e);
                if (D) Log.i(TAG, "Trying to create RfcommSocket using UUID");
                try {
                    tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
                } catch (IOException e2) {
                    Log.e(TAG, "create() failed", e2);
                    //TODO: notify that connection failed
                    // Need to do something smarter than ignore
                    
                }
            }
            mmSocket = tmp;
        }

        
        public void run() {
            Log.i(TAG, "BEGIN mConnectThread");
            setName("ConnectThread");

            // Always cancel discovery because it will slow down a connection
            mAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();
            } catch (IOException e) {
            	//Unable to start Service Discovery
            	Log.e(TAG, "unable to connect: ", e);
                //connectionFailed();
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() socket during connection failure", e2);
                }
                
                // Start the service over to restart searching mode
                try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
                BluetoothChatService.this.start();
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (BluetoothChatService.this) {
                mConnectThread = null;
            }

            // Start the connected thread
            connected(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "create ConnectedThread");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        
        //>TODO: Remove duplicate try-catch
        public void run_loop() {
        	
        	ObdBaseCommand commandReset = new ObdResetCommand();
        	ObdBaseCommand commandEchoOff = new EchoOffObdCommand();
        	ObdBaseCommand commandLineFeedOff = new LineFeedOffObdCommand();
        	ObdBaseCommand commandTimeout = new TimeoutObdCommand(62);
        	ObdBaseCommand commandProtocol = new SelectProtocolObdCommand(ObdProtocols.AUTO);
        	
    		try {
    			commandReset.run(mmInStream, mmOutStream);
    			commandEchoOff.run(mmInStream, mmOutStream);
    			commandLineFeedOff.run(mmInStream, mmOutStream);
    			commandTimeout.run(mmInStream, mmOutStream);
    			commandProtocol.run(mmInStream, mmOutStream);
    		}
    		catch (IOException e) {
				Log.e(TAG, "IO exception", e);
				connectionLost();
    		} 
    		catch (InterruptedException e) {
				Log.e(TAG, "Interrupted exception", e);
				connectionLost();
    		}
        	
    		// Poll car data
    		// Loop will end when the connection is lost
        	while (true) {
        		EngineRPMObdCommand commandRPM = new EngineRPMObdCommand();
        		SpeedObdCommand commandSpeed = new SpeedObdCommand();
        		ThrottlePositionObdCommand commandThrottle = new ThrottlePositionObdCommand();
        		
        		try {
        			commandRPM.run(mmInStream, mmOutStream);
        			commandSpeed.run(mmInStream, mmOutStream);
        			commandThrottle.run(mmInStream, mmOutStream);
        		}
        		catch (IOException e) {
    				Log.e(TAG, "IO exception", e);
    				connectionLost();
    				break;
        		} 
        		catch (InterruptedException e) {
    				Log.e(TAG, "Interrupted exception", e);
    				connectionLost();
    				break;
				}
        		
        		mCarParameters.setSpeed( commandSpeed.getParsedResult() );
        		mCarParameters.setRpm( commandRPM.getParsedResult() );
        		mCarParameters.setThrottle( commandThrottle.getParsedResult() );
        		
        		// Update UI, if visible
        		try {
        			if (mHandler != null) {
        				mHandler.obtainMessage(CarParametersActivity.MESSAGE_UPDATE_CAR_PARAMETERS, -1, -1, mCarParameters.clone())
        				.sendToTarget();
        			}
        		} catch (CloneNotSupportedException e) {
        			e.printStackTrace();
        		}
        		
        	}
        	
        }

        //TODO: move run_loop directly here
		public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
            byte[] buffer = new byte[1024];
            int bytes;
            
            run_loop();
        }

        /**
         * Write to the connected OutStream.
         * @param buffer  The bytes to write
         */
		@Deprecated
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

		@Deprecated
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

    
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
	public class LocalBinder extends Binder {
		BluetoothChatService getService() {
			//return the instance of BluetootCatService
			return BluetoothChatService.this;
		}
	
	}


	/**
	 *Set a handler of an activity
	 * @param handler
	 */
	public void registerHandler(Handler handler) {
		mHandler = handler;
	}
}

//DEBUG INFO
//10-29 22:25:49.190: D/RAWDATA(19089): SEARCHING...
//UNABLETOCONNECT