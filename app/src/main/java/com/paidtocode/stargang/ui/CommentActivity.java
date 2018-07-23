package com.paidtocode.stargang.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.paidtocode.stargang.R;
import com.paidtocode.stargang.StarGangApplication;
import com.paidtocode.stargang.api.APIHelper;
import com.paidtocode.stargang.api.request.helper.impl.PostRequestHelperImpl;
import com.paidtocode.stargang.api.response.Ancestor;
import com.paidtocode.stargang.api.response.CommentListResponse;
import com.paidtocode.stargang.api.response.CommentResponse;
import com.paidtocode.stargang.api.response.Error;
import com.paidtocode.stargang.listener.EndlessRecyclerViewScrollListener;
import com.paidtocode.stargang.modal.Comment;
import com.paidtocode.stargang.modal.Signup;
import com.paidtocode.stargang.modal.Wall;
import com.paidtocode.stargang.ui.adapter.CommentAdapter;
import com.paidtocode.stargang.util.Constants;
import com.paidtocode.stargang.util.UserSessionManager;
import com.paidtocode.stargang.util.WrapContentLinearLayoutManager;

import java.util.List;

public class CommentActivity extends AppCompatActivity {

	private static final int LIMIT = 15;
	private int page;
	private View btnBack;
	private View btnSend;
	private EditText editText;
	private Wall wall;
	private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
	private RecyclerView recyclerView;
	private CommentAdapter commentAdapter;
	private SwipeRefreshLayout refreshLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			wall = (Wall) extras.getSerializable("wall");
		}
		initializeComponents();
		setHookListeners();
		loadComments(++page);
	}

	@Override
	public void onBackPressed() {
		if (Constants.addComment > 0) {
			Intent intent = new Intent();
			setResult(RESULT_OK, intent);
			finish();
		} else super.onBackPressed();
	}

	private void setHookListeners() {
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onBackPressed();
			}
		});
		btnSend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				addComment();
			}
		});
		recyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);
		refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				refreshLayout.setRefreshing(true);
				page = 0;
				if (commentAdapter != null) {
					commentAdapter.getComments().clear();
				}
				loadComments(++page);
			}
		});
	}

	private void addComment() {
		if (editText.getText().toString().trim().isEmpty() || wall == null) {
			editText.setText("");
			return;
		}
		if (checkNetwork()) {
			hideKeyboard();
			refreshLayout.setRefreshing(true);
			new PostRequestHelperImpl().addComment(editText.getText().toString().trim(), wall.getPostID(),
					new APIHelper.PostManResponseListener() {
						@Override
						public void onResponse(Ancestor ancestor) {
							refreshLayout.setRefreshing(false);
							if (ancestor.getStatus()) {
								if (ancestor instanceof CommentResponse) {
									Comment data = ((CommentResponse) ancestor).getData();
									if (data != null) {
										Signup user = UserSessionManager.getInstance().getUser();
										if (user != null) {
											data.setType(user.getType());
											if (user.getImage() != null)
												data.setImage(user.getImage());
										}
										commentAdapter.getComments().add(data);
										commentAdapter.notifyDataSetChanged();
										editText.setText("");
										recyclerView.scrollToPosition(commentAdapter.getItemCount() - 1);
										Constants.addComment++;
									}
								}
							}
						}


						@Override
						public void onError(Error error) {
							refreshLayout.setRefreshing(false);
							if (error != null) {
								if (!TextUtils.isEmpty(error.getMessage())) {
									Toast.makeText(CommentActivity.this,
											error.getMessage(), Toast.LENGTH_SHORT).show();
								}
							}
						}
					});
		}
	}

	private void initializeComponents() {

		Constants.addComment = 0;
		TextView txtTitle = findViewById(R.id.title);
		txtTitle.setText(R.string.comments);
		btnBack = findViewById(R.id.btn_back);
		refreshLayout = findViewById(R.id.swipe_refresh);
		refreshLayout.setRefreshing(false);
		editText = findViewById(R.id.txt_comment);
		btnSend = findViewById(R.id.btn_send);
		View btnSave = findViewById(R.id.btn_nav);
		btnSave.setVisibility(View.GONE);

		recyclerView = findViewById(R.id.recycler_view);
		recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
		commentAdapter = new CommentAdapter(null, null, this);
		recyclerView.setAdapter(commentAdapter);

		endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(
				(LinearLayoutManager) recyclerView.getLayoutManager()) {
			@Override
			public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
				if (commentAdapter != null && commentAdapter.getItemCount() > 0
						&& commentAdapter.getComments().get(commentAdapter.getItemCount() - 1) != null) {
					commentAdapter.getComments().add(null);
					recyclerView.post(new Runnable() {
						public void run() {
							commentAdapter.notifyItemInserted(commentAdapter.getItemCount() - 1);
						}
					});
					loadComments(++CommentActivity.this.page);
				}
			}
		};
	}

	private void loadComments(int page) {
		if (checkNetwork()) {
			new PostRequestHelperImpl().getAllComments(page, LIMIT, wall.getPostID(), new APIHelper.PostManResponseListener() {
				@Override
				public void onResponse(Ancestor ancestor) {
					refreshLayout.setRefreshing(false);
					removeProgress();
					if (ancestor.getStatus()) {
						if (ancestor instanceof CommentListResponse) {
							List<Comment> data = ((CommentListResponse) ancestor).getData();
							if (data != null) {
								commentAdapter.getComments().addAll(data);
								commentAdapter.notifyDataSetChanged();
							}
						}
					}
				}

				private void removeProgress() {
					if (commentAdapter.getItemCount() > 0
							&& commentAdapter.getComments().get(commentAdapter.getItemCount() - 1) == null) {
						commentAdapter.getComments().remove(commentAdapter.getItemCount() - 1);
						commentAdapter.notifyItemRemoved(commentAdapter.getItemCount() - 1);
					}
				}

				@Override
				public void onError(Error error) {
					refreshLayout.setRefreshing(false);
					removeProgress();

				}
			});
		} else {
			this.page--;
		}
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

	private void hideKeyboard() {
		View view = this.getCurrentFocus();
		if (view != null) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}
}
