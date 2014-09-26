package me.senwang.criminalintent;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class CrimeLab {
	private static final String TAG = CrimeLab.class.getSimpleName();
	private static final String FILE_NAME = "crimes.json";

	private static CrimeLab sCrimeLab;

	public static CrimeLab get(Context c) {
		if (sCrimeLab == null) {
			sCrimeLab = new CrimeLab(c.getApplicationContext());
		}
		return  sCrimeLab;
	}

	private Context mAppContext;
	private ArrayList<Crime> mCrimes;
	private CriminalIntentJSONSerializer mSerializer;

	private CrimeLab(Context appContext) {
		mAppContext = appContext;
		mSerializer = new CriminalIntentJSONSerializer(appContext, FILE_NAME);

		try {
			mCrimes = mSerializer.loadCrimes();
		} catch (Exception e) {
			mCrimes = new ArrayList<Crime>();
			Log.e(TAG, "Error loading crimes: ", e);
		}
	}

	public ArrayList<Crime> getCrimes() {
		return  mCrimes;
	}

	public Crime getCrime(UUID id) {
		for (Crime c : mCrimes) {
			if (c.getId().equals(id)) return c;
		}
		return  null;
	}

	public void addCrime(Crime c) {
		mCrimes.add(c);
	}

	public void deleteCrime(Crime c) {
		mCrimes.remove(c);
	}

	public boolean saveCrimes() {
		try {
			mSerializer.saveCrimes(mCrimes);
			Log.d(TAG, "crimes saved to file");
			return true;
		} catch (Exception e) {
			Log.e(TAG, "Error saving crimes: ", e);
			return false;
		}
	}
}
