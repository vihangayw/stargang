package com.paidtocode.stargang.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.plus.PlusOneButton;
import com.paidtocode.stargang.R;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link EndFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EndFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EndFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	// The request code must be 0 or greater.
	private static final int PLUS_ONE_REQUEST_CODE = 0;
	// The URL to +1.  Must be a valid URL.
	private final String PLUS_ONE_URL = "http://developer.android.com";
	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	private PlusOneButton mPlusOneButton;


	public EndFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment EndFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static EndFragment newInstance() {
		EndFragment fragment = new EndFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_end, container, false);

		//Find the +1 button
		mPlusOneButton = (PlusOneButton) view.findViewById(R.id.plus_one_button);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();

		// Refresh the state of the +1 button each time the activity receives focus.
		mPlusOneButton.initialize(PLUS_ONE_URL, PLUS_ONE_REQUEST_CODE);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}


}
