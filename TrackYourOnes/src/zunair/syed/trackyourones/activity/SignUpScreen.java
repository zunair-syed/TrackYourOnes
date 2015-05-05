package zunair.syed.trackyourones.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import zunair.syed.trackyourones.R;
import zunair.syed.trackyourones.R.anim;
import zunair.syed.trackyourones.R.id;
import zunair.syed.trackyourones.R.layout;
import zunair.syed.trackyourones.util.AsyncJsonObj;
import zunair.syed.trackyourones.util.IASyncExecutable;
import zunair.syed.trackyourones.util.ServiceHandler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpScreen extends Activity implements  IASyncExecutable{

	EditText codeText;
	boolean goodResponse;
	boolean creatingNewGroup;
	String code;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setWindowAnimations(R.anim.abc_slide_out_bottom);
		
		creatingNewGroup=false;
		goodResponse = false;

		setContentView(R.layout.start_up_screen);
	}

	/* When pressing go on EditText */
	public void goAction(View view)
	{
		codeText =(EditText) findViewById(R.id.startup_code_text);

		if(! codeText.getText().toString().equals("")){
			AsyncJsonObj databaseChecker = new AsyncJsonObj("Checking Database...",  this, true);
			databaseChecker.execute();
		}else{
			Toast.makeText(this, "Please enter a code", Toast.LENGTH_SHORT).show();;
		}
	}

	/* New group Button OnClick method */
	public void newgroupaction(View view)
	{
		creatingNewGroup = true;

		AsyncJsonObj newGroupCreator = new AsyncJsonObj("Creating New Group...",  this, true);
		newGroupCreator.execute();
	}


	@Override
	public void logicOnPreExecute() {
	}


	@Override
	public void logicWhileInBackground() {

		if(creatingNewGroup == false){ //We are checking for a database

			List<NameValuePair> params = new ArrayList<NameValuePair>();

			//Params for the request
			params.add(new BasicNameValuePair("code", codeText.getText().toString()));

			// Creating service handler class instances
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String responseStr = sh.makeServiceCall("http://zunairgames.site90.com/webservice/check_if_code_exist.php", ServiceHandler.POST, params);

			Log.d("Response: ", "GET_RESPONSE> " + responseStr);		

			if (responseStr != null) {
				if(responseStr.contains("Does Not Exist")){
					goodResponse = false;
				}else{
					goodResponse = true;
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			//Save the entered code for later use
			SharedPreferences pref = getApplicationContext().getSharedPreferences("MyCode", MODE_PRIVATE); 
			Editor editor = pref.edit();
			editor.putString("code", codeText.getText().toString());
			editor.commit(); 
			
		}else{ //We are creating a new group
			
			// Creating service handler class instances
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String responseStr = sh.makeServiceCall("http://zunairgames.site90.com/webservice/new_group.php", ServiceHandler.POST);
			
			Log.d("Response: ", " CODE:"+code+" RESPONSE>" + responseStr);		

			if (responseStr != null) {
				if(responseStr.contains("|")){
					code = responseStr.substring(2,6); //getting specific code word from response from JSON string
				}else{
					code = null;
				}
			} else {
				Log.d("ServiceHandler", "Couldn't get any data from the url");
			}

			//save the code
			SharedPreferences pref = getApplicationContext().getSharedPreferences("MyCode", MODE_PRIVATE); 
			Editor editor = pref.edit();
			editor.putString("code", code);
			editor.commit(); 		

		}

	}


	@Override
	public void logicOnPostExecute() {
		//show the code to the user so he can remember it
		if(creatingNewGroup){
			Toast.makeText(this, "Your Code: "+code, Toast.LENGTH_LONG).show();
		}
		
		moveToAddYourSelfScreen();
	}

	@Override
	public Context getContextOfClass() {
		return this;
	}

	public void moveToAddYourSelfScreen(){
		Intent goToSetupScreen = new Intent(this, AddYourself.class);
		
		startActivity(goToSetupScreen);
		overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
		finish();
	}


}
