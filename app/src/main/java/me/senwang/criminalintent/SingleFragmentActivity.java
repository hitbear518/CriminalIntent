package me.senwang.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;

public abstract class SingleFragmentActivity extends ActionBarActivity{

	protected int getLayoutResId() {
		return R.layout.activity_fragment;
	}

	protected abstract Fragment createFragment();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayoutResId());

		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		Log.d(Utils.getTag(), "Height in pixels: " + displayMetrics.heightPixels);
		Log.d(Utils.getTag(), "Width in pixels: " + displayMetrics.widthPixels);
		Log.d(Utils.getTag(), "Density: " + displayMetrics.density);
		Log.d(Utils.getTag(), "Density in dpi: " + displayMetrics.densityDpi);
		Log.d(Utils.getTag(), "Width in dpi: " + displayMetrics.widthPixels / displayMetrics.density);

		FragmentManager fm = getSupportFragmentManager();

		Fragment fragment = fm.findFragmentById(R.id.fragment_container);
		if (fragment == null) {
			fragment = createFragment();
			fm.beginTransaction()
					.add(R.id.fragment_container, fragment)
					.commit();
		}
	}
}
