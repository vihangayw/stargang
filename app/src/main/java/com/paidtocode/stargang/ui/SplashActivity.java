package com.paidtocode.stargang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.paidtocode.stargang.R;
import com.paidtocode.stargang.util.UserSessionManager;

public class SplashActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		if (UserSessionManager.getInstance().isLogin()) {
			startActivity(new Intent(this, DashbordActivity.class));
			finish();
		} else {
			startActivity(new Intent(this, LoginActivity.class));
			finish();
		}
	}
}
