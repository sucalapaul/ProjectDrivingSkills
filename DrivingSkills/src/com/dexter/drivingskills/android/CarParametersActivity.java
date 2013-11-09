package com.dexter.drivingskills.android;

import com.dexter.drivingskills.android.BluetoothChatService.LocalBinder;
import com.dexter.drivingskills.android.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;

public class CarParametersActivity extends Activity {
	
	protected static final int MESSAGE_UPDATE_CAR_PARAMETERS = 0;
	protected static final int MESSAGE_TOAST = 1;
	protected static final String TOAST = "toast";
	
	BluetoothChatService mService;
    boolean mBound = false;
    
    //UI elements
	private TextView mTextSpeed;
	private TextView mTextRPM;
	private TextView mTextGear;
	private TextView mTextThrottle;
	private TextView mTextAcceleration;
	private TextView mTextDrivingType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car_parameters);
		
		mTextSpeed = (TextView) findViewById(R.id.speed);
		mTextRPM = (TextView) findViewById(R.id.rpm);
		mTextGear = (TextView) findViewById(R.id.gear);
		mTextThrottle = (TextView) findViewById(R.id.throttle);
		mTextAcceleration = (TextView) findViewById(R.id.acceleration);
		mTextDrivingType = ( TextView ) findViewById(R.id.driving_type);
		// Show the Up button in the action bar.
		//setupActionBar();
		
		//TODO: avoid creating a new service
		// use startService?
		
        // Bind to LocalService
        Intent intent = new Intent(this, BluetoothChatService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}
	
    @Override
    protected void onStart() {
        super.onStart();

    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
    	// Pass a handler to background service
    	// to update UI in real time
    	if (mService != null && ( !mBound )) {
            mService.registerHandler(mHandler);
            mBound = true;
    	}
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    }
    
    @Override
    protected void onStop() {
        super.onStop();

    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mService.registerHandler(null);
            mBound = false;
        }
    }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.car_parameters, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
//		case android.R.id.home:
//			// This ID represents the Home or Up button. In the case of this
//			// activity, the Up button is shown. Use NavUtils to allow users
//			// to navigate up one level in the application structure. For
//			// more details, see the Navigation pattern on Android Design:
//			//
//			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
//			//
//			NavUtils.navigateUpFromSameTask(this);
//			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalBinder binder = (LocalBinder) service;
            mService = binder.getService();
            mService.registerHandler(mHandler);
            mService.loadDB();
            mBound = true;
        }
        
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
    
    
    // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
    	
    	@Override
    	public void handleMessage(Message msg) {
    		switch (msg.what) {
    			case MESSAGE_UPDATE_CAR_PARAMETERS:
    				CarParameters mCarParameters = (CarParameters) msg.obj;
    				mTextSpeed.setText( mCarParameters.getStringSpeed() );
    				mTextRPM.setText( mCarParameters.getStringRpm() );
    				mTextGear.setText( mCarParameters.getStringGear() );
    				mTextThrottle.setText( mCarParameters.getStringThrottle() );
    				mTextAcceleration.setText( mCarParameters.getStringAcceleration() );
    				mTextDrivingType.setText( mCarParameters.getNote() );

    				break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
                    	 .show();
                    break;
    		}
    	}
    };

}
