package me.senwang.criminalintent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CrimeListFragment extends ListFragment {

	public interface CallBack {
		public void onCrimeSelected(Crime crime);
	}

	private ArrayList<Crime> mCrimes;
	private boolean mSubtitleVisible;
	private ActionMode mActionMode;
	private CallBack mCallBack;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof CallBack) {
			mCallBack = (CallBack) activity;
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallBack = null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		setRetainInstance(true);
		mSubtitleVisible = false;

		getActivity().setTitle(R.string.crimes_title);
		mCrimes = CrimeLab.get(getActivity()).getCrimes();

		CrimeAdapter adapter = new CrimeAdapter(mCrimes);
		setListAdapter(adapter);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
		if (mSubtitleVisible) {
			actionBar.setSubtitle(R.string.subtitle);
		}

		View v = super.onCreateView(inflater, container, savedInstanceState);
		ListView listView = (ListView) v.findViewById(android.R.id.list);

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			registerForContextMenu(listView);
		} else {
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
				@Override
				public void onItemCheckedStateChanged(android.view.ActionMode mode, int position, long id, boolean checked) {
				}

				@Override
				public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
					mode.setTitle("Test Title");
					MenuInflater inflater = mode.getMenuInflater();
					inflater.inflate(R.menu.crime_list_item_context, menu);
					return true;
				}

				@Override
				public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
					return false;
				}

				@Override
				public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
					switch (item.getItemId()) {
						case R.id.menu_item_delete_crime:
							CrimeAdapter adapter = (CrimeAdapter) getListAdapter();
							CrimeLab crimeLab = CrimeLab.get(getActivity());
							for (int i = adapter.getCount() - 1; i >= 0; i--) {
								if (getListView().isItemChecked(i)) {
									crimeLab.deleteCrime(adapter.getItem(i));
								}
							}
							mode.finish();
							adapter.notifyDataSetChanged();
							return true;
						default:
							return false;
					}
				}

				@Override
				public void onDestroyActionMode(android.view.ActionMode mode) {
				}
			});
		}

		return v;
	}

	@Override
	public void onPause() {
		super.onPause();
		CrimeLab.get(getActivity()).saveCrimes();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Crime c = ((CrimeAdapter) getListAdapter()).getItem(position);
		if (mCallBack != null) {
			mCallBack.onCrimeSelected(c);
		}
	}


	@Override
	public void onResume() {
		super.onResume();
		((CrimeAdapter) getListAdapter()).notifyDataSetChanged();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_crime_list, menu);
		MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
		if (mSubtitleVisible) {
			showSubtitle.setTitle(R.string.hide_subtitle);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_item_new_crime:
				Crime crime = new Crime();
				CrimeLab.get(getActivity()).addCrime(crime);
				((CrimeAdapter) getListAdapter()).notifyDataSetChanged();
				mCallBack.onCrimeSelected(crime);
				return true;
			case R.id.menu_item_show_subtitle:
				ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
				if (actionBar.getSubtitle() == null) {
					actionBar.setSubtitle(R.string.subtitle);
					mSubtitleVisible = true;
					item.setTitle(R.string.hide_subtitle);
				} else {
					actionBar.setSubtitle(null);
					mSubtitleVisible = false;
					item.setTitle(R.string.show_subtitle);
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		int position = info.position;
		CrimeAdapter adapter = (CrimeAdapter) getListAdapter();
		Crime crime = adapter.getItem(position);

		switch (item.getItemId()) {
			case R.id.menu_item_delete_crime:
				CrimeLab.get(getActivity()).deleteCrime(crime);
				adapter.notifyDataSetChanged();
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}

	public void updateUI() {
		((CrimeAdapter) getListAdapter()).notifyDataSetChanged();
	}

	private class CrimeAdapter extends ArrayAdapter<Crime> {

		public CrimeAdapter(ArrayList<Crime> crimes) {
			super(getActivity(), 0, crimes);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater()
						.inflate(R.layout.list_item_crime, parent, false);
			}

			Crime c = getItem(position);

			TextView titleTextView = (TextView) convertView.findViewById(R.id.title_text_view);
			titleTextView.setText(c.getTitle());
			TextView dateTextView = (TextView) convertView.findViewById(R.id.date_text_view);
			dateTextView.setText(c.getDate().toString());
			CheckBox solvedCheckBox = (CheckBox) convertView.findViewById(R.id.solved_check_box);
			solvedCheckBox.setChecked(c.isSolved());

			return convertView;
		}
	}
}
