package com.paidtocode.stargang.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Typeface;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.paidtocode.stargang.R;
import com.paidtocode.stargang.modal.Signup;
import com.paidtocode.stargang.modal.UserType;
import com.paidtocode.stargang.ui.adapter.ViewPagerAdapter;
import com.paidtocode.stargang.ui.fragment.AboutUserFragment;
import com.paidtocode.stargang.ui.fragment.EditUserFragment;
import com.paidtocode.stargang.ui.fragment.SubscriptionFragment;
import com.paidtocode.stargang.util.Constants;
import com.paidtocode.stargang.util.UserSessionManager;
import com.paidtocode.stargang.util.UtilityManager;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

	private final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 12;
	private TextView txtName;
	private ImageView imgCover;
	private ImageView imgProfilePic;
	private TabLayout tabLayout;
	private CoordinatorLayout coordinator;
	private View btnCover, btnImage, overlay;
	private boolean selectCover;
	private boolean isAdmin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		initializeComponents();
		setHookListeners();
	}

	private void initializeComponents() {
		txtName = findViewById(R.id.txt_name);
		imgCover = findViewById(R.id.img_cover);
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

		ViewPager viewPager = findViewById(R.id.viewpager_main);
		ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
		viewPager.setOffscreenPageLimit(isAdmin ? 3 : 2);
		SubscriptionFragment subscriptionFragment = SubscriptionFragment.newInstance();
		adapter.addFragment(AboutUserFragment.newInstance(), "About");
		adapter.addFragment(subscriptionFragment, "Photos");
		if (isAdmin)
			adapter.addFragment(EditUserFragment.newInstance(), "Edit");
		viewPager.setAdapter(adapter);

		tabLayout.setupWithViewPager(viewPager);
		changeTabsFont();
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

	private void setHookListeners() {
		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				if (tab.getPosition() == 2) {
					btnCover.setVisibility(View.VISIBLE);
					btnImage.setVisibility(View.VISIBLE);
					overlay.setVisibility(View.VISIBLE);
				} else {
					btnImage.setVisibility(View.GONE);
					btnCover.setVisibility(View.GONE);
					overlay.setVisibility(View.GONE);
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

	private void showUserData() {
		Signup user = UserSessionManager.getInstance().getUser();
		if (user != null) {
			txtName.setText(!TextUtils.isEmpty(user.getFullName()) ? user.getFullName() : "");
			List<UserType> type = user.getType();
			if (type != null && !type.isEmpty()) {
				UserType userType = type.get(0);
				if (userType != null && !TextUtils.isEmpty(userType.getUserType())
						&& TextUtils.equals(userType.getUserType(), "1")) {
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
