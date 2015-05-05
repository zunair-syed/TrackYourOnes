package zunair.syed.trackyourones.fragment;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import zunair.syed.trackyourones.R;
import zunair.syed.trackyourones.R.drawable;
import zunair.syed.trackyourones.R.id;
import zunair.syed.trackyourones.R.layout;
import zunair.syed.trackyourones.common.Person;
import zunair.syed.trackyourones.util.AsyncJsonObj;
import zunair.syed.trackyourones.util.IASyncExecutable;
import zunair.syed.trackyourones.util.LocationFinder;
import zunair.syed.trackyourones.util.ServiceHandler;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class Settings extends Fragment implements IASyncExecutable {

	Button updateButton;
	EditText updateText;
	Person newPerson;
	ImageButton imagerUpdater;
	TextView personName;


	LocationFinder mLocationFinder;

	Context context;
	public Settings(Context context) {
		this.context = context;
	}

	public Settings() {
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_settings, container,
				false);

		context = view.getContext();

		updateButton = (Button) view.findViewById(R.id.update_button);
		updateText = (EditText) view.findViewById(R.id.message_edit_box);
		imagerUpdater = (ImageButton) view.findViewById(R.id.settings_image_button);
		personName= (TextView) view.findViewById(R.id.settings_person_name);


		SharedPreferences pref = context.getSharedPreferences("MyPersonInformation", context.MODE_PRIVATE); 
		personName.setText(pref.getString("name", "Your Name Here"));
		updateText.setText(pref.getString("status", "What You currently doing?"));
		imagerUpdater.setImageBitmap(getImageBitmap());
		
		


		updateButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				updateInfo();
			}
		});

		imagerUpdater.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				updateImage();
			}
		});


		return view;
	}




	public void updateInfo(){
		mLocationFinder = new LocationFinder(context);
		newPerson = new Person(personName.getText().toString(), updateText.getText().toString(), mLocationFinder.getLastKnownLocationCity());

		AsyncJsonObj updaterObj = new AsyncJsonObj("Updating Info...", this, true);
		updaterObj.execute();

	}

	public void updateImage(){
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
		SharedPreferences pref = context.getSharedPreferences("MyCode", context.MODE_PRIVATE); 
		params.add(new BasicNameValuePair("code", pref.getString("code", "NotAvailable")));

		if(newPerson.getPicture() != null){
			params.add(new BasicNameValuePair("Picture", getImageToByte64( newPerson.getPicture() )));
		}

		// Creating service handler class instances
		ServiceHandler sh = new ServiceHandler();

		// Making a request to url and getting response
		String jsonStr = sh.makeServiceCall("http://zunairgames.site90.com/webservice/update_person_info.php", ServiceHandler.POST, params);

		Log.d("Response: ", "> " + jsonStr);
	}

	@Override
	public void logicOnPostExecute() {
	}

	@Override
	public Context getContextOfClass() {
		return context;
	}




	public String getImageToByte64( Bitmap bitmap)
	{
		// Bitmap bitmap = BitmapFactory.decodeResource(ctx.getResources(),R.drawable.ic_launcher);   
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream); //compress to which format you want.
		byte [] byte_arr = stream.toByteArray();
		String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT );
		return image_str;
	}


	public Bitmap getImageBitmap( ){
		try{
			FileInputStream fis = context.openFileInput("PersonImage");
			Bitmap b = BitmapFactory.decodeStream(fis);
			fis.close();
			return b;
		}
		catch(Exception e){
			Bitmap standardImage = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ic_launcher);
			return standardImage;
		}
	}
}
