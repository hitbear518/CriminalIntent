package me.senwang.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

public class CrimeLab {

	private static CrimeLab sCrimeLab;

	public static CrimeLab get(Context c) {
		if (sCrimeLab == null) {
			sCrimeLab = new CrimeLab(c.getApplicationContext());
		}
		return  sCrimeLab;
	}

	private Context mAppContext;
	private ArrayList<Crime> mCrimes;

	private CrimeLab(Context appContext) {
		mAppContext = appContext;
		mCrimes = new ArrayList<Crime>();
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
}
