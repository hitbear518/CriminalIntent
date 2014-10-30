package me.senwang.criminalintent;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.CallBack, CrimeFragment.CallBack {

	@Override
	protected int getLayoutResId() {
		return R.layout.activity_master_detail;
	}

	@Override
	protected Fragment createFragment() {
		return new CrimeListFragment();
	}

	@Override
	public void onCrimeSelected(Crime crime) {

		if (findViewById(R.id.detail_fragment_container) != null) {
			CrimeFragment crimeFragment = CrimeFragment.newInstance(crime.getId());
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.detail_fragment_container, crimeFragment)
					.commit();
		} else {
			Intent i = new Intent(this, CrimePagerActivity.class);
			i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
			startActivity(i);
		}
	}

	@Override
	public void onCrimeUpdated(Crime crime) {
		FragmentManager fm = getSupportFragmentManager();
		CrimeListFragment listFragment = (CrimeListFragment) fm.findFragmentById(R.id.fragment_container);
		listFragment.updateUI();
	}
}
