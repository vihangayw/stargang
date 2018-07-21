package com.paidtocode.stargang.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.paidtocode.stargang.R;
import com.paidtocode.stargang.modal.User;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final int VIEW_TYPE_LOADING = 0;
	private static final int VIEW_TYPE_NOTIFICATION = 1;
	private final OnComponentClickListener listener;
	private List<User> users;
	private Context context;

	public UserAdapter(List<User> users, OnComponentClickListener listener, Context context) {
		if (users == null) users = new ArrayList<>();
		this.users = users;
		this.context = context;
		this.listener = listener;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		if (users == null) users = new ArrayList<>();
		this.users = users;
		notifyDataSetChanged();
	}

	public User getUser(int position) {
		return this.users.get(position);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		switch (viewType) {
			case VIEW_TYPE_NOTIFICATION:
				View view = LayoutInflater.from(parent.getContext())
						.inflate(R.layout.list_item_user, parent, false);
				return new UserViewHolder(view);
			case VIEW_TYPE_LOADING:
				View loading = LayoutInflater.from(parent.getContext())
						.inflate(R.layout.list_item_load_more, parent, false);
				return new LoadingFooter(loading);
		}
		return null;

	}

	private void bindViewHolderLoading(LoadingFooter holder) {
		holder.layoutLoading.setVisibility(View.VISIBLE);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		switch (holder.getItemViewType()) {
			case VIEW_TYPE_NOTIFICATION:
				bindViewBranch((UserViewHolder) holder, position);
				break;
			case VIEW_TYPE_LOADING:
				bindViewHolderLoading((LoadingFooter) holder);
				break;
		}

	}

	private void bindViewBranch(final UserViewHolder holder, int position) {
		final User user = users.get(position);
		if (user != null) {
			if (!TextUtils.isEmpty(user.getFullName())) {
				holder.txtName.setText(user.getFullName().trim());
			} else {
				holder.txtName.setText("");
			}
			if (!TextUtils.isEmpty(user.getImage())) {
				Glide.with(context)
						.load(user.getImage())
						.crossFade()
						.placeholder(R.drawable.ic_user)
						.diskCacheStrategy(DiskCacheStrategy.ALL)
						.into(holder.imgUser);
			} else {
				Glide.with(context)
						.load(R.drawable.ic_user)
						.crossFade()
						.into(holder.imgUser);
			}
		}
	}

	@Override
	public int getItemViewType(int position) {
		return users.size() > position && users.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_NOTIFICATION;
	}

	@Override
	public int getItemCount() {
		return users.size();
	}

	public interface OnComponentClickListener {
		void onComponentClick(View itemView, int position);
	}

	private class LoadingFooter extends RecyclerView.ViewHolder {
		final View layoutLoading;

		LoadingFooter(View view) {
			super(view);
			layoutLoading = view.findViewById(R.id.layout_load_more);
		}
	}

	class UserViewHolder extends RecyclerView.ViewHolder {

		final TextView txtName;
		final TextView btnSubscribe;
		final ImageView imgUser;

		UserViewHolder(View itemView) {
			super(itemView);
			btnSubscribe = itemView.findViewById(R.id.btn_sub);
			txtName = itemView.findViewById(R.id.txt_name);
			imgUser = itemView.findViewById(R.id.img_user);

			setListener();
		}

		private void setListener() {
			btnSubscribe.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (listener != null)
						listener.onComponentClick(view, getLayoutPosition());
				}
			});
		}
	}
}
