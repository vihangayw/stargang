package com.paidtocode.stargang.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.paidtocode.stargang.R;
import com.paidtocode.stargang.util.TextDrawable;
import com.paidtocode.stargang.util.UserSessionManager;
import com.paidtocode.stargang.util.UtilityManager;

import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {

	private static final String TAG = RegisterActivity.class.getSimpleName();
	private String code = "+94";
	private TextView txtNumber;
	private Button btnSubmit;
	private View viewLogin;
	private AlertDialog progressAlert;
	private AlertDialog alertDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		initializeViews();
		setListeners();
	}

	public static boolean isValidMobile(CharSequence target) {
		return target != null && target.length() == 9;
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

	private void initializeViews() {
		txtNumber = findViewById(R.id.txt_mobile);
		btnSubmit = findViewById(R.id.btn_submit);
		viewLogin = findViewById(R.id.layout_account);

		txtNumber.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable(code, this), null, null, null);
		txtNumber.setCompoundDrawablePadding((int) (code.length() * getResources().getDimension(R.dimen._9sdp)));
	}

	private void registerUser() {
		progressAlert = UtilityManager.showProgressAlert(this, "Verifying your mobile number");

		PhoneAuthProvider.getInstance().verifyPhoneNumber(
				code + txtNumber.getText().toString().trim(),
				60,
				TimeUnit.SECONDS,
				this,
				new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

					@Override
					public void onVerificationCompleted(PhoneAuthCredential credential) {
						// This callback will be invoked in two situations:
						// 1 - Instant verification. In some cases the phone number can be instantly
						//     verified without needing to send or enter a verification code.
						// 2 - Auto-retrieval. On some devices Google Play services can automatically
						//     detect the incoming verification SMS and perform verificaiton without
						//     user action.
						Log.d(TAG, "onVerificationCompleted:" + credential);
						progressAlert.setTitle("Verification completed, signing in");
						signInWithPhoneAuthCredential(credential);
					}

					@Override
					public void onVerificationFailed(FirebaseException e) {
						// This callback is invoked in an invalid request for verification is made,
						// for instance if the the phone number format is not valid.
						Log.w(TAG, "onVerificationFailed", e);

//                            if (e instanceof FirebaseAuthInvalidCredentialsException) {
//                                // Invalid request
//                                // ...
//                            } else if (e instanceof FirebaseTooManyRequestsException) {
//                                // The SMS quota for the project has been exceeded
//                                // ...
//                            }
						progressAlert.dismiss();
						DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								alertDialog.dismiss();
							}
						};
						alertDialog = UtilityManager.showAlert(RegisterActivity.this, null,
								e.getLocalizedMessage(),
								"OK", "", false,
								listener, listener);
					}

					@Override
					public void onCodeSent(String verificationId,
					                       PhoneAuthProvider.ForceResendingToken token) {
						// The SMS verification code has been sent to the provided phone number, we
						// now need to ask the user to enter the code and then construct a credential
						// by combining the code with a verification ID.
						UserSessionManager.getInstance().createAuthRetry(verificationId, token.toString());
						Intent intent = new Intent(RegisterActivity.this, MobileVerificationActivity.class);
						intent.putExtra("mobile_number", code + txtNumber.getText().toString().trim());
						startActivity(intent);
						progressAlert.dismiss();
						finish();
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
							progressAlert.setTitle("Finishing");
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
							alertDialog = UtilityManager.showAlert(RegisterActivity.this, null,
									task.getException() != null
											? task.getException().getLocalizedMessage()
											: "Mobile verific.ation Failed",
									"OK", "", false,
									listener, listener);
						}
					}
				});
	}

	private void startSignUpActivity() {
		Intent intent = new Intent(this, RegisterDetailActivity.class);
		intent.putExtra("mobile_number", code + txtNumber.getText().toString().trim());
		startActivity(intent);
		progressAlert.dismiss();
		finish();
	}

	private boolean valid() {
		if (TextUtils.isEmpty(txtNumber.getText().toString().trim())) {
			txtNumber.setError("Enter Mobile Number");
			return false;
		}
		if (!isValidMobile(txtNumber.getText().toString().trim())) {
			txtNumber.setError("Invalid Mobile Number");
			return false;
		}
		return true;
	}
}
