package com.paidtocode.stargang.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;
import com.paidtocode.stargang.R;

public class ImagePreviewActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_preview);

		Bundle extras = getIntent().getExtras();
		if (extras == null || extras.getString("url") == null) {
			finish();
			return;
		}
		PhotoView imageView = findViewById(R.id.image_view);
		Glide.with(this)
				.load(extras.getString("url"))
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.into(imageView);
	}
}
