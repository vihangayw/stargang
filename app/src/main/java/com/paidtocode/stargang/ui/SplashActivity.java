package com.paidtocode.stargang.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

import org.json.JSONException;

public class SplashActivity extends AppCompatActivity {

	private static final String TAG = SplashActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		if (UserSessionManager.getInstance().getExp() == null)
			UserSessionManager.getInstance().creataEXP();
		else {
//			if (!new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date())
//					.equals(UserSessionManager.getInstance().getExp())) {
//				Toast.makeText(this, "Trial Expired !", Toast.LENGTH_SHORT).show();
//				return;
//			}
		}
		if (UserSessionManager.getInstance().isLogin()) {
			if (checkNetwork()) {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						fetchUser();
					}
				}, 50);
			} else {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						startActivity(new Intent(SplashActivity.this, DashbordActivity.class));
						finish();
					}
				}, 1500);
			}
		} else {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					startActivity(new Intent(SplashActivity.this, LoginActivity.class));
					finish();
				}
			}, 1500);
		}
	}

	private void fetchUser() {
		DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("allow");
		myRef.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				// This method is called once with the initial value and again
				// whenever data at this location is updated.
				String value = dataSnapshot.getValue(String.class);
				if (!TextUtils.isEmpty(value) && value.equalsIgnoreCase("1")) {
					new UserRequestHelperImpl().getUserProfile(new APIHelper.PostManResponseListener() {
						@Override
						public void onResponse(Ancestor ancestor) {
							if (ancestor.getStatus() && ancestor instanceof SignUpResponse) {
								Signup data = ((SignUpResponse) ancestor).getData();
								if (data != null) {
									proceedLogin(data);
								}
							}
						}

						@Override
						public void onError(Error error) {
							if (error != null) {
								if (!TextUtils.isEmpty(error.getMessage())) {
									Toast.makeText(SplashActivity.this,
											error.getMessage(), Toast.LENGTH_SHORT).show();
								}
							}
							startActivity(new Intent(SplashActivity.this, DashbordActivity.class));
							finish();
						}
					});
				}
			}

			@Override
			public void onCancelled(DatabaseError error) {
				// Failed to read value
				Log.w(TAG, "Failed to read value.", error.toException());
			}
		});

	}

	private void proceedLogin(Signup signup) {
		try {
			UserSessionManager.getInstance().createUser(JsonService.toJsonNode(signup).toString());
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		startActivity(new Intent(this, DashbordActivity.class));
		finish();
	}

	public boolean checkNetwork() {
		if (isNetworkConnected()) {
			return true;
		}
		Toast.makeText(SplashActivity.this, getString(R.string.no_network), Toast.LENGTH_SHORT).show();
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
