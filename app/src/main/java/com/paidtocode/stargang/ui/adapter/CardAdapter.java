package com.paidtocode.stargang.ui.adapter;

import android.support.v7.widget.CardView;

public interface CardAdapter {

	int MAX_ELEVATION_FACTOR = 5;

	float getBaseElevation();

	CardView getCardViewAt(int position);

	int getCount();
}
