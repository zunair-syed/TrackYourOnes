package zunair.syed.trackyourones.util;

import android.content.Context;

/* Allows inner classes to have code running on Asyncronous tasks*/
public interface IASyncExecutable {
	
	public void logicOnPreExecute();
	public void logicWhileInBackground();
	public void logicOnPostExecute();	
	public Context getContextOfClass();	

}
