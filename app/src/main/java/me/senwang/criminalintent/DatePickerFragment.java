package me.senwang.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {

	public static final String EXTRA_DATE = "me.senwang.criminalintent.DATE";

	public static DatePickerFragment newInstance(Date date) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_DATE, date);

		DatePickerFragment fragment = new DatePickerFragment();
		fragment.setArguments(args);

		return fragment;
	}

	private Date mDate;

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mDate = (Date) getArguments().getSerializable(EXTRA_DATE);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(mDate);
		int year = calendar.get(Calendar.YEAR);
		final int month = calendar.get(Calendar.MONTH);
		final int day = calendar.get(Calendar.DAY_OF_MONTH);

		View v = View.inflate(getActivity(), R.layout.dialog_date, null);

		DatePicker datePicker = (DatePicker) v.findViewById(R.id.date_picker);
		datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				mDate = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();

				getArguments().putSerializable(EXTRA_DATE, mDate);
			}
		});

		return new AlertDialog.Builder(getActivity())
				.setTitle(R.string.date_picker_title)
				.setView(v)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						sendResult(Activity.RESULT_OK);
					}
				})
				.create();
	}

	private void sendResult(int resultCode) {
		if (getTargetFragment() == null) return;

		Intent i = new Intent();
		i.putExtra(EXTRA_DATE, mDate);

		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
	}
}
