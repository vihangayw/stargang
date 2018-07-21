package com.paidtocode.stargang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.paidtocode.stargang.R;

public class RegisterActivity extends AppCompatActivity {

	private TextView txtNumber;
	private Button btnSubmit;
	private View viewLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		initializeViews();
		setListeners();
	}

	private void initializeViews() {
		txtNumber = findViewById(R.id.txt_mobile);
		btnSubmit = findViewById(R.id.btn_submit);
		viewLogin = findViewById(R.id.layout_account);
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
		viewLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
				finish();
			}
		});
	}

	private void registerUser() {

	}

	private boolean valid() {
		if (TextUtils.isEmpty(txtNumber.getText().toString().trim())) {
			txtNumber.setError("Enter Mobile Number");
			return false;
		}
		return true;
	}
}
