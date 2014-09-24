package me.senwang.criminalintent;

import android.content.Context;

import org.json.JSONArray;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

public class CriminalIntentJSONSerializer {

	private Context mContext;
	private String mFilename;

	public CriminalIntentJSONSerializer(Context context, String filename) {
		mContext = context;
		mFilename = filename;
	}

	public void saveCrimes(ArrayList<Crime> crimes) throws IOException {
		JSONArray array = new JSONArray();
		for (Crime c : crimes) {
			array.put(c.toJSON());
		}

		Writer writer = null;
		try {
			OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
			writer = new OutputStreamWriter(out);
			writer.write(array.toString());
		} finally {
			if (writer != null) writer.close();
		}
	}
}
