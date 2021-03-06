package com.paidtocode.stargang.ui.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.paidtocode.stargang.R;
import com.paidtocode.stargang.StarGangApplication;
import com.paidtocode.stargang.api.APIHelper;
import com.paidtocode.stargang.api.request.helper.impl.PostRequestHelperImpl;
import com.paidtocode.stargang.api.response.Ancestor;
import com.paidtocode.stargang.api.response.Error;
import com.paidtocode.stargang.api.response.WallListResponse;
import com.paidtocode.stargang.listener.EndlessRecyclerViewScrollListener;
import com.paidtocode.stargang.modal.Wall;
import com.paidtocode.stargang.ui.AddPostActivity;
import com.paidtocode.stargang.ui.CommentActivity;
import com.paidtocode.stargang.ui.ImagePreviewActivity;
import com.paidtocode.stargang.ui.PostImagesActivity;
import com.paidtocode.stargang.ui.adapter.WallAdapter;
import com.paidtocode.stargang.util.Constants;
import com.paidtocode.stargang.util.UtilityManager;
import com.paidtocode.stargang.util.WrapContentLinearLayoutManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.paidtocode.stargang.util.Constants.ACTION_WALL;
import static com.paidtocode.stargang.util.Constants.bitmaps;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements WallAdapter.OnComponentClickListener {

	private static final int LIMIT = 15;
	private final int REQ_IMAGE_PICKER = 461;
	private final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 12;
	private int page;
	private TextView txtInfo;
	private FloatingActionButton fab;
	private WallAdapter adapter;
	private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
	private RecyclerView recyclerView;
	private CoordinatorLayout coordinator;
	private Wall wallComment;
	private SwipeRefreshLayout swipeRefreshLayout;

	public HomeFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment EditUserFragment.
	 */
	public static HomeFragment newInstance() {
		return new HomeFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_home, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initViews(view);
		setListeners();
		loadMore(++page);
	}

	private void initViews(View view) {
		fab = view.findViewById(R.id.add_images);
		txtInfo = view.findViewById(R.id.txt_info);
		coordinator = view.findViewById(R.id.coordinator);
		txtInfo.setVisibility(View.GONE);
		swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
		swipeRefreshLayout.setRefreshing(false);

		recyclerView = view.findViewById(R.id.recycler_view);
		recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
		adapter = new WallAdapter(null, this, getContext());
		recyclerView.setAdapter(adapter);

		endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(
				(LinearLayoutManager) recyclerView.getLayoutManager()) {
			@Override
			public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
				if (adapter != null && adapter.getItemCount() > 0
						&& adapter.getPosts().get(adapter.getItemCount() - 1) != null) {
					adapter.getPosts().add(null);
					recyclerView.post(new Runnable() {
						public void run() {
							adapter.notifyItemInserted(adapter.getItemCount() - 1);
						}
					});
					loadMore(++HomeFragment.this.page);
				}
			}
		};
	}

	private void loadMore(final int page) {
		if (checkNetwork())
			new PostRequestHelperImpl().getWallPosts(page, LIMIT, new APIHelper.PostManResponseListener() {
				@Override
				public void onResponse(Ancestor ancestor) {
					swipeRefreshLayout.setRefreshing(false);
					if (adapter == null) return;
					removeProgress();
					if (ancestor instanceof WallListResponse) {
						List<Wall> data = ((WallListResponse) ancestor).getData();
						if (data != null) {
							if (page == 1 && data.isEmpty()) {
								txtInfo.setText(R.string.no_posts);
								txtInfo.setVisibility(View.VISIBLE);
							} else {
								txtInfo.setVisibility(View.GONE);
							}
							adapter.getPosts().addAll(data);
							adapter.notifyDataSetChanged();
						}
					}
				}

				private void removeProgress() {
					swipeRefreshLayout.setRefreshing(false);
					if (adapter.getItemCount() > 0
							&& adapter.getPosts().get(adapter.getItemCount() - 1) == null) {
						adapter.getPosts().remove(adapter.getItemCount() - 1);
						adapter.notifyItemRemoved(adapter.getItemCount() - 1);
					}
				}


				@Override
				public void onError(Error error) {
					HomeFragment.this.page--;
					if (adapter == null) return;
					removeProgress();
					if (getActivity() != null) {
						if (!TextUtils.isEmpty(error.getMessage())) {
							Toast.makeText(getActivity(),
									error.getMessage(), Toast.LENGTH_SHORT).show();
						}
					}
				}
			});
		else {
			this.page--;
			if (page == 0 && adapter != null && adapter.getPosts().isEmpty()) {
				txtInfo.setText(R.string.no_network);
				txtInfo.setVisibility(View.VISIBLE);
			} else {
				txtInfo.setVisibility(View.GONE);
			}
		}
	}

	private void setListeners() {
		recyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				if (checkNetwork()) {
					if (adapter != null) {
						adapter.getPosts().clear();
						page = 0;
						loadMore(++page);
					}
				}
			}
		});
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				requirePermissions();
			}
		});
	}

	private void requirePermissions() {
		if (getActivity() == null) return;
		if (ContextCompat.checkSelfPermission(getActivity(),
				Manifest.permission.WRITE_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED) {
			if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
					Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
				showRequestPermissionAlert();
			} else {
				ActivityCompat.requestPermissions(getActivity(),
						new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
						MY_PERMISSIONS_REQUEST_READ_STORAGE);
			}
		} else {
			openBottomSheet();
		}
	}

	private void openBottomSheet() {
		ImagePicker.create(this)
				.returnAfterFirst(false)
				.folderMode(true) // folder mode (false by default)
				.imageTitle("Tap to select") // image selection title
				.multi() // multi mode (default mode)
				.theme(R.style.PickerTheme)
				.limit(10) // max images can be selected (99 by default)
				.showCamera(true) // show camera or not (true by default)
				.imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
				.enableLog(false) // disabling log
				.start(REQ_IMAGE_PICKER); // start image picker activity with request code
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case REQ_IMAGE_PICKER:
				bitmaps.clear();
				if (resultCode == Activity.RESULT_OK && data != null) {
					if (getActivity() == null) return;
					ArrayList<Image> images =
							(ArrayList<com.esafirm.imagepicker.model.Image>) ImagePicker.getImages(data);
					if (!images.isEmpty()) {
						for (com.esafirm.imagepicker.model.Image image : images) {
							Uri uri = Uri.fromFile(new File(image.getPath()));
							try {
								Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
								bitmaps.add(bitmap);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						startActivity(new Intent(getActivity(), AddPostActivity.class));
					}
				}
				break;
			case 400:
				if (wallComment != null && adapter != null && Constants.addComment > 0) {
					int i = adapter.getPosts().indexOf(wallComment);
					if (i != -1) {
						wallComment.setComments(wallComment.getComments() + Constants.addComment);
						wallComment.setCommentByMe(true);
						adapter.notifyItemChanged(i);
					}
				}
				break;
		}
	}

	@Override
	public void onComponentClick(View itemView, int position) {
		if (getActivity() == null) return;
		if (adapter != null && adapter.getPosts().size() > position)
			switch (itemView.getId()) {
				case R.id.txt_more:
					Intent intent = new Intent(getActivity(), PostImagesActivity.class);
					intent.setAction(ACTION_WALL);
					Bundle bundle = new Bundle();
					bundle.putSerializable("post", adapter.getPosts().get(position));
					intent.putExtras(bundle);
					startActivity(intent);
					break;
				case R.id.img_1:
					intent = new Intent(getActivity(), ImagePreviewActivity.class);
					bundle = new Bundle();
					bundle.putSerializable("url", adapter.getPosts().get(position).getImages().get(0).getImage());
					intent.putExtras(bundle);
					startActivity(intent);
					break;
				case R.id.img_2:
					intent = new Intent(getActivity(), ImagePreviewActivity.class);
					bundle = new Bundle();
					bundle.putSerializable("url", adapter.getPosts().get(position).getImages().get(1).getImage());
					intent.putExtras(bundle);
					startActivity(intent);
					break;
				case R.id.img_like:
					Wall post = adapter.getPosts().get(position);
					likePost(post);
					break;
				case R.id.btn_comment:
					intent = new Intent(getActivity(), CommentActivity.class);
					bundle = new Bundle();
					wallComment = adapter.getPosts().get(position);
					bundle.putSerializable("wall", wallComment);
					intent.putExtras(bundle);
					startActivityForResult(intent, 400);
					break;
			}
	}

	private void likePost(Wall post) {
		new PostRequestHelperImpl().likeUnlike(post.getPostID(), new APIHelper.PostManResponseListener() {
			@Override
			public void onResponse(Ancestor ancestor) {

			}

			@Override
			public void onError(Error error) {
				if (getActivity() != null) {
					if (!TextUtils.isEmpty(error.getMessage())) {
						Toast.makeText(getActivity(),
								error.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
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
		if (getActivity() == null) return;
		UtilityManager.showSnackBarWithAction(getString(R.string.on_permission_deny),
				Snackbar.LENGTH_LONG, getString(R.string.app_settings),
				coordinator, new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent();
						intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
						Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
						intent.setData(uri);
						startActivity(intent);
					}
				});
	}

	public boolean checkNetwork() {
		if (isNetworkConnected()) {
			return true;
		}
		if (getActivity() != null)
			Toast.makeText(getActivity(), getString(R.string.no_network), Toast.LENGTH_SHORT).show();
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
