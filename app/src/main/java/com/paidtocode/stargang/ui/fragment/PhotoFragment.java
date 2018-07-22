package com.paidtocode.stargang.ui.fragment;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.paidtocode.stargang.R;
import com.paidtocode.stargang.StarGangApplication;
import com.paidtocode.stargang.api.APIHelper;
import com.paidtocode.stargang.api.request.helper.impl.PostRequestHelperImpl;
import com.paidtocode.stargang.api.response.Ancestor;
import com.paidtocode.stargang.api.response.Error;
import com.paidtocode.stargang.api.response.PostListResponse;
import com.paidtocode.stargang.listener.EndlessRecyclerViewScrollListener;
import com.paidtocode.stargang.modal.Post;
import com.paidtocode.stargang.ui.adapter.PhotoAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoFragment extends Fragment implements PhotoAdapter.OnComponentClickListener {

	private static final int LIMIT = 15;
	private int page;
	private TextView txtInfo;
	private FloatingActionButton fab;
	private PhotoAdapter adapter;
	private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
	private RecyclerView recyclerView;

	public PhotoFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment EditUserFragment.
	 */
	public static PhotoFragment newInstance() {
		return new PhotoFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_photos_user, container, false);
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
		txtInfo.setVisibility(View.GONE);

		recyclerView = view.findViewById(R.id.recycler_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
		adapter = new PhotoAdapter(null, this, getContext());
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
					loadMore(++PhotoFragment.this.page);
				}
			}
		};
	}

	private void loadMore(int page) {
		if (checkNetwork())
			new PostRequestHelperImpl().getMyPost(page, LIMIT, new APIHelper.PostManResponseListener() {
				@Override
				public void onResponse(Ancestor ancestor) {
					if (adapter == null) return;
					removeProgress();
					if (ancestor instanceof PostListResponse) {
						List<Post> data = ((PostListResponse) ancestor).getData();
						if (data != null) {
							adapter.getPosts().addAll(data);
							adapter.notifyDataSetChanged();
						}
					}
				}

				private void removeProgress() {
					if (adapter.getItemCount() > 0
							&& adapter.getPosts().get(adapter.getItemCount() - 1) == null) {
						adapter.getPosts().remove(adapter.getItemCount() - 1);
						adapter.notifyItemRemoved(adapter.getItemCount() - 1);
					}
				}


				@Override
				public void onError(Error error) {
					PhotoFragment.this.page--;
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
		}
	}

	private void setListeners() {
		recyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);
	}

	@Override
	public void onComponentClick(View itemView, int position) {

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
