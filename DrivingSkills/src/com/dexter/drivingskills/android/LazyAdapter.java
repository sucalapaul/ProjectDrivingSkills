package com.dexter.drivingskills.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dexter.drivingskills.android.data.Trip;
 
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
 
/**
 * Adapter class which populates the trips ListView
 * @author dexter
 *
 */
public class LazyAdapter extends BaseAdapter {
 
    private Activity activity;
    private List<Trip> data;
    private static LayoutInflater inflater=null;
 
    @Deprecated
    public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
//        activity = a;
//        data=d;
//        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        //imageLoader=new ImageLoader(activity.getApplicationContext());
    }
 
    public LazyAdapter(StatisticsActivity a, List<Trip> d) {
    	activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
        return data.size();
    }
 
    public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    //TODO: reflection?
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row, null);
        
        TextView date_time = (TextView)vi.findViewById(R.id.trip_date_time);
        TextView date = (TextView)vi.findViewById(R.id.trip_date);
        TextView distance = (TextView)vi.findViewById(R.id.trip_distance);
        TextView price = (TextView)vi.findViewById(R.id.trip_price);
        TextView location = (TextView)vi.findViewById(R.id.trip_location);
        TextView driving_type = (TextView)vi.findViewById(R.id.trip_driving_type);
        TextView brake_details = (TextView)vi.findViewById(R.id.trip_brake_details);
        TextView speed_details = (TextView)vi.findViewById(R.id.trip_speed_details);
        TextView throttle_details = (TextView)vi.findViewById(R.id.trip_throttle_details);
        
        Trip trip = data.get(position);
        
        String asd = Trip.DRIVE[Trip.DRIVE_KEEN];
 
        // Setting all values in listview
        date_time.setText( trip.getEndDateTimeString() );
        date.setText( trip.getEndDateString() );
        distance.setText( trip.getDistanceString() );
        price.setText( trip.getPriceString() );
        location.setText( trip.getEndLocation() );
        driving_type.setText( trip.getDrivingTypeString() );
        brake_details.setText( trip.getBrakeTypeString() );
        speed_details.setText( trip.getSpeedTypeString() );
        throttle_details.setText( trip.getThrottleTypeString() );
        return vi;
    }
}