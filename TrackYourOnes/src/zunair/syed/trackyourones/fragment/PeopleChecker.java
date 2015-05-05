package zunair.syed.trackyourones.fragment;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import zunair.syed.trackyourones.R;
import zunair.syed.trackyourones.adapter.PeopleCheckerCustomAdapter;
import zunair.syed.trackyourones.common.Person;
import zunair.syed.trackyourones.util.AsyncJsonObj;
import zunair.syed.trackyourones.util.IASyncExecutable;
import zunair.syed.trackyourones.util.ServiceHandler;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;


public class PeopleChecker extends Fragment implements IASyncExecutable {

	Context context;
	View ClassView;
	Person people[];

	private static final String TAG_NAME = "name";
	private static final String TAG_STATUS = "status";
	private static final String TAG_LOCATION = "location";
	private static final String TAG_PICTURE = "Picture";

	public PeopleChecker(Context context) {
		this.context = context;
	}

	public PeopleChecker(){
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ClassView = inflater.inflate(R.layout.fragment_people_checker, container,
				false);

		AsyncJsonObj peopleGetter = new AsyncJsonObj("Loading Contacts...",  this, true);
		peopleGetter.execute();

		return ClassView;
	}





	public void  createPeopleList()
	{
		
		for(int i=0; i<people.length; i++){
			Log.d("Trackyourones", "obj:"+people[i] );
		}
		
		ListAdapter zunairCustomAdapter = new PeopleCheckerCustomAdapter (context, people); //just a custom listAdapter instead of the old generic one

		ListView zunairListView = (ListView) ClassView.findViewById(R.id.peopleList);

		zunairListView.setAdapter(zunairCustomAdapter);//Converting lists using adapter (a converter) into listview For our Custom Lists

		
	}

	@Override
	public void logicOnPreExecute() {
	}

	@Override
	public void logicWhileInBackground() {
		// Creating service handler class instance
		ServiceHandler sh = new ServiceHandler();

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		//Params for the request
		SharedPreferences pref = context.getSharedPreferences("MyCode", context.MODE_PRIVATE); 
		params.add(new BasicNameValuePair("code", pref.getString("code", "NotAvailable")));

		
		// Making a request to url and getting response
		String jsonStr = sh.makeServiceCall("http://zunairgames.site90.com/webservice/get_general_info.php", ServiceHandler.POST,params);

		Log.d("Response: ", "HERE PROBLEM " + pref.getString("code", "NotAvailable")+ "|  > " + jsonStr);

		if (jsonStr != null) {
			try {
				JSONObject jsonObj = new JSONObject(jsonStr);
				Log.d("TrackurOnes", "HEREEE 3" );

				// Getting JSON Array node
				JSONArray jsonObjects = jsonObj.getJSONArray("posts");
				people = new Person [jsonObjects.length() ]; //-1 because there is one user himself
				Log.d("TrackurOnes", "HEREEE 4" );

				// looping through All Contacts
				for (int i = 0; i < jsonObjects.length() ; i++) {
					JSONObject person = jsonObjects.getJSONObject(i);
					//Log.d("TrackurOnes", "HEREEE 5" );

					people [i] = new Person(person.getString(TAG_NAME), person.getString(TAG_STATUS), person.getString(TAG_LOCATION));
					people [i].setPicture(getByte64ToImage( person.getString(TAG_PICTURE)));

				}
				people = filterOutDeviceOwner(people);
			} catch (JSONException e) {
				e.printStackTrace();
				Log.d("TrackurOnes", "Caught in ASYNC Obj" + e.getMessage());
			}
		} else {
			Log.e("ServiceHandler", "Couldn't get any data from the url");
		}
	}

	
	public Person [] filterOutDeviceOwner(Person [] people){
		Person [] tempArray = new Person [people.length];
		
		for(int i=0; i<people.length ; i++){
			Log.d("trackyourones", "people[i]1"+people[i]);
		}
		
		int loopCounter =0;
		for(int i=0; i<people.length; i++){
			if(!checkIfThisIsUser(people[i])){
				Log.d("trackyourones", "i: "+i + "  ||  loopCounter: "+loopCounter);
				Person mPerson = people[i];
				tempArray[loopCounter] = mPerson;
				loopCounter++;
			}
		}
		
		for(int i=0; i<people.length ; i++){
			Log.d("trackyourones", "people[i]2"+tempArray[i]);
		}
		
		if(tempArray[tempArray.length - 1] == null){
			Log.d("trackyourones", "HEERER!!FHEEEEEEEEEEEEEEEEEEEEEEEEEEE");
			people = new Person [tempArray.length - 1];
			for(int i=0; i<people.length ; i++){
				people[i] = tempArray[i];
			}
		}
			
		for(int i=0; i<people.length ; i++){
			Log.d("trackyourones", "people[i]3"+people[i]);
		}
		return people;
	}

	
	
	@Override
	public void logicOnPostExecute() {
		// TODO Auto-generated method stub
		createPeopleList();
	}





	public Bitmap getByte64ToImage(String byte64)
	{
		byte [] imgByteArr = Base64.decode(byte64, Base64.DEFAULT);
		Bitmap Image = BitmapFactory.decodeByteArray(imgByteArr, 0, imgByteArr.length);
		if(Image == null){ Log.d("Trackyourones", "IMAGE WAS NULL");}
		return Image;
	}

	@Override
	public Context getContextOfClass() {
		// TODO Auto-generated method stub
		return context;
	}

	public boolean checkIfThisIsUser(Person person){
		SharedPreferences pref = context.getSharedPreferences("MyPersonInformation", context.MODE_PRIVATE); 
		pref.getString("name", "NotAvailable");
		pref.getString("status", "NotAvailable");
		pref.getString("location", "NotAvailable");
		
		String personname = person.getName();
		Log.d("trackyourones", "personName: "+personname);
		
		String devicePerosnName = pref.getString("name", "NotAvailable");
		Log.d("trackyourones", "devicePerosnName: "+pref.getString("name", "NotAvailable"));
		
		if (personname.equals(devicePerosnName)){
			return true;
		}else{
			return false;
		}
	}


}
