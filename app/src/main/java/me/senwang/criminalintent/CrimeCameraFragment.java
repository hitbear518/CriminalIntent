package me.senwang.criminalintent;

import android.graphics.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CrimeCameraFragment extends Fragment {
	private static final String TAG = CrimeCameraFragment.class.getSimpleName();

	private Camera mCamera;
	private SurfaceView mSurfaceView;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_crime_camera, container, false);

		Button takePictureButton = (Button) v.findViewById(R.id.take_picture_button);
		takePictureButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});

		mSurfaceView = (SurfaceView) v.findViewById(R.id.surface_view);

		return v;
	}
}
