package zunair.syed.trackyourones.adapter;

import zunair.syed.trackyourones.R;
import zunair.syed.trackyourones.R.id;
import zunair.syed.trackyourones.R.layout;
import zunair.syed.trackyourones.common.Message;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChitChatCustomAdapter extends ArrayAdapter <String> {

	TextView messageContent;
	TextView posterName;
	TextView compositionDate;

	Message [] messages;
	Context context;

	public ChitChatCustomAdapter(Context context, Message [] messages) {
		super(context,R.layout.custom_otherpeople_messages, new String[messages.length]); // create a string array with the number of units our message array has
		this.messages = messages;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater zunairInflater = LayoutInflater.from(getContext()); 

		View customView;

		SharedPreferences pref = context.getSharedPreferences("MyPersonInformation", Context.MODE_PRIVATE); 

		if(messages[position].getPosterName().equalsIgnoreCase(pref.getString("name", "NotAvailableName"))){
			customView = zunairInflater.inflate(R.layout.custom_owner_messages, parent, false);//Links to XML LAYOUT FILE, don't worry about any thing else

			messageContent = (TextView) customView.findViewById(R.id.custom_owner_message_content); //attach a text view to XML file
			compositionDate = (TextView) customView.findViewById(R.id.custom_owner_message_date);
			posterName = (TextView) customView.findViewById(R.id.custom_owner_person_name);

			messageContent.setText(messages[position].getContentMessage());
			compositionDate.setText(messages[position].getCompositionDate());
			posterName.setText(messages[position].getPosterName());
		}else{
			customView = zunairInflater.inflate(R.layout.custom_otherpeople_messages, parent, false);//Links to XML LAYOUT FILE, don't worry about any thing else
			messageContent = (TextView) customView.findViewById(R.id.custom_other_people_message_content); //attach a text view to XML file
			compositionDate = (TextView) customView.findViewById(R.id.custom_other_people_message_date);
			posterName = (TextView) customView.findViewById(R.id.custom_other_people_person_name);

			messageContent.setText(messages[position].getContentMessage());
			compositionDate.setText(messages[position].getCompositionDate());
			posterName.setText(messages[position].getPosterName());
		}

		return customView; //Return this view
	}


}
