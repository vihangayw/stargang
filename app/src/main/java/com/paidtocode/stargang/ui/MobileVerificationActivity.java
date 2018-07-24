package com.paidtocode.stargang.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.glomadrian.codeinputlib.CodeInput;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.paidtocode.stargang.R;
import com.paidtocode.stargang.util.UserSessionManager;
import com.paidtocode.stargang.util.UtilityManager;

public class MobileVerificationActivity extends AppCompatActivity {

	private String TAG = getClass().getSimpleName();
	private AlertDialog alertDialog;
	private AlertDialog progressAlert;
	private CodeInput txtNumber;
	private Button btnSubmit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mobile_verification);

		initializeViews();
		setListeners();
	}

	private void initializeViews() {
		txtNumber = findViewById(R.id.txt_mobile);
		btnSubmit = findViewById(R.id.btn_submit);
	}

	private void setListeners() {
		btnSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Character[] code = txtNumber.getCode();
				if (code.length == 6) {
					progressAlert = UtilityManager.showProgressAlert(MobileVerificationActivity.this,
							"Verifying your mobile number");
					signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(
							UserSessionManager.getInstance().getVerificationId(),
							code[0] + "" + code[1] + "" + code[2] + "" + code[3] + "" + code[4] + "" + code[5] + ""));
				} else {
					Toast.makeText(MobileVerificationActivity.this, "Invalid Code !", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
		FirebaseAuth.getInstance().signInWithCredential(credential)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							// Sign in success, update UI with the signed-in user's information
							Log.d(TAG, "signInWithCredential:success");

							FirebaseUser user = task.getResult().getUser();
							UserSessionManager.getInstance().createFirebaseUid(user.getUid());
							progressAlert.setTitle("Verifying your mobile number");
							startSignUpActivity();
							// ...
						} else {
							// Sign in failed, display a message and update the UI
							Log.w(TAG, "signInWithCredential:failure", task.getException());
							if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
								// The verification code entered was invalid
							}
							progressAlert.dismiss();
							DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialogInterface, int i) {
									alertDialog.dismiss();
								}
							};
							alertDialog = UtilityManager.showAlert(MobileVerificationActivity.this, null,
									task.getException() != null
											? task.getException().getLocalizedMessage()
											: "Number verification fail",
									"OK", "", false,
									listener, listener);
						}
					}
				});
	}

	private void startSignUpActivity() {
		Character[] code = txtNumber.getCode();
		Intent intent = new Intent(this, RegisterDetailActivity.class);
		intent.putExtra("mobile_number", "+94" + code[0] + "" + code[1] + "" + code[2] + "" + code[3] + "" + code[4] + "" + code[5] + "");
		startActivity(intent);
		progressAlert.dismiss();
		finish();
	}

}
