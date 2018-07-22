package com.paidtocode.stargang.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.paidtocode.stargang.R;
import com.paidtocode.stargang.StarGangApplication;
import com.paidtocode.stargang.api.APIHelper;
import com.paidtocode.stargang.api.request.helper.impl.UserRequestHelperImpl;
import com.paidtocode.stargang.api.response.Ancestor;
import com.paidtocode.stargang.api.response.Error;
import com.paidtocode.stargang.api.response.SignUPListResponse;
import com.paidtocode.stargang.modal.Register;
import com.paidtocode.stargang.modal.Signup;
import com.paidtocode.stargang.util.UtilityManager;

import java.util.List;

public class RegisterDetailActivity extends AppCompatActivity {

	private TextView txtEmail;
	private TextView txtPassword;
	private TextView txtConfirmPassword;
	private Button btnSubmit;
	private String mobileNo;

	public static boolean isValidEmail(CharSequence target) {
		return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_detail);

		initializeViews();
		setListeners();
	}

	private void initializeViews() {
		txtEmail = findViewById(R.id.txt_email);
		txtPassword = findViewById(R.id.txt_pw);
		txtConfirmPassword = findViewById(R.id.txt_cpw);
		btnSubmit = findViewById(R.id.btn_register);
		Bundle extras = getIntent().getExtras();
		if (extras != null)
			mobileNo = extras.getString("mobile_number");
	}

	private void setListeners() {
		btnSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (valid()) {
					registerUser();
				}
			}
		});
	}

	private void registerUser() {
		if (checkNetwork()) {
			final ProgressDialog progressDialog = UtilityManager.showProgressAlert(this, getString(R.string.wait));
			new UserRequestHelperImpl().registerUser(new Register(txtEmail.getText().toString().trim(),
							txtPassword.getText().toString().trim(),
							txtConfirmPassword.getText().toString().trim(),
							"2",
							mobileNo),
					new APIHelper.PostManResponseListener() {
						@Override
						public void onResponse(Ancestor ancestor) {
							progressDialog.dismiss();
							if (ancestor.getStatus() && ancestor instanceof SignUPListResponse) {
								List<Signup> data = ((SignUPListResponse) ancestor).getData();
								if (data != null && !data.isEmpty()) {
									Intent intent = new Intent(RegisterDetailActivity.this, LoginActivity.class);
									intent.putExtra("email", txtEmail.getText().toString().trim());
									startActivity(intent);
									finish();
								}
							}
						}

						@Override
						public void onError(Error error) {
							progressDialog.dismiss();
							if (error != null) {
								if (!TextUtils.isEmpty(error.getMessage())) {
									Toast.makeText(RegisterDetailActivity.this,
											error.getMessage(), Toast.LENGTH_SHORT).show();
								}
							}
						}
					});
		}
	}

	private boolean valid() {
		if (TextUtils.isEmpty(txtEmail.getText().toString().trim())) {
			txtEmail.setError("Enter email");
			return false;
		}

		if (!isValidEmail(txtEmail.getText().toString().trim())) {
			txtEmail.setError("Invalid email address");
			return false;
		}

		if (TextUtils.isEmpty(txtPassword.getText().toString().trim())) {
			txtPassword.setError("Enter Password");
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
