package com.paidtocode.stargang.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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
import com.paidtocode.stargang.modal.Image;
import com.paidtocode.stargang.modal.Post;
import com.paidtocode.stargang.modal.Signup;
import com.paidtocode.stargang.modal.UserType;
import com.paidtocode.stargang.util.UserSessionManager;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final int VIEW_TYPE_LOADING = 0;
	private static final int VIEW_TYPE_POST_1 = 1;
	private static final int VIEW_TYPE_POST_2 = 2;
	private static final int VIEW_TYPE_POST_3 = 3;
	private final OnComponentClickListener listener;
	private List<Post> posts;
	private Context context;
	private Signup user;

	public PhotoAdapter(List<Post> posts, OnComponentClickListener listener, Context context) {
		if (posts == null) posts = new ArrayList<>();
		this.posts = posts;
		this.context = context;
		this.listener = listener;
		this.user = UserSessionManager.getInstance().getUser();
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		if (posts == null) posts = new ArrayList<>();
		this.posts = posts;
		notifyDataSetChanged();
	}

	public Post getPost(int position) {
		return this.posts.get(position);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		switch (viewType) {
			case VIEW_TYPE_POST_1:
				View view = LayoutInflater.from(parent.getContext())
						.inflate(R.layout.list_item_photo, parent, false);
				return new Post1ViewHolder(view);
			case VIEW_TYPE_POST_2:
				view = LayoutInflater.from(parent.getContext())
						.inflate(R.layout.list_item_photo_2, parent, false);
				return new Post1ViewHolder(view);
			case VIEW_TYPE_POST_3:
				view = LayoutInflater.from(parent.getContext())
						.inflate(R.layout.list_item_photo_3, parent, false);
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
			case VIEW_TYPE_POST_2:
				bindViewBranch((Post1ViewHolder) holder, position);
				break;
			case VIEW_TYPE_POST_3:
				bindViewBranch((Post1ViewHolder) holder, position);
				break;
			case VIEW_TYPE_LOADING:
				bindViewHolderLoading((LoadingFooter) holder);
				break;
		}

	}

	private void bindViewBranch(final Post1ViewHolder holder, int position) {
		Post post = posts.get(position);
		if (post != null && user != null) {
			if (!TextUtils.isEmpty(user.getFullName())) {
				holder.txtName.setText(user.getFullName().trim());
			} else {
				holder.txtName.setText("");
			}
			if (!TextUtils.isEmpty(post.getpText())) {
				holder.txtCaption.setVisibility(View.VISIBLE);
				holder.hr.setVisibility(View.VISIBLE);
				holder.txtCaption.setText(post.getpText().trim());
			} else {
				holder.txtCaption.setVisibility(View.VISIBLE);
				holder.hr.setVisibility(View.VISIBLE);
			}
			showUserImage(user, holder);
			List<Image> images = post.getImages();
			if (images != null && !images.isEmpty()) {
				if (images.size() == 1) {
					showPostImages01(images, holder);
				} else if (images.size() == 2) {
					showPostImages01(images, holder);
					showPostImages02(images, holder);
				} else {
					showPostImages01(images, holder);
					showPostImages02(images, holder);
					showPostImagesMore(images, holder);
				}
			}
			holder.txtTime.setText(!TextUtils.isEmpty(post.getAgoTime()) ? post.getAgoTime() : "");
			holder.txtLike.setText(String.valueOf(post.getLikes()));
			holder.txtComment.setText(String.valueOf(post.getComments()));

			if (post.getLikes() > 0) {
				holder.imgLike.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star));
			} else {
				holder.imgLike.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_not_fill));
			}
			if (post.getComments() > 0) {
				holder.imgComment.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_comment_fill));
			} else {
				holder.imgComment.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_comment));
			}

		}
	}

	private void showPostImages01(List<Image> images, Post1ViewHolder holder) {
		Image image = images.get(0);
		if (!TextUtils.isEmpty(image.getUrl())) {
			Glide.with(context)
					.load(image.getUrl())
					.centerCrop()
					.diskCacheStrategy(DiskCacheStrategy.ALL)
					.into(holder.img1);
		} else {
			Glide.with(context)
					.load(R.drawable.image_placeholder)
					.into(holder.img1);
		}
		if (holder.overlay != null) {
			holder.overlay.setVisibility(View.GONE);
		}
		if (holder.txtMore != null) {
			holder.txtMore.setVisibility(View.GONE);
		}
		if (holder.img2 != null) {
			holder.img2.setVisibility(View.GONE);
		}
	}


	private void showPostImages02(List<Image> images, Post1ViewHolder holder) {
		holder.img2.setVisibility(View.VISIBLE);
		Image image = images.get(1);
		if (!TextUtils.isEmpty(image.getUrl())) {
			Glide.with(context)
					.load(image.getUrl())
					.centerCrop()
					.diskCacheStrategy(DiskCacheStrategy.ALL)
					.into(holder.img2);
		} else {
			Glide.with(context)
					.load(R.drawable.image_placeholder)
					.into(holder.img2);
		}
		if (holder.txtMore != null) {
			holder.txtMore.setVisibility(View.GONE);
		}
		if (holder.overlay != null) {
			holder.overlay.setVisibility(View.GONE);
		}
	}

	private void showPostImagesMore(List<Image> images, Post1ViewHolder holder) {
		holder.txtMore.setVisibility(View.VISIBLE);
		holder.overlay.setVisibility(View.VISIBLE);
		holder.txtMore.setText("+" + (images.size() - 2) + "\n more");
	}

	private void showUserImage(Signup user, Post1ViewHolder holder) {
		List<UserType> type = user.getType();
		boolean setImage = false;
		if (type != null && !type.isEmpty()) {
			UserType userType = type.get(0);
			if (userType != null && !TextUtils.isEmpty(userType.getUserType())
					&& TextUtils.equals(userType.getUserType(), "1")) {
				if (!TextUtils.isEmpty(user.getImage())) {
					setImage = true;
					Glide.with(context)
							.load(user.getImage())
							.crossFade()
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
		Post post = posts.get(position);
		if (posts.size() > position && post == null) return VIEW_TYPE_LOADING;
		else if (post != null && post.getImages() != null && !post.getImages().isEmpty()) {
			if (post.getImages().size() == 1) {
				return VIEW_TYPE_POST_1;
			} else if (post.getImages().size() == 2) {
				return VIEW_TYPE_POST_2;
			} else {
				return VIEW_TYPE_POST_3;
			}
		} else {
			return VIEW_TYPE_POST_1;
		}
	}

	@Override
	public int getItemCount() {
		return posts.size();
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
		final TextView txtLike;
		final TextView txtCaption;
		final TextView txtComment;
		final TextView txtMore;
		final ImageView imgUser;
		final ImageView imgLike;
		final ImageView imgComment;
		final ImageView img1;
		final ImageView img2;
		final View hr;
		final View overlay;

		Post1ViewHolder(View itemView) {
			super(itemView);
			txtCaption = itemView.findViewById(R.id.txt_caption);
			hr = itemView.findViewById(R.id.hr0);
			overlay = itemView.findViewById(R.id.overlay);
			txtName = itemView.findViewById(R.id.txt_name);
			txtTime = itemView.findViewById(R.id.txt_time);
			txtMore = itemView.findViewById(R.id.txt_more);
			txtComment = itemView.findViewById(R.id.txt_comment);
			txtLike = itemView.findViewById(R.id.txt_like);
			imgUser = itemView.findViewById(R.id.img_user);
			imgLike = itemView.findViewById(R.id.img_like);
			imgComment = itemView.findViewById(R.id.img_comment);
			img1 = itemView.findViewById(R.id.img_1);
			img2 = itemView.findViewById(R.id.img_2);

			setListener();
		}

		private void setListener() {
			imgLike.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (listener != null)
						listener.onComponentClick(view, getLayoutPosition());
				}
			});
			imgComment.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (listener != null)
						listener.onComponentClick(view, getLayoutPosition());
				}
			});
			if (txtMore != null)
				txtMore.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if (listener != null)
							listener.onComponentClick(view, getLayoutPosition());
					}
				});
			if (img2 != null)
				img2.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if (listener != null)
							listener.onComponentClick(view, getLayoutPosition());
					}
				});
			img1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (listener != null)
						listener.onComponentClick(view, getLayoutPosition());
				}
			});

		}
	}
}
