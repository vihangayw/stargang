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
import com.paidtocode.stargang.modal.Comment;
import com.paidtocode.stargang.modal.Signup;
import com.paidtocode.stargang.modal.UserType;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final int VIEW_TYPE_LOADING = 0;
	private static final int VIEW_TYPE_POST_1 = 1;
	private final OnComponentClickListener listener;
	private List<Comment> comments;
	private Context context;

	public CommentAdapter(List<Comment> comments, OnComponentClickListener listener, Context context) {
		if (comments == null) comments = new ArrayList<>();
		this.comments = comments;
		this.context = context;
		this.listener = listener;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		if (comments == null) comments = new ArrayList<>();
		this.comments = comments;
		notifyDataSetChanged();
	}

	public Comment getWall(int position) {
		return this.comments.get(position);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		switch (viewType) {
			case VIEW_TYPE_POST_1:
				View view = LayoutInflater.from(parent.getContext())
						.inflate(R.layout.list_item_comment, parent, false);
				return new Post1ViewHolder(view);
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
			case VIEW_TYPE_POST_1:
				bindViewBranch((Post1ViewHolder) holder, position);
				break;
			case VIEW_TYPE_LOADING:
				bindViewHolderLoading((LoadingFooter) holder);
				break;
		}

	}

	private void bindViewBranch(final Post1ViewHolder holder, int position) {
		Comment comment = comments.get(position);
		if (comment != null) {
			if (!TextUtils.isEmpty(comment.getFullName())) {
				holder.txtName.setText(comment.getFullName().trim());
			} else {
				holder.txtName.setText("");
			}
			if (!TextUtils.isEmpty(comment.getComment())) {
				holder.txtComment.setText(comment.getComment().trim());
			} else {
				holder.txtComment.setText("");
			}
			showUserImage(new Signup(comment.getImage(), comment.getType()), holder);

			holder.txtTime.setText(!TextUtils.isEmpty(comment.getAgoTime()) ? comment.getAgoTime() : "");

		}
	}

	private void showUserImage(Signup user, Post1ViewHolder holder) {
		List<UserType> type = user.getType();
		boolean setImage = false;
		if (type != null && !type.isEmpty()) {
			UserType userType = type.get(0);
			if (userType != null && !TextUtils.isEmpty(userType.getIduserType())
					&& TextUtils.equals(userType.getIduserType(), "1")) {
				if (!TextUtils.isEmpty(user.getImage())) {
					setImage = true;
					Glide.with(context)
							.load(user.getImage())
							.diskCacheStrategy(DiskCacheStrategy.ALL)
							.into(holder.imgUser);
				}
			}

		}
		if (!setImage) {
			Glide.with(context)
					.load(R.drawable.ic_user)
					.crossFade()
					.into(holder.imgUser);
		}
	}

	@Override
	public int getItemViewType(int position) {
		Comment post = comments.get(position);
		if (comments.size() > position && post == null) return VIEW_TYPE_LOADING;
		else {
			return VIEW_TYPE_POST_1;
		}
	}

	@Override
	public int getItemCount() {
		return comments.size();
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

	class Post1ViewHolder extends RecyclerView.ViewHolder {

		final TextView txtName;
		final TextView txtTime;
		final TextView txtComment;
		final ImageView imgUser;

		Post1ViewHolder(View itemView) {
			super(itemView);
			txtComment = itemView.findViewById(R.id.txt_caption);
			txtName = itemView.findViewById(R.id.txt_name);
			txtTime = itemView.findViewById(R.id.txt_time);
			imgUser = itemView.findViewById(R.id.img_user);

			setListener();
		}

		private void setListener() {

		}
	}
}
