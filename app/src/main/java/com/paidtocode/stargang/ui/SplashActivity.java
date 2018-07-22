package com.paidtocode.stargang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.paidtocode.stargang.R;
import com.paidtocode.stargang.util.UserSessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		if (UserSessionManager.getInstance().getExp() == null)
			UserSessionManager.getInstance().creataEXP();
		else {
			if (!new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date())
					.equals(UserSessionManager.getInstance().getExp())) {
				Toast.makeText(this, "Trial Expired !", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		if (UserSessionManager.getInstance().isLogin()) {
			startActivity(new Intent(this, DashbordActivity.class));
			finish();
		} else {
			startActivity(new Intent(this, LoginActivity.class));
			finish();
		}
	}
}
