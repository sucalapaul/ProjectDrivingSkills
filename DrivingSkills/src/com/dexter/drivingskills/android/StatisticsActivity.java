package com.dexter.drivingskills.android;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;

import com.dexter.drivingskills.android.data.Trip;
import com.dexter.drivingskills.android.data.TripsDataSource;

public class StatisticsActivity extends Activity {
	
	ListView list;
	LazyAdapter adapter;
	private TripsDataSource datasource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);
		
		datasource = new TripsDataSource(this);
	    datasource.open();
	    
	    List<Trip> tripsList = datasource.getAllTrips();
	    
	    
		
		
		//TODO: Get these from Database!
		
//        ArrayList<HashMap<String, String>> tripsList = new ArrayList<HashMap<String, String>>();
//        
//        // creating new HashMap
//        HashMap<String, String> map;
//        
//        map = new HashMap<String, String>();
//        
//        // adding each child node to HashMap key => value
//        map.put(KEY_ID, "1");
//        map.put(KEY_DATE_TIME, "13:51");
//        map.put(KEY_DATE, "today");
//        map.put(KEY_DISTANCE, "22.3");
//        map.put(KEY_PRICE, "10");
//        map.put(KEY_LOCATION, "Cluj-Napoca, Cluj");
//        map.put(KEY_DRIVING_TYPE, "Aggressive Driving");
//        map.put(KEY_BRAKE_DETAILS, "Hard \nbrakes");
//        map.put(KEY_SPEED_DETAILS, "Over \nspeed");
//        map.put(KEY_THROTTLE_DETAILS, "High \nthrottle");
//
//        // adding HashList to ArrayList
//        tripsList.add(map);        
//        map = new HashMap<String, String>();
//        
//        // adding each child node to HashMap key => value
//        map.put(KEY_ID, "1");
//        map.put(KEY_DATE_TIME, "23.11");
//        map.put(KEY_DATE, "yesterday");
//        map.put(KEY_DISTANCE, "81.3");
//        map.put(KEY_PRICE, "53");
//        map.put(KEY_LOCATION, "Zalau, Salaj");
//        map.put(KEY_DRIVING_TYPE, "Economical Driving");
//        map.put(KEY_BRAKE_DETAILS, "Hard \nbrakes");
//        map.put(KEY_SPEED_DETAILS, "Over \nspeed");
//        map.put(KEY_THROTTLE_DETAILS, "High \nthrottle");
//
//        // adding HashList to ArrayList
//        tripsList.add(map);

 
        list=(ListView)findViewById(R.id.list);
 
        // Getting adapter by passing xml data ArrayList
        adapter=new LazyAdapter(this, tripsList);
        list.setAdapter(adapter);
 
        // Click event for single list row
        list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				
			}
        });
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		Button b = (Button) findViewById(R.id.button_db);
		b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				long now = System.currentTimeMillis() / 1000L;
				Random rand = new Random();
				double distance = rand.nextDouble()*50 + 10;
				
				Trip trip = datasource.createTrip(
						"Cluj-Napoca, CJ",
						"Cluj-Napoca, CJ", 
						now,
						now,
						distance,
						distance * 6.3,
						Trip.BRAKE_HARD, 
						Trip.SPEED_NORMAL,
						Trip.THROTTLE_HARD, 
						Trip.DRIVE_AGGRESSIVE);
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.statistics, menu);
		return true;
	}

}
