package zunair.syed.trackyourones.activity;

import zunair.syed.trackyourones.R;
import zunair.syed.trackyourones.adapter.ViewPagerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class MainDashboard extends FragmentActivity {

	Bundle mSavedInstanceState;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSavedInstanceState = savedInstanceState;

		setContentView(R.layout.main_dashboard);

		// Locate the viewpager in activity_main.xml
		ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
		
		// Set the ViewPagerAdapter into ViewPager
		viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), this));
		viewPager.setCurrentItem(1); //default tab is people checker tab
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//do nothing for now
	}
	
}