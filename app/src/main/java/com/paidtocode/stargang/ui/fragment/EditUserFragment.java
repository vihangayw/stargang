package com.paidtocode.stargang.ui.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.paidtocode.stargang.R;
import com.paidtocode.stargang.StarGangApplication;
import com.paidtocode.stargang.api.APIHelper;
import com.paidtocode.stargang.api.request.helper.impl.UserRequestHelperImpl;
import com.paidtocode.stargang.api.response.Ancestor;
import com.paidtocode.stargang.api.response.Error;
import com.paidtocode.stargang.api.response.SignUpResponse;
import com.paidtocode.stargang.modal.Signup;
import com.paidtocode.stargang.util.JsonService;
import com.paidtocode.stargang.util.UserSessionManager;
import com.paidtocode.stargang.util.UtilityManager;

import org.json.JSONException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditUserFragment extends Fragment {

	private EditText txtName, txtEmail, txtInfo;
	private View btnSave;

	public EditUserFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment EditUserFragment.
	 */
	public static EditUserFragment newInstance() {
		EditUserFragment fragment = new EditUserFragment();
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
		return inflater.inflate(R.layout.fragment_edit_user, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initViews(view);
		setListeners();
	}

	private void initViews(View view) {
		txtName = view.findViewById(R.id.txt_name);
		txtEmail = view.findViewById(R.id.txt_email);
		txtInfo = view.findViewById(R.id.txt_info);
		btnSave = view.findViewById(R.id.btn_save);
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

	private void setListeners() {
		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (valid()) {
					saveProfile();
				}
			}
		});
	}

	private void saveProfile() {
		if (checkNetwork()) {
			final ProgressDialog progressDialog = UtilityManager.showProgressAlert(getContext(), getString(R.string.wait));
			new UserRequestHelperImpl().editProfile(txtName.getText().toString().trim(),
					txtInfo.getText().toString().trim(), new APIHelper.PostManResponseListener() {
						@Override
						public void onResponse(Ancestor ancestor) {
							progressDialog.dismiss();
							if (ancestor.getStatus() && ancestor instanceof SignUpResponse) {
								Signup data = ((SignUpResponse) ancestor).getData();
								if (data != null)
									proceedLogin(data);
							}
						}

						@Override
						public void onError(Error error) {
							progressDialog.dismiss();
							if (error != null) {
								if (!TextUtils.isEmpty(error.getMessage())) {
									Toast.makeText(getContext(),
											error.getMessage(), Toast.LENGTH_SHORT).show();
								}
							}
						}
					});
		}
	}

	private void proceedLogin(Signup signup) {
		try {
			Signup user = UserSessionManager.getInstance().getUser();
			user.setFullName(signup.getFullName());
			user.setInfo(signup.getInfo());
			user.setcImage(signup.getcImage());
			user.setImage(signup.getImage());
			UserSessionManager.getInstance().createUser(JsonService.toJsonNode(user).toString());
			if (getActivity() != null)
				Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private boolean valid() {
		if (TextUtils.isEmpty(txtName.getText().toString().trim())) {
			txtName.setError("Name Cannot be empty");
			return false;
		}
		if (TextUtils.isEmpty(txtInfo.getText().toString().trim())) {
			txtInfo.setError("Info Cannot be empty");
			return false;
		}
		return true;
	}

	public boolean checkNetwork() {
		if (isNetworkConnected()) {
			return true;
		}
		if (getActivity() != null)
			Toast.makeText(getActivity(), getString(R.string.no_network), Toast.LENGTH_SHORT).show();
		return false;
	}

	public boolean isNetworkConnected() {
		ConnectivityManager cm =
				(ConnectivityManager) StarGangApplication.getInstance().getApplicationContext()
						.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
			return activeNetwork != null &&
					activeNetwork.isConnectedOrConnecting();
		}
		return false;
	}
}
