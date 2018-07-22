package com.paidtocode.stargang.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.paidtocode.stargang.R;
import com.paidtocode.stargang.util.Constants;

import java.util.List;

public class PostImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final int VIEW_TYPE_LOADING = 0;
	private static final int VIEW_TYPE_NOTIFICATION = 1;
	private final OnComponentClickListener listener;
	private Context context;

	public PostImageAdapter(OnComponentClickListener listener, Context context) {
		this.context = context;
		this.listener = listener;
	}

	public List<Bitmap> getUsers() {
		return Constants.bitmaps;
	}


	public Bitmap getUser(int position) {
		return Constants.bitmaps.get(position);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		switch (viewType) {
			case VIEW_TYPE_NOTIFICATION:
				View view = LayoutInflater.from(parent.getContext())
						.inflate(R.layout.list_item_post, parent, false);
				return new BitmapViewHolder(view);
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
				bindViewBranch((BitmapViewHolder) holder, position);
				break;
			case VIEW_TYPE_LOADING:
				bindViewHolderLoading((LoadingFooter) holder);
				break;
		}

	}

	private void bindViewBranch(final BitmapViewHolder holder, int position) {
		final Bitmap bitmap = Constants.bitmaps.get(position);
		if (bitmap != null) {
			holder.image.setImageBitmap(bitmap);
		}
	}

	@Override
	public int getItemViewType(int position) {
		return Constants.bitmaps.size() > position && Constants.bitmaps.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_NOTIFICATION;
	}

	@Override
	public int getItemCount() {
		return Constants.bitmaps.size();
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

	class BitmapViewHolder extends RecyclerView.ViewHolder {

		final ImageView image;

		BitmapViewHolder(View itemView) {
			super(itemView);
			image = itemView.findViewById(R.id.imgs);
		}

	}
}
