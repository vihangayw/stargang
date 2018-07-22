package com.paidtocode.stargang.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.paidtocode.stargang.R;
import com.paidtocode.stargang.StarGangApplication;
import com.paidtocode.stargang.api.APIHelper;
import com.paidtocode.stargang.api.request.helper.impl.UserRequestHelperImpl;
import com.paidtocode.stargang.api.response.Ancestor;
import com.paidtocode.stargang.api.response.Error;
import com.paidtocode.stargang.util.UtilityManager;

public class PasswordResetActivity extends AppCompatActivity {

	private EditText txtConfirmPassword, txtPassword, txtOPW;
	private View btnSave, btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_reset);
		initViews();
		setListeners();
	}

	private void setListeners() {
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onBackPressed();
			}
		});
		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (valid()) {
					changePW();
				}
			}
		});
	}

	private void changePW() {
		if (checkNetwork()) {
			final ProgressDialog progressDialog = UtilityManager.showProgressAlert(this, getString(R.string.wait));
			new UserRequestHelperImpl().changePW(txtOPW.getText().toString(), txtPassword.getText().toString(),
					new APIHelper.PostManResponseListener() {
						@Override
						public void onResponse(Ancestor ancestor) {
							progressDialog.dismiss();
							if (ancestor.getStatus()) {
								StarGangApplication.getInstance().restartApplication(true);
							}
						}

						@Override
						public void onError(Error error) {
							progressDialog.dismiss();
							if (error != null) {
								if (!TextUtils.isEmpty(error.getMessage())) {
									Toast.makeText(PasswordResetActivity.this,
											error.getMessage(), Toast.LENGTH_SHORT).show();
								}
							}
						}
					});
		}
	}

	private boolean valid() {
		if (TextUtils.isEmpty(txtOPW.getText().toString().trim())) {
			txtOPW.setError("Enter Current Password");
			return false;
		}
		if (TextUtils.isEmpty(txtPassword.getText().toString().trim())) {
			txtPassword.setError("Enter New Password");
			return false;
		}
		if (txtPassword.getText().toString().trim().length() < 8) {
			txtPassword.setError("Invalid Password");
			Toast.makeText(this, "Password must contain at least 8 characters",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (TextUtils.isEmpty(txtConfirmPassword.getText().toString().trim())) {
			txtConfirmPassword.setError("Confirm Password");
			return false;
		}
		if (!TextUtils.equals(txtConfirmPassword.getText().toString().trim(),
				txtPassword.getText().toString().trim())) {
			Toast.makeText(this, "Passwords does not match",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	private void initViews() {
		TextView txtTitle = findViewById(R.id.title);
		txtTitle.setText(R.string.reset_pw);
		txtConfirmPassword = findViewById(R.id.txt_cpw);
		txtPassword = findViewById(R.id.txt_pw);
		txtOPW = findViewById(R.id.txt_opw);
		btnSave = findViewById(R.id.btn_nav);
		btnBack = findViewById(R.id.btn_back);
	}

	public boolean checkNetwork() {
		if (isNetworkConnected()) {
			return true;
		}
		Toast.makeText(this, getString(R.string.no_network), Toast.LENGTH_SHORT).show();
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
