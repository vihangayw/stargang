package com.paidtocode.stargang.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.paidtocode.stargang.R;
import com.paidtocode.stargang.StarGangApplication;
import com.paidtocode.stargang.api.APIHelper;
import com.paidtocode.stargang.api.request.helper.impl.MobitelRequestHelperImpl;
import com.paidtocode.stargang.api.request.helper.impl.UserRequestHelperImpl;
import com.paidtocode.stargang.api.response.Ancestor;
import com.paidtocode.stargang.api.response.Error;
import com.paidtocode.stargang.api.response.MobitelAncestor;
import com.paidtocode.stargang.api.response.SubscriberResponse;
import com.paidtocode.stargang.modal.Signup;
import com.paidtocode.stargang.modal.User;
import com.paidtocode.stargang.modal.UserType;
import com.paidtocode.stargang.ui.adapter.ViewPagerAdapter;
import com.paidtocode.stargang.ui.fragment.AboutUserFragment;
import com.paidtocode.stargang.ui.fragment.EditUserFragment;
import com.paidtocode.stargang.ui.fragment.PhotoFragment;
import com.paidtocode.stargang.util.Constants;
import com.paidtocode.stargang.util.UserSessionManager;
import com.paidtocode.stargang.util.UtilityManager;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

	private final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 12;
	private TextView txtName;
	private TextView txtFollow;
	private TextView txtEdit;
	private View viewEdit;
	private ImageView imgCover;
	private ImageView imgFollow;
	private ImageView imgEdit;
	private ImageView imgProfilePic;
	private TabLayout tabLayout;
	private CoordinatorLayout coordinator;
	private View btnCover, btnImage, overlay;
	private boolean selectCover;
	private boolean isAdmin;
	private boolean isMyProfile;
	private ViewPager viewPager;
	private ViewPagerAdapter adapter;
	private User user;
	private AlertDialog alertDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		String action = getIntent().getAction();
		isMyProfile = !TextUtils.isEmpty(action) && action.equalsIgnoreCase("MyProfile");

		initializeComponents();
		setHookListeners();
	}

	private void initializeComponents() {
		txtName = findViewById(R.id.txt_name);
		txtFollow = findViewById(R.id.txt_follow);
		txtEdit = findViewById(R.id.txt_edit);
		viewEdit = findViewById(R.id.layout_edit);
		imgCover = findViewById(R.id.img_cover);
		imgFollow = findViewById(R.id.img_follow);
		imgEdit = findViewById(R.id.img_edit);
		imgProfilePic = findViewById(R.id.img_user);
		tabLayout = findViewById(R.id.tab_layout_main);
		coordinator = findViewById(R.id.coordinator);
		btnCover = findViewById(R.id.btn_cover);
		btnImage = findViewById(R.id.btn_edit_image);
		overlay = findViewById(R.id.overlay);
		overlay.setVisibility(View.GONE);
		btnImage.setVisibility(View.GONE);
		btnCover.setVisibility(View.GONE);
		showUserData();

		viewPager = findViewById(R.id.viewpager_main);
		adapter = new ViewPagerAdapter(getSupportFragmentManager());
		viewPager.setOffscreenPageLimit(isAdmin ? 3 : 2);
		adapter.addFragment(AboutUserFragment.newInstance(), "About");
		adapter.addFragment(PhotoFragment.newInstance(isMyProfile, user), "Photos");
		if (isAdmin)
			adapter.addFragment(EditUserFragment.newInstance(), "Edit");
		viewPager.setAdapter(adapter);

		tabLayout.setupWithViewPager(viewPager);
		changeTabsFont();
	}

	public boolean checkNetwork() {
		if (isNetworkConnected()) {
			return true;
		}
		Toast.makeText(this, getString(R.string.no_network), Toast.LENGTH_SHORT).show();
		return false;
	}

	private void changeTabsFont() {
		ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
		//selected
		ViewGroup vgTab = (ViewGroup) vg.getChildAt(0);
		for (int i = 0; i < vgTab.getChildCount(); i++) {
			View tabViewChild = vgTab.getChildAt(i);
			if (tabViewChild instanceof TextView) {
				AssetManager mgr = getAssets();
				Typeface tf = Typeface.createFromAsset(mgr, "font/sf_ui_text_bold.otf");//Font file in /assets
				((TextView) tabViewChild).setTypeface(tf);
				//((TextView) tabViewChild).setTextSize(size);
			}
		}
		// unselected
		vgTab = (ViewGroup) vg.getChildAt(1);
		for (int i = 0; i < vgTab.getChildCount(); i++) {
			View tabViewChild = vgTab.getChildAt(i);
			if (tabViewChild instanceof TextView) {
				AssetManager mgr = getAssets();
				Typeface tf = Typeface.createFromAsset(mgr, "font/sf_ui_text_bold.otf");//Font file in /assets
				((TextView) tabViewChild).setTypeface(tf);
				// ((TextView) tabViewChild).setTextSize(size);
			}
		}
		// unselected
		if (isAdmin) {
			vgTab = (ViewGroup) vg.getChildAt(2);
			for (int i = 0; i < vgTab.getChildCount(); i++) {
				View tabViewChild = vgTab.getChildAt(i);
				if (tabViewChild instanceof TextView) {
					AssetManager mgr = getAssets();
					Typeface tf = Typeface.createFromAsset(mgr, "font/sf_ui_text_bold.otf");//Font file in /assets
					((TextView) tabViewChild).setTypeface(tf);
					// ((TextView) tabViewChild).setTextSize(size);

				}
			}
		}
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

	private void setHookListeners() {
		viewEdit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (adapter.getCount() == 3)
					viewPager.setCurrentItem(2);
			}
		});
		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				if (tab.getPosition() == 2) {
					btnCover.setVisibility(View.VISIBLE);
					btnImage.setVisibility(View.VISIBLE);
					overlay.setVisibility(View.VISIBLE);
					txtEdit.setTextColor(ContextCompat.getColor(ProfileActivity.this, R.color.colorGOld));
					imgEdit.setImageDrawable(ContextCompat.getDrawable(ProfileActivity.this, R.drawable.edit_profile_highlight));
				} else {
					btnImage.setVisibility(View.GONE);
					btnCover.setVisibility(View.GONE);
					overlay.setVisibility(View.GONE);
					txtEdit.setTextColor(ContextCompat.getColor(ProfileActivity.this, R.color.colorGray999));
					imgEdit.setImageDrawable(ContextCompat.getDrawable(ProfileActivity.this, R.drawable.edit_profile_icon));
				}
				showUserData();
				Constants.profilePic = null;
				Constants.cover = null;
			}


			@Override
			public void onTabUnselected(TabLayout.Tab tab) {

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});
		btnCover.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				selectCover = true;
				requirePermissions();
			}
		});
		btnImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				selectCover = false;
				requirePermissions();
			}
		});
	}

	private void proceedSubs(final User user) {
		if (!user.isSubscribe()) {
			DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					if (alertDialog != null) alertDialog.dismiss();
				}
			};
			DialogInterface.OnClickListener listenerYes = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					mobitelSubscribe(user);
					if (alertDialog != null) alertDialog.dismiss();
				}
			};
			alertDialog = UtilityManager.showAlert(ProfileActivity.this, null,
					"Do you want to subscribe " + user.getFullName() + "?",
					"Yes", "No", false,
					listenerYes, listener);
		} else {
			DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					if (alertDialog != null) alertDialog.dismiss();
				}
			};
			DialogInterface.OnClickListener listenerYes = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					mobitelUnSubscribe(user);
					if (alertDialog != null) alertDialog.dismiss();
				}
			};
			alertDialog = UtilityManager.showAlert(ProfileActivity.this, null,
					"Do you want to unsubscribe " + user.getFullName() + "?",
					"Yes", "No", false,
					listenerYes, listener);
		}
	}

	private void doUnsubscribe(final User user) {
		if (checkNetwork())
			new UserRequestHelperImpl().doUnsubscribe(user, new APIHelper.PostManResponseListener() {
				@Override
				public void onResponse(Ancestor ancestor) {
					if (ancestor.getStatus()) {
						Toast.makeText(ProfileActivity.this, "Request Sent", Toast.LENGTH_SHORT).show();
						updateUser(user);
					} else {
						Toast.makeText(ProfileActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void onError(Error error) {
					if (error != null) {
						if (!TextUtils.isEmpty(error.getMessage())) {
							Toast.makeText(ProfileActivity.this,
									error.getMessage(), Toast.LENGTH_SHORT).show();
						}
					}
				}
			});
	}

	private void doSubscribe(final User user) {
		if (checkNetwork())
			new UserRequestHelperImpl().doSubscribe(user, new APIHelper.PostManResponseListener() {
				@Override
				public void onResponse(Ancestor ancestor) {
					if (ancestor.getStatus()) {
						Toast.makeText(ProfileActivity.this, "Request Sent", Toast.LENGTH_SHORT).show();
						updateUser(user);
					} else {
						Toast.makeText(ProfileActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void onError(Error error) {
					if (error != null) {
						if (!TextUtils.isEmpty(error.getMessage())) {
							Toast.makeText(ProfileActivity.this,
									error.getMessage(), Toast.LENGTH_SHORT).show();
						}
					}
				}
			});
	}

	private void mobitelSubscribe(final User user) {
		if (checkNetwork())
			try {
				new MobitelRequestHelperImpl().subscribe(UserSessionManager.getInstance().getUser().getuMobile(), new APIHelper.PostManMobitelResponseListener() {
					@Override
					public void onResponse(MobitelAncestor ancestor) {
						if (ancestor instanceof SubscriberResponse) {
							String message = ancestor.getMessage();
							if (!TextUtils.isEmpty(message))
								Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
							if (ancestor.getStatus() == 1000) {
								doSubscribe(user);
							}
						}
					}

					@Override
					public void onError(Error error) {
						if (error != null) {
							if (!TextUtils.isEmpty(error.getMessage())) {
								Toast.makeText(ProfileActivity.this,
										error.getMessage(), Toast.LENGTH_SHORT).show();
							}
						}
					}
				});
			} catch (JSONException e) {
				e.printStackTrace();
			}
	}

	private void mobitelUnSubscribe(final User user) {
		if (checkNetwork())
			try {
				new MobitelRequestHelperImpl().unsubscribe(UserSessionManager.getInstance().getUser().getuMobile(), new APIHelper.PostManMobitelResponseListener() {
					@Override
					public void onResponse(MobitelAncestor ancestor) {
						if (ancestor instanceof SubscriberResponse) {
							String message = ancestor.getMessage();
							if (!TextUtils.isEmpty(message))
								Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
							if (ancestor.getStatus() == 1000) {
								doUnsubscribe(user);
							}
						}
					}

					@Override
					public void onError(Error error) {
						if (error != null) {
							if (!TextUtils.isEmpty(error.getMessage())) {
								Toast.makeText(ProfileActivity.this,
										error.getMessage(), Toast.LENGTH_SHORT).show();
							}
						}
					}
				});
			} catch (JSONException e) {
				e.printStackTrace();
			}
	}

	private void updateUser(User user) {
		user.setSubscribe(!user.isSubscribe());
		if (user.isSubscribe()) {
			txtFollow.setText("Following");
			txtFollow.setTextColor(ContextCompat.getColor(this, R.color.colorAccent_));
			imgFollow.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.following_icon));
		} else {
			txtFollow.setText("Follow");
			txtFollow.setTextColor(ContextCompat.getColor(this, R.color.colorGray999));
			imgFollow.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.follow_icon));
		}
	}

	private void showUserData() {
		if (isMyProfile) {
			Signup user = UserSessionManager.getInstance().getUser();
			if (user != null) {
				txtName.setText(!TextUtils.isEmpty(user.getFullName()) ? user.getFullName() : "");
				List<UserType> type = user.getType();
				if (type != null && !type.isEmpty()) {
					UserType userType = type.get(0);
					if (userType != null && !TextUtils.isEmpty(userType.getIduserType())
							&& TextUtils.equals(userType.getIduserType(), "1")) {
						isAdmin = true;
						if (!TextUtils.isEmpty(user.getImage()))
							Glide.with(this)
									.load(user.getImage())
									.crossFade()
									.diskCacheStrategy(DiskCacheStrategy.ALL)
									.into(imgProfilePic);
						if (!TextUtils.isEmpty(user.getcImage()))
							Glide.with(this)
									.load(user.getcImage())
									.centerCrop()
									.diskCacheStrategy(DiskCacheStrategy.ALL)
									.into(imgCover);
					} else {
						findViewById(R.id.layout_edit).setVisibility(View.GONE);
					}
				}
			}
			findViewById(R.id.layout_follow).setVisibility(View.GONE);
		} else {
			if (getIntent().getExtras() == null) return;
			user = (User) getIntent().getExtras().getSerializable("user");
			if (user != null) {
				Signup signup = UserSessionManager.getInstance().getUser();
				if (user.getId().equalsIgnoreCase(signup.getId())) {
					findViewById(R.id.layout_follow).setVisibility(View.GONE);
					List<UserType> type = user.getType();
					if (type != null && !type.isEmpty()) {
						UserType userType = type.get(0);
						if (userType != null && !TextUtils.isEmpty(userType.getIduserType())
								&& TextUtils.equals(userType.getIduserType(), "1")) {
							isAdmin = true;
						}
					}
				} else {
					findViewById(R.id.layout_edit).setVisibility(View.GONE);
					if (user.isSubscribe()) {
						txtFollow.setText("Following");
						txtFollow.setTextColor(ContextCompat.getColor(this, R.color.colorAccent_));
						imgFollow.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.following_icon));
					} else {
						txtFollow.setText("Follow");
						txtFollow.setTextColor(ContextCompat.getColor(this, R.color.colorGray999));
						imgFollow.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.follow_icon));
					}
					findViewById(R.id.layout_follow).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							proceedSubs(user);
						}
					});
				}

				txtName.setText(!TextUtils.isEmpty(user.getFullName()) ? user.getFullName() : "");
				List<UserType> type = user.getType();
				if (type != null && !type.isEmpty()) {
					if (!TextUtils.isEmpty(user.getImage()))
						Glide.with(this)
								.load(user.getImage())
								.crossFade()
								.diskCacheStrategy(DiskCacheStrategy.ALL)
								.into(imgProfilePic);
					if (!TextUtils.isEmpty(user.getcImage()))
						Glide.with(this)
								.load(user.getcImage())
								.centerCrop()
								.diskCacheStrategy(DiskCacheStrategy.ALL)
								.into(imgCover);
				}

			}
		}
	}

	private void requirePermissions() {
		if (ContextCompat.checkSelfPermission(ProfileActivity.this,
				Manifest.permission.WRITE_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED) {
			if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this,
					Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
				showRequestPermissionAlert();
			} else {
				ActivityCompat.requestPermissions(ProfileActivity.this,
						new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
						MY_PERMISSIONS_REQUEST_READ_STORAGE);
			}
		} else {
			openBottomSheet();
		}
	}

	private void openBottomSheet() {
		CropImage.activity()
				.setGuidelines(CropImageView.Guidelines.ON)
				.start(this);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
			CropImage.ActivityResult result = CropImage.getActivityResult(data);
			if (resultCode == RESULT_OK) {
				Uri resultUri = result.getUri();
				try {
					if (selectCover)
						Constants.cover = MediaStore.Images.Media.getBitmap(ProfileActivity.this.getContentResolver(), resultUri);
					else
						Constants.profilePic = MediaStore.Images.Media.getBitmap(ProfileActivity.this.getContentResolver(), resultUri);
					Glide.with(this)
							.load(resultUri)
							.centerCrop()
							.diskCacheStrategy(DiskCacheStrategy.ALL)
							.into(selectCover ? imgCover : imgProfilePic);
				} catch (IOException e) {
					e.printStackTrace();

				}
			}
		}
	}


	@Override
	public void onRequestPermissionsResult(int requestCode,
	                                       @NonNull String permissions[], @NonNull int[] grantResults) {
		switch (requestCode) {
			case MY_PERMISSIONS_REQUEST_READ_STORAGE: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					openBottomSheet();
				} else {
					showRequestPermissionAlert();
				}
			}

		}
	}

	private void showRequestPermissionAlert() {
		UtilityManager.showSnackBarWithAction(getString(R.string.on_permission_deny),
				Snackbar.LENGTH_LONG, getString(R.string.app_settings),
				coordinator, new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent();
						intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
						Uri uri = Uri.fromParts("package", ProfileActivity.this.getPackageName(), null);
						intent.setData(uri);
						startActivity(intent);
					}
				});
	}
}
