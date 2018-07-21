package com.paidtocode.stargang.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.paidtocode.stargang.R;
import com.paidtocode.stargang.api.APIHelper;
import com.paidtocode.stargang.api.request.helper.impl.UserRequestHelperImpl;
import com.paidtocode.stargang.api.response.Ancestor;
import com.paidtocode.stargang.api.response.Error;
import com.paidtocode.stargang.api.response.UserListResponse;
import com.paidtocode.stargang.listener.EndlessRecyclerViewScrollListener;
import com.paidtocode.stargang.modal.UserList;
import com.paidtocode.stargang.ui.adapter.UserAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SubscriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubscriptionFragment extends Fragment implements UserAdapter.OnComponentClickListener {

	private static final int LIMIT = 15;
	private int page;
	private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
	private RecyclerView recyclerView;
	private UserAdapter userAdapter;

	public SubscriptionFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment SubscriptionFragment.
	 */
	public static SubscriptionFragment newInstance() {
		SubscriptionFragment fragment = new SubscriptionFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_subcription, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initializeComponents(view);
		setHookListeners();
		loadMore(++SubscriptionFragment.this.page);
	}

	private void setHookListeners() {
		recyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);
	}

	private void initializeComponents(View view) {
		recyclerView = view.findViewById(R.id.recycler_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
		userAdapter = new UserAdapter(null, this, getContext());
		recyclerView.setAdapter(userAdapter);

		endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(
				(LinearLayoutManager) recyclerView.getLayoutManager()) {
			@Override
			public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
				if (userAdapter != null && userAdapter.getItemCount() > 0
						&& userAdapter.getUsers().get(userAdapter.getItemCount() - 1) != null) {
					userAdapter.getUsers().add(null);
					recyclerView.post(new Runnable() {
						public void run() {
							userAdapter.notifyItemInserted(userAdapter.getItemCount() - 1);
						}
					});
					loadMore(++SubscriptionFragment.this.page);
				}
			}
		};
	}

	private void loadMore(int page) {
		new UserRequestHelperImpl().getUserList(page, LIMIT, new APIHelper.PostManResponseListener() {
			@Override
			public void onResponse(Ancestor ancestor) {
				if (userAdapter == null) return;
				removeProgress();
				if (ancestor instanceof UserListResponse) {
					UserList data = ((UserListResponse) ancestor).getData();
					if (data != null && data.getUsers() != null) {
						userAdapter.getUsers().addAll(data.getUsers());
						userAdapter.notifyDataSetChanged();
					}
				}
			}

			private void removeProgress() {
				if (userAdapter.getItemCount() > 0
						&& userAdapter.getUsers().get(userAdapter.getItemCount() - 1) == null) {
					userAdapter.getUsers().remove(userAdapter.getItemCount() - 1);
					userAdapter.notifyItemRemoved(userAdapter.getItemCount() - 1);
				}
			}


			@Override
			public void onError(Error error) {
				SubscriptionFragment.this.page--;
				if (userAdapter == null) return;
				removeProgress();
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
	public void onAttach(Context context) {
		super.onAttach(context);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onComponentClick(View itemView, int position) {

	}
}
