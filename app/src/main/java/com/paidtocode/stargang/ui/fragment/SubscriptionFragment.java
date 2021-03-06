package com.paidtocode.stargang.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
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
import com.paidtocode.stargang.api.response.UserListResponse;
import com.paidtocode.stargang.listener.EndlessRecyclerViewScrollListener;
import com.paidtocode.stargang.modal.User;
import com.paidtocode.stargang.modal.UserList;
import com.paidtocode.stargang.ui.ProfileActivity;
import com.paidtocode.stargang.ui.adapter.UserAdapter;
import com.paidtocode.stargang.util.UserSessionManager;
import com.paidtocode.stargang.util.UtilityManager;
import com.paidtocode.stargang.util.WrapContentLinearLayoutManager;

import org.json.JSONException;

import java.util.List;

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
	private Handler handler = new Handler();
	private UserAdapter userAdapter;
	private EditText txtSearch;
	private AlertDialog alertDialog;
	private String key = "";

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

	private APIHelper.PostManResponseListener postManResponseListener;
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			page = 1;
			if (userAdapter != null) {
				userAdapter.getUsers().clear();
				userAdapter.notifyDataSetChanged();
			}
			searchProduct();
		}
	};

	private void setHookListeners() {
		postManResponseListener = new APIHelper.PostManResponseListener() {
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
		};
		txtSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				SubscriptionFragment.this.key = s.toString();
				handler.removeCallbacks(runnable);
				handler.postDelayed(runnable, 800);
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		recyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);
	}

	private void searchProduct() {
		if (!TextUtils.isEmpty(key)) {
			new UserRequestHelperImpl().getUserList(key.trim(), page, LIMIT, postManResponseListener);
		} else {
			loadMore(page);
		}
	}

	private void initializeComponents(View view) {
		txtSearch = view.findViewById(R.id.txt_search);
		recyclerView = view.findViewById(R.id.recycler_view);
		recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
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
		if (checkNetwork()) {
			if (!TextUtils.isEmpty(key)) {
				new UserRequestHelperImpl().getUserList(key.trim(), page, LIMIT, postManResponseListener);
			} else {
				new UserRequestHelperImpl().getUserList(page, LIMIT, postManResponseListener);
			}
		} else {
			SubscriptionFragment.this.page--;
		}
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
		if (itemView.getId() == R.id.btn_sub) {
			if (userAdapter != null && userAdapter.getItemCount() > position) {
				User user = userAdapter.getUsers().get(position);
				if (user != null) {
					if (user.isSubscribe())
						proceedSubs(user);
					else if (user.getDateRemaining() > 0) {
						subsPopup(user);
					} else {
						proceedSubs(user);
					}
				}
			}
		} else if (getActivity() != null) {
			if (userAdapter != null && userAdapter.getItemCount() > position) {
				User user = userAdapter.getUsers().get(position);
				if (user != null) {
					Intent intent = new Intent(getActivity(), ProfileActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("user", user);
					intent.putExtras(bundle);
					intent.setAction("UserProfile");
					startActivity(intent);
				}
			}
		}
	}

	private void subsPopup(final User user) {
		if (getActivity() == null) return;
		MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
				.customView(R.layout.layout_subs, true).cancelable(true);
		final MaterialDialog dialog = builder.build();
		View customView = dialog.getCustomView();
		if (customView != null) {
//			RelativeLayout relativeLayout = customView.findViewById(R.id.layout_dialog);
//			FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) relativeLayout.getLayoutParams();
//			layoutParams.height = getHeight(0) - 40;
//			relativeLayout.setLayoutParams(layoutParams);

			ImageView imgUser = customView.findViewById(R.id.img_user);
			final TextView txtName = customView.findViewById(R.id.txt_name);
			final TextView txtMessage = customView.findViewById(R.id.msg);
			final View btnSub = customView.findViewById(R.id.btn_sub);

			if (!TextUtils.isEmpty(user.getFullName())) {
				txtName.setText(user.getFullName().trim());
			} else {
				txtName.setText("");
			}
			if (!TextUtils.isEmpty(user.getImage())) {
				Glide.with(getActivity())
						.load(user.getImage())
						.crossFade()
						.diskCacheStrategy(DiskCacheStrategy.ALL)
						.into(imgUser);
			} else {
				Glide.with(getActivity())
						.load(R.drawable.ic_user)
						.crossFade()
						.into(imgUser);
			}
			txtMessage.setText("You have a " + user.getDateRemaining() + " days free subscription");
			btnSub.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					proceedSubs(user);
				}
			});
		}
		dialog.show();
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
			alertDialog = UtilityManager.showAlert(getContext(), null,
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
			alertDialog = UtilityManager.showAlert(getContext(), null,
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
						if (getActivity() != null)
							Toast.makeText(getActivity(), "Request Sent", Toast.LENGTH_SHORT).show();
						updateUser(user);
					} else {
						if (getActivity() != null)
							Toast.makeText(getActivity(), "Request Failed", Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void onError(Error error) {
					if (getActivity() == null) return;
					if (error != null) {
						if (!TextUtils.isEmpty(error.getMessage())) {
							Toast.makeText(getActivity(),
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
//						if (getActivity() != null)
//							Toast.makeText(getActivity(), "Request Sent", Toast.LENGTH_SHORT).show();
						updateUser(user);
//						mobitelSubscribe(user);
					} else {
						if (getActivity() != null)
							Toast.makeText(getActivity(), "Request Failed", Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void onError(Error error) {
					if (getActivity() == null) return;
					if (error != null) {
						if (!TextUtils.isEmpty(error.getMessage())) {
							Toast.makeText(getActivity(),
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
							if (!TextUtils.isEmpty(message) && getActivity() != null)
								Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
							if (ancestor.getStatus() == 1000) {
								doSubscribe(user);
							}
						}
					}

					@Override
					public void onError(Error error) {
						if (getActivity() == null) return;
						if (error != null) {
							if (!TextUtils.isEmpty(error.getMessage())) {
								Toast.makeText(getActivity(),
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
							if (!TextUtils.isEmpty(message) && getActivity() != null)
								Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
							if (ancestor.getStatus() == 1000) {
								doUnsubscribe(user);
							}
						}
					}

					@Override
					public void onError(Error error) {
						if (getActivity() == null) return;
						if (error != null) {
							if (!TextUtils.isEmpty(error.getMessage())) {
								Toast.makeText(getActivity(),
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
		if (userAdapter != null) {
			List<User> users = userAdapter.getUsers();
			if (user != null) {
				for (int i = 0; i < users.size(); i++) {
					int indexOf = users.indexOf(user);
					if (indexOf != -1) {
						User userSel = users.get(indexOf);
						userSel.setSubscribe(!user.isSubscribe());
						userAdapter.notifyItemChanged(indexOf);
						break;
					}
				}
			}
		}
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
