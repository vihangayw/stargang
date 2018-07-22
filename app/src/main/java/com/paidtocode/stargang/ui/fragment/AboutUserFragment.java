package com.paidtocode.stargang.ui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paidtocode.stargang.R;
import com.paidtocode.stargang.modal.Signup;
import com.paidtocode.stargang.util.UserSessionManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AboutUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutUserFragment extends Fragment {

	private TextView txtName, txtEmail, txtInfo;

	public AboutUserFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment EditUserFragment.
	 */
	public static AboutUserFragment newInstance() {
		AboutUserFragment fragment = new AboutUserFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_about_user, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initViews(view);
	}

	private void initViews(View view) {
		txtName = view.findViewById(R.id.txt_name);
		txtEmail = view.findViewById(R.id.txt_email);
		txtInfo = view.findViewById(R.id.txt_info);
		showUserData();
	}

	private void showUserData() {
		Signup user = UserSessionManager.getInstance().getUser();
		if (user != null) {
			txtName.setText(!TextUtils.isEmpty(user.getFullName()) ? user.getFullName() : "");
			txtInfo.setText(!TextUtils.isEmpty(user.getInfo()) ? user.getInfo() : "");
			txtEmail.setText(!TextUtils.isEmpty(user.getEmail()) ? user.getEmail() : "");
		}
	}

}
