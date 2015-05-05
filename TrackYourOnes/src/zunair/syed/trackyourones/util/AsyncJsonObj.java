package zunair.syed.trackyourones.util;



import android.app.ProgressDialog;
import android.os.AsyncTask;


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
