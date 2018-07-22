package com.paidtocode.stargang.ui;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.paidtocode.stargang.R;
import com.paidtocode.stargang.StarGangApplication;
import com.paidtocode.stargang.service.PostUploadService;
import com.paidtocode.stargang.ui.adapter.PostImageAdapter;

public class AddPostActivity extends AppCompatActivity {

	private TextView txtCaptions;
	private View btnBack, btnSave;
	private PostImageAdapter postImageAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_post);
		initializeViews();
		setListeners();
	}

	private void initializeViews() {
		TextView txtTitle = findViewById(R.id.title);
		txtTitle.setText(R.string.creata_post);
		btnBack = findViewById(R.id.btn_back);
		btnSave = findViewById(R.id.btn_nav);
		txtCaptions = findViewById(R.id.txt_caption);


		RecyclerView recyclerView = findViewById(R.id.recycler_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
		postImageAdapter = new PostImageAdapter(null, this);
		recyclerView.setAdapter(postImageAdapter);
	}

	private void setListeners() {
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onBackPressed();
			}
		});
		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				addPost();
			}
		});
	}

	private void addPost() {
		if (checkNetwork())
			if (!isMyServiceRunning(PostUploadService.class)) {
				Bundle bundle = new Bundle();
				bundle.putString("caption", txtCaptions.getText().toString());
				Intent intent = new Intent(this, PostUploadService.class);
				intent.putExtras(bundle);
				startService(intent);
				finish();
			} else {
				Toast.makeText(this, "Upload is in progress, please wait", Toast.LENGTH_LONG).show();
			}
	}

	private boolean isMyServiceRunning(Class<?> serviceClass) {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		if (manager == null)
			return false;
		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
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
