package zunair.syed.trackyourones.fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import zunair.syed.trackyourones.R;
import zunair.syed.trackyourones.adapter.ChitChatCustomAdapter;
import zunair.syed.trackyourones.common.Message;
import zunair.syed.trackyourones.util.AsyncJsonObj;
import zunair.syed.trackyourones.util.IASyncExecutable;
import zunair.syed.trackyourones.util.ServiceHandler;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;


public class ChitChat extends Fragment implements IASyncExecutable {

	Context context;
	View classView;

	EditText messageToPost;
	ImageButton postMessageButton;

	boolean isPostingMessage;

	Message [] messages;
	
	public ChitChat() {
	}

	public ChitChat(Context context) {
		this.context = context;
		isPostingMessage = false;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_chit_chat, container, false);

		this.classView = view;

		postMessageButton = (ImageButton) view.findViewById(R.id.chitchat_post_message_button);
		messageToPost = (EditText) view.findViewById(R.id.chitchat_message_to_post_edittext);

		postMessageButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				postOwnerMessage();
			}
		});


		AsyncJsonObj peopleGetter = new AsyncJsonObj("",  this, false);
		peopleGetter.execute();

		return view;
	}


	public void createMessageList(){

		if(! (messages == null)){
			ListAdapter zunairCustomAdapter = new ChitChatCustomAdapter (context, messages); //just a custom listAdapter instead of the old generic one

			ListView zunairListView = (ListView) classView.findViewById(R.id.messageList);
			zunairListView.setAdapter(zunairCustomAdapter);//Converting lists using adapter (a converter) into listview For our Custom Lists

		}
	}


	@Override
	public void logicOnPreExecute() {
	}

	@Override
	public void logicWhileInBackground() {

		if(isPostingMessage){
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			//Params for the request
			params.add(new BasicNameValuePair("name", getOwnerName()));
			params.add(new BasicNameValuePair("message", messageToPost.getText().toString()));
			params.add(new BasicNameValuePair("time", getCurrentDateTime()));

			//code for table
			SharedPreferences pref = context.getSharedPreferences("MyCode", context.MODE_PRIVATE); 
			params.add(new BasicNameValuePair("code", pref.getString("code", "NotAvailable")));

			// Creating service handler class instances
			ServiceHandler sh = new ServiceHandler();


			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall("http://zunairgames.site90.com/webservice/post_message.php", ServiceHandler.POST, params);

			Log.d("Response: ", "GET_RESPONSE_ADDYOURSELF!!> " + jsonStr);

		}

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		SharedPreferences pref = context.getSharedPreferences("MyCode", context.MODE_PRIVATE); 
		params.add(new BasicNameValuePair("code", pref.getString("code", "NotAvailable")));

		// Creating service handler class instance
		ServiceHandler sh = new ServiceHandler();

		// Making a request to url and getting response
		String jsonStr = sh.makeServiceCall("http://zunairgames.site90.com/webservice/get_messages.php", ServiceHandler.POST, params);

		Log.d("Response: ", "> " + jsonStr);

		if (jsonStr != null) {
			try {
				JSONObject jsonObj = new JSONObject(jsonStr);
				Log.d("TrackurOnes", "HEREEE 3" );

				// Getting JSON Array node
				JSONArray jsonObjects = jsonObj.getJSONArray("posts");
				messages = new Message [jsonObjects.length() ]; //-1 because there is one user himself
				Log.d("TrackurOnes", "HEREEE 4" );

				// looping through All Contacts
				for (int i = 0; i < jsonObjects.length() ; i++) {
					JSONObject person = jsonObjects.getJSONObject(i);
					messages [i] = new Message(person.getString("message_content"), person.getString("submission_date"), person.getString("person_name"));

				}
				//	people = filterOutDeviceOwner(people);
			} catch (JSONException e) {
				e.printStackTrace();
				Log.d("TrackurOnes", "Caught in ASYNC Obj" + e.getMessage());
			}
		} else {
			Log.e("ServiceHandler", "Couldn't get any data from the url");
		}	

	}

	@Override
	public void logicOnPostExecute() {
		createMessageList();
		isPostingMessage=false;
	}

	@Override
	public Context getContextOfClass() {
		return context;
	}



	public void postOwnerMessage(){
		isPostingMessage = true;

		AsyncJsonObj postMyMessage = new AsyncJsonObj("",  this, false);
		postMyMessage.execute();
	}

	public String getOwnerName(){
		SharedPreferences pref = context.getSharedPreferences("MyPersonInformation", context.MODE_PRIVATE); 
		return (pref.getString("name", "Anonymous"));
	}

	public String getCurrentDateTime(){
		DateFormat dateFormat = new SimpleDateFormat("hh:mm a, MM/dd");
		Date date = new Date();
		return dateFormat.format(date); //2014/08/06 15:59:48
	}


}
