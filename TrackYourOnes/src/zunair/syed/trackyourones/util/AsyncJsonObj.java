package zunair.syed.trackyourones.util;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;






import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Async task class to get json by making HTTP call
 * */
public class AsyncJsonObj  extends AsyncTask<Void, Void, Void> {

	private ProgressDialog pDialog;
	private boolean showProgressDialog;
	IASyncExecutable parentClassCaller;


	public AsyncJsonObj(String message, Object parentClassCaller, boolean showProgressDialog) {
		this.parentClassCaller = (IASyncExecutable) parentClassCaller;
		this.showProgressDialog = showProgressDialog;
		
		if(showProgressDialog){
		pDialog = new ProgressDialog(this.parentClassCaller.getContextOfClass());
		pDialog.setMessage(message);
		pDialog.setCancelable(false);
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		if(showProgressDialog){
		pDialog.show();
		}
		
		parentClassCaller.logicOnPreExecute();;
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		parentClassCaller.logicWhileInBackground();
		return null;
	}


	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);

		if(showProgressDialog){
		// Dismiss the progress dialog
		if (pDialog.isShowing())
		pDialog.dismiss();
		}

		parentClassCaller.logicOnPostExecute();

	}

}
