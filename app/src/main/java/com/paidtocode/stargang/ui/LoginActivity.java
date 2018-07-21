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

import com.paidtocode.stargang.NFCApplication;
import com.paidtocode.stargang.R;
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

public class LoginActivity extends AppCompatActivity {

	private TextView txtEmail;
	private TextView txtPassword;
	private Button btnSubmit;
	private View viewRegister;

	public static boolean isValidEmail(CharSequence target) {
		return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		initializeViews();
		setListeners();
	}

	private void initializeViews() {
		txtEmail = findViewById(R.id.txt_email);
		txtPassword = findViewById(R.id.txt_pw);
		btnSubmit = findViewById(R.id.btn_register);
		viewRegister = findViewById(R.id.layout_account);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			if (!TextUtils.isEmpty(extras.getString("email"))) {
				txtEmail.setText(extras.getString("email"));
			}
		}
	}

	private void setListeners() {
		btnSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (valid()) {
					loginUser();
				}
			}
		});
		viewRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
				finish();
			}
		});
	}

	private void loginUser() {
		if (checkNetwork()) {
			final ProgressDialog progressDialog = UtilityManager.showProgressAlert(this, getString(R.string.wait));
			new UserRequestHelperImpl().loginUser(txtEmail.getText().toString().trim(),
					txtPassword.getText().toString().trim(),
					new APIHelper.PostManResponseListener() {
						@Override
						public void onResponse(Ancestor ancestor) {
							progressDialog.dismiss();
							if (ancestor.getStatus() && ancestor instanceof SignUpResponse) {
								Signup data = ((SignUpResponse) ancestor).getData();
								if (data != null) {
									proceedLogin(data);
								}
							}
						}

						@Override
						public void onError(Error error) {
							progressDialog.dismiss();
							if (error != null) {
								if (!TextUtils.isEmpty(error.getMessage())) {
									Toast.makeText(LoginActivity.this,
											error.getMessage(), Toast.LENGTH_SHORT).show();
								}
							}
						}
					});
		}
	}

	private void proceedLogin(Signup signup) {
		try {
			UserSessionManager.getInstance().createUser(JsonService.toJsonNode(signup).toString());
			UserSessionManager.getInstance().createToken(signup.getToken());
			startActivity(new Intent(LoginActivity.this, DashbordActivity.class));
			finish();
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	private boolean valid() {
		if (TextUtils.isEmpty(txtEmail.getText().toString().trim())) {
			txtEmail.setError("Enter email");
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
				(ConnectivityManager) NFCApplication.getInstance().getApplicationContext()
						.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
			return activeNetwork != null &&
					activeNetwork.isConnectedOrConnecting();
		}
		return false;
	}
}
