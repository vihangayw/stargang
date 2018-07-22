package com.paidtocode.stargang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.paidtocode.stargang.R;
import com.paidtocode.stargang.modal.Image;
import com.paidtocode.stargang.modal.Post;
import com.paidtocode.stargang.modal.Wall;
import com.paidtocode.stargang.modal.WallImage;
import com.paidtocode.stargang.ui.adapter.PostImageViewAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.paidtocode.stargang.util.Constants.ACTION_POST;
import static com.paidtocode.stargang.util.Constants.ACTION_WALL;

public class PostImagesActivity extends AppCompatActivity implements PostImageViewAdapter.OnComponentClickListener {

	private View btnBack, btnSave;
	private PostImageViewAdapter postImageAdapter;
	private Post post;
	private Wall wall;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_images);

		Bundle extras = getIntent().getExtras();
		if (extras != null && getIntent().getAction() != null) {
			if (getIntent().getAction().equals(ACTION_POST))
				post = (Post) extras.getSerializable("post");
			if (getIntent().getAction().equals(ACTION_WALL))
				wall = (Wall) extras.getSerializable("post");
		}
		initializeViews();
		setListeners();
	}

	private void initializeViews() {
		TextView txtTitle = findViewById(R.id.title);
		txtTitle.setText(R.string.post_images);
		btnBack = findViewById(R.id.btn_back);
		btnSave = findViewById(R.id.btn_nav);
		btnSave.setVisibility(View.GONE);

		RecyclerView recyclerView = findViewById(R.id.recycler_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
		List<String> stringList = new ArrayList<>();
		if (post != null && post.getImages() != null) {
			for (Image image : post.getImages()) {
				stringList.add(image.getUrl());
			}
			postImageAdapter = new PostImageViewAdapter(stringList, this, this);
			recyclerView.setAdapter(postImageAdapter);
		} else if (wall != null && wall.getImages() != null) {
			for (WallImage image : wall.getImages()) {
				stringList.add(image.getImage());
			}
			postImageAdapter = new PostImageViewAdapter(stringList, this, this);
			recyclerView.setAdapter(postImageAdapter);
		}
	}

	private void setListeners() {
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onBackPressed();
			}
		});

	}

	@Override
	public void onComponentClick(View itemView, int position) {
		if (postImageAdapter != null && postImageAdapter.getItemCount() > position) {
			String image = postImageAdapter.getImage(position);
			if (image != null) {
				Intent intent = new Intent(this, ImagePreviewActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("url", image);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		}
	}
}
