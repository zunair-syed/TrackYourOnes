package zunair.syed.trackyourones.adapter;


import java.io.FileInputStream;

import zunair.syed.trackyourones.R;
import zunair.syed.trackyourones.R.drawable;
import zunair.syed.trackyourones.R.id;
import zunair.syed.trackyourones.R.layout;
import zunair.syed.trackyourones.common.Person;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PeopleCheckerCustomAdapter extends ArrayAdapter <String>{

	Bitmap photo; 
	TextView personStatus;
	TextView personLocation;
	TextView personName;
	ImageView zunairImage;
	Person [] people;
	Context context;

	public PeopleCheckerCustomAdapter(Context context, Person [] people) {
		super(context,R.layout.custom_people_list, new String[people.length]); // create a string array with the number of units our people array has
		this.people = people;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater zunairInflater = LayoutInflater.from(getContext()); 

		View customView = zunairInflater.inflate(R.layout.custom_people_list, parent, false);

		Person singlePerson = people[position]; //Reference to the name of the item from the list using position and getting it from the array
		personStatus = (TextView) customView.findViewById(R.id.PersonStatus); 
		personLocation = (TextView) customView.findViewById(R.id.PersonLocation);
		personName = (TextView) customView.findViewById(R.id.PersonName);
		zunairImage = (ImageView) customView.findViewById(R.id.PersonImage);

		personStatus.setText(singlePerson.getStatus());
		personLocation.setText(singlePerson.getLocation());
		personName.setText(singlePerson.getName());

		if(singlePerson.getPicture() != null){
			zunairImage.setImageBitmap(singlePerson.getPicture()); 
		}else{
			zunairImage.setImageResource(R.drawable.ic_launcher); 
		}
		return customView; //Return this view
	}





}
