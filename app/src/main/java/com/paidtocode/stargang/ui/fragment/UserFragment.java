package com.paidtocode.stargang.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.paidtocode.stargang.BuildConfig;
import com.paidtocode.stargang.R;
import com.paidtocode.stargang.modal.Signup;
import com.paidtocode.stargang.modal.UserType;
import com.paidtocode.stargang.ui.ProfileActivity;
import com.paidtocode.stargang.util.UserSessionManager;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {

	private TextView txtName;
	private ImageView imgUser;
	private View btnChangePw, btnHelp, btnEditProfile;

	public UserFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment UserFragment.
	 */
	public static UserFragment newInstance() {
		UserFragment fragment = new UserFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_user, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initViews(view);
		setListeners();
	}

	private void setListeners() {
		btnChangePw.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

			}
		});
		btnHelp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

			}
		});
		btnEditProfile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (getActivity() != null)
					startActivity(new Intent(getActivity(), ProfileActivity.class));
			}
		});
	}

	private void initViews(View view) {
		TextView txtVersion = view.findViewById(R.id.txt_version);
		txtVersion.setText(BuildConfig.VERSION_NAME);
		txtName = view.findViewById(R.id.txt_name);
		imgUser = view.findViewById(R.id.img_user);
		btnChangePw = view.findViewById(R.id.btn_change_pw);
		btnHelp = view.findViewById(R.id.btn_help_sup);
		btnEditProfile = view.findViewById(R.id.btn_edit);
		showUserData();
	}

	private void showUserData() {
		Signup user = UserSessionManager.getInstance().getUser();
		if (user != null) {
			txtName.setText(!TextUtils.isEmpty(user.getFullName()) ? user.getFullName() : "");
			List<UserType> type = user.getType();
			if (type != null && !type.isEmpty()) {
				UserType userType = type.get(0);
				if (userType != null && !TextUtils.isEmpty(userType.getUserType())
						&& TextUtils.equals(userType.getUserType(), "1")) {
					if (!TextUtils.isEmpty(user.getcImage()))
						Glide.with(this)
								.load(user.getcImage())
								.centerCrop()
								.diskCacheStrategy(DiskCacheStrategy.ALL)
								.into(imgUser);
				}

			}

		}
	}
}
