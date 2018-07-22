package com.paidtocode.stargang.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.paidtocode.stargang.R;
import com.paidtocode.stargang.modal.Image;

import java.util.List;

public class PostImageViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final int VIEW_TYPE_NOTIFICATION = 1;
	private final OnComponentClickListener listener;
	private final List<Image> images;
	private Context context;

	public PostImageViewAdapter(List<Image> images, OnComponentClickListener listener, Context context) {
		this.context = context;
		this.images = images;
		this.listener = listener;
	}


	public Image getImage(int position) {
		return images.get(position);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		switch (viewType) {
			case VIEW_TYPE_NOTIFICATION:
				View view = LayoutInflater.from(parent.getContext())
						.inflate(R.layout.list_item_post, parent, false);
				return new BitmapViewHolder(view);
		}
		return null;

	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		switch (holder.getItemViewType()) {
			case VIEW_TYPE_NOTIFICATION:
				bindViewBranch((BitmapViewHolder) holder, position);
				break;
		}

	}

	private void bindViewBranch(final BitmapViewHolder holder, int position) {
		Image image = images.get(position);
		if (image != null && !TextUtils.isEmpty(image.getUrl())) {
			Glide.with(context)
					.load(image.getUrl().trim())
					.centerCrop()
					.diskCacheStrategy(DiskCacheStrategy.ALL)
					.into(holder.image);
		} else {
			Glide.with(context)
					.load(R.drawable.ic_user)
					.crossFade()
					.into(holder.image);
		}
	}

	@Override
	public int getItemViewType(int position) {
		return VIEW_TYPE_NOTIFICATION;
	}

	@Override
	public int getItemCount() {
		return images.size();
	}

	public interface OnComponentClickListener {
		void onComponentClick(View itemView, int position);
	}


	class BitmapViewHolder extends RecyclerView.ViewHolder {

		final ImageView image;

		BitmapViewHolder(View itemView) {
			super(itemView);
			image = itemView.findViewById(R.id.imgs);
			image.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (listener != null)
						listener.onComponentClick(view, getLayoutPosition());
				}
			});
		}

	}
}
