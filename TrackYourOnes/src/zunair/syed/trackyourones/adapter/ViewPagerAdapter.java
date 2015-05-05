package zunair.syed.trackyourones.adapter;

import zunair.syed.trackyourones.R;
import zunair.syed.trackyourones.fragment.ChitChat;
import zunair.syed.trackyourones.fragment.PeopleChecker;
import zunair.syed.trackyourones.fragment.Settings;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;

public class ViewPagerAdapter extends FragmentPagerAdapter {

	final int PAGE_COUNT = 3;

	//images id array
	final private int[] imageResId = {
			R.drawable.gear,
			R.drawable.person,
			R.drawable.globe
	};
	
	Context context;

	public ViewPagerAdapter(FragmentManager fm, Context context) {
		super(fm);
		this.context = context;
	}

	@Override
	public int getCount() {
		return PAGE_COUNT;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {

		// Open FragmentTab1.java
		case 0:
			Settings fragmenttab1 = new Settings();
			return fragmenttab1;

			// Open FragmentTab2.java
		case 1:
			PeopleChecker fragmenttab2 = new PeopleChecker(context);
			return fragmenttab2;

			// Open FragmentTab3.java
		case 2:
			ChitChat fragmenttab3 = new ChitChat(context);
			return fragmenttab3;
		}
		return null;
	}


	@Override
	public CharSequence getPageTitle(int position) {
		Drawable image = context.getResources().getDrawable(imageResId[position]);
		image.setBounds(0, 0, getPixelsFromPercentScreen(0.1, "Width"), getPixelsFromPercentScreen(0.08, "Height"));//was 200f
		SpannableString sb = new SpannableString(" ");
		ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
		sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return sb;
	}

	public int convertPixelsToDp(float px){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float dp = px / (metrics.densityDpi / 160f);
	    return (int)dp;
	}

	public int getPixelsFromPercentScreen(double percentScreen, String widthOrHeight){
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		
		if(widthOrHeight.equalsIgnoreCase("Width")){
			 return (int) (metrics.widthPixels * percentScreen);
		}else if(widthOrHeight.equalsIgnoreCase("Height")){
			 return (int) (metrics.heightPixels * percentScreen);
		}else{
			return -1; //something went wrong
		}
	}



}