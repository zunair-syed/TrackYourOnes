package zunair.syed.trackyourones.activity;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import zunair.syed.trackyourones.R;
import zunair.syed.trackyourones.common.Person;
import zunair.syed.trackyourones.util.AsyncJsonObj;
import zunair.syed.trackyourones.util.IASyncExecutable;
import zunair.syed.trackyourones.util.LocationFinder;
import zunair.syed.trackyourones.util.ServiceHandler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class AddYourself extends Activity implements  IASyncExecutable {


	private EditText personName;
	private EditText personStatus;
	private ImageButton addPictureButton;
	private Bitmap personPicture;

	private Person newPerson;

	private LocationFinder locationFinder;


	private static final int PICK_FROM_CAMERA = 1;
	private static final int PICK_FROM_GALLERY = 2;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_yourself_screen);
		getWindow().setWindowAnimations(android.R.anim.slide_in_left);

		personName = (EditText) findViewById(R.id.startup_code_text);
		personStatus = (EditText) findViewById(R.id.create_person_status);
		addPictureButton = (ImageButton) findViewById(R.id.addPicture);

		locationFinder = new LocationFinder(this);
	}

	/* Request updates at startup */
	@Override
	protected void onResume() {
		super.onResume();
		locationFinder.getRequestLocationUpdates();
	}

	/* Remove the locationlistener updates when Activity is paused */
	@Override
	protected void onPause() {
		super.onPause();
		locationFinder.getRequestRemoveUpdates();
	}

	/* When User presses Proceed Button */
	public void doneAdding(View view){

		if (locationFinder.getLocationNotAvailable() == true){

			AlertDialog alertDialog = new AlertDialog.Builder(AddYourself.this).create();
			alertDialog.setTitle("Your Location Cannot be found");
			alertDialog.setMessage("Cannot Post: Make sure your GPS is turned on.");
			alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

			alertDialog.show();
		}else{

			//Create a new Async http post method and post the new person's info
			AsyncJsonObj personPoster = new AsyncJsonObj("Posting your info...", this, true);
			newPerson = new Person(personName.getText().toString(), personStatus.getText().toString(), locationFinder.getCity());

			//Save person info in SharedPreferences for later use
			SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPersonInformation", MODE_PRIVATE); 
			Editor editor = pref.edit();
			editor.putString("name", personName.getText().toString());
			editor.putString("status", personStatus.getText().toString());
			editor.putString("location", locationFinder.getCity());
			editor.commit();


			if(personPicture != null){
				newPerson.setPicture(personPicture);
				saveImage(personPicture); //saves image in device
			}

			//Start the posting
			personPoster.execute();

		}
	}


	/* User want's to proceed without posting himself */
	public void closeActivity(View view){
		Intent goToMainDashboard = new Intent(this, MainDashboard.class);
		startActivity(goToMainDashboard);
		overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
		finish();
	}




	public void addPicture(View view)
	{

		Intent intent = new Intent();

		// call android default gallery
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);

		// crop image
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 0);
		intent.putExtra("aspectY", 0);
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 150);

		try {
			intent.putExtra("return-data", true);
			startActivityForResult(Intent.createChooser(intent,
					"Complete action using"), PICK_FROM_GALLERY);
		} catch (Exception e) {
		}


	}




	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(data != null && data.getExtras() != null){
			
			//Implemention for picking from camera is coded already...for later use
			if (requestCode == PICK_FROM_CAMERA) {
				Bundle extras = data.getExtras();
				if (extras != null) {
					personPicture = extras.getParcelable("data");
					addPictureButton.setImageBitmap(personPicture);

				}
			}

			if (requestCode == PICK_FROM_GALLERY) {
				Bundle extras2 = data.getExtras();
				if (extras2 != null) {
					personPicture = extras2.getParcelable("data");
					addPictureButton.setImageBitmap(personPicture);
				}
			}
		}

	}


	@Override
	public void logicOnPreExecute() {
		
	}

	@Override
	public void logicWhileInBackground() {

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		//Params for the request
		params.add(new BasicNameValuePair("name", newPerson.getName()));
		params.add(new BasicNameValuePair("status", newPerson.getStatus()));
		params.add(new BasicNameValuePair("location", newPerson.getLocation()));

		//get code from shared preferences
		SharedPreferences pref = getSharedPreferences("MyCode", MODE_PRIVATE); 
		params.add(new BasicNameValuePair("code", pref.getString("code", "NotAvailable")));

		if(newPerson.getPicture() != null){
			params.add(new BasicNameValuePair("Picture", getImageToByte64( newPerson.getPicture() )));
		}

		// Creating service handler class instances
		ServiceHandler sh = new ServiceHandler();

		// Making a request to url and getting response
		String jsonStr = sh.makeServiceCall("http://zunairgames.site90.com/webservice/create_person_info.php", ServiceHandler.POST, params);

		Log.d("Response: ", "GET_RESPONSE_ADDYOURSELF!!> " + jsonStr);

	}

	@Override
	public void logicOnPostExecute() {
		//close current activity and proceed to main dashboard
		closeActivity(null);
	}


	public String getImageToByte64(Bitmap bitmap)
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream); //compress to which format you want.
		byte [] byte_arr = stream.toByteArray();
		String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT );
		return image_str;
	}

	@Override
	public Context getContextOfClass() {
		return this;
	}


	/* Save image to device */
	public void saveImage(Bitmap b){

		FileOutputStream out;
		try {
			out = this.openFileOutput("PersonImage", Context.MODE_PRIVATE);
			b.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this,  "Whoops, Something Went Wrong :(", Toast.LENGTH_SHORT).show();
		}
	}



}


