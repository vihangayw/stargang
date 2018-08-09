package com.paidtocode.stargang.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.paidtocode.stargang.R;
import com.paidtocode.stargang.modal.Option;
import com.paidtocode.stargang.modal.Question;

import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {

	private List<CardView> mViews;
	private List<Question> mData;
	private float mBaseElevation;
	private ComponentClickListener listener;
	private Context context;

	public CardPagerAdapter(ComponentClickListener listener, Context context) {
		mData = new ArrayList<>();
		mViews = new ArrayList<>();
		this.listener = listener;
		this.context = context;
	}

	public void addCardItem(Question item) {
		mViews.add(null);
		mData.add(item);
	}

	public float getBaseElevation() {
		return mBaseElevation;
	}

	public List<Question> getmData() {
		return mData;
	}

	@Override
	public CardView getCardViewAt(int position) {
		return mViews.get(position);
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
		return view == object;
	}

	@NonNull
	@Override
	public Object instantiateItem(@NonNull ViewGroup container, int position) {
		View view = LayoutInflater.from(container.getContext())
				.inflate(R.layout.list_item_time_template, container, false);
		container.addView(view);
		final Question cardItem = mData.get(position);
		bind(cardItem, view);
		CardView cardView = view.findViewById(R.id.cardView);

		if (mBaseElevation == 0) {
			mBaseElevation = cardView.getCardElevation();
		}

		cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
		mViews.set(position, cardView);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null) listener.onItemClick(cardItem);
			}
		});
		return view;
	}

	@Override
	public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
		container.removeView((View) object);
		mViews.set(position, null);
	}

	private void bind(final Question item, View view) {
		TextView txtTime = view.findViewById(R.id.txt_time);
		RadioButton option1 = view.findViewById(R.id.option1);
		RadioButton option2 = view.findViewById(R.id.option2);
		RadioButton option3 = view.findViewById(R.id.option3);
		RadioButton option4 = view.findViewById(R.id.option4);
		final RadioGroup radioGroup = view.findViewById(R.id.radioGroup1);
//		Button button = view.findViewById(R.id.btn_submit);
		if (item.getOption() != null) {
			List<Option> options = item.getOption();
			for (int i = 0, option5Size = options.size(); i < option5Size; i++) {
				Option option = options.get(i);
				if (i == 0)
					option1.setText(option.getAnswers());
				if (i == 1)
					option2.setText(option.getAnswers());
				if (i == 2)
					option3.setText(option.getAnswers());
				if (i == 3)
					option4.setText(option.getAnswers());
			}
		}
		option1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if (b) item.setSelectedAns(item.getOption().get(0).getQaID());
			}
		});
		option2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if (b) item.setSelectedAns(item.getOption().get(1).getQaID());
			}
		});
		option3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if (b) item.setSelectedAns(item.getOption().get(2).getQaID());
			}
		});
		option4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if (b) item.setSelectedAns(item.getOption().get(3).getQaID());
			}
		});
		txtTime.setText(item.getQuestion());
	}

	public interface ComponentClickListener {
		void onItemClick(Question cardItem);
	}
}
