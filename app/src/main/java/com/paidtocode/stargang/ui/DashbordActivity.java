package com.paidtocode.stargang.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.paidtocode.stargang.R;
import com.paidtocode.stargang.ui.adapter.ViewPagerAdapter;
import com.paidtocode.stargang.ui.fragment.HomeFragment;
import com.paidtocode.stargang.ui.fragment.QuestionsFragment;
import com.paidtocode.stargang.ui.fragment.SubscriptionFragment;
import com.paidtocode.stargang.ui.fragment.UserFragment;
import com.paidtocode.stargang.util.BottomNavigationViewHelper;

public class DashbordActivity extends AppCompatActivity implements
		BottomNavigationView.OnNavigationItemSelectedListener {

	private ViewPager viewPager;
	private final int[] navItems = new int[]{R.id.navigation_home, R.id.navigation_dashboard,
			R.id.navigation_notifications, R.id.navigation_qs};
	private BottomNavigationViewEx navigation;
	private SubscriptionFragment subscriptionFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashbord);

		initializeViews();
		setListeners();
	}

	private void initializeViews() {
		navigation = findViewById(R.id.navigation);
		BottomNavigationViewHelper.removeShiftMode(navigation);
		BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
		for (int i = 0; i < menuView.getChildCount(); i++) {
			final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
			final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
			final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
			layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 21, displayMetrics);
			layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 21, displayMetrics);
			iconView.setLayoutParams(layoutParams);
		}
		navigation.setTextVisibility(false);
		navigation.setTextSize(0);
		ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
		viewPager = findViewById(R.id.viewPager);
		viewPager.setOffscreenPageLimit(4);
		subscriptionFragment = SubscriptionFragment.newInstance();
		adapter.addFragment(subscriptionFragment, "Recent");
		adapter.addFragment(HomeFragment.newInstance(), "Top Rated");
		adapter.addFragment(UserFragment.newInstance(), "Downloads");
		adapter.addFragment(QuestionsFragment.newInstance(), "Bookmarks");
		viewPager.setAdapter(adapter);

	}

	private void setListeners() {
		navigation.setOnNavigationItemSelectedListener(this);
		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				navigation.setOnNavigationItemSelectedListener(null);
				navigation.setSelectedItemId(navItems[position]);
				navigation.setOnNavigationItemSelectedListener(DashbordActivity.this);
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		switch (item.getItemId()) {
			case R.id.navigation_home:
				viewPager.setCurrentItem(0);
				return true;
			case R.id.navigation_dashboard:
				viewPager.setCurrentItem(1);
				return true;
			case R.id.navigation_notifications:
				viewPager.setCurrentItem(2);
				return true;
			case R.id.navigation_qs:
				viewPager.setCurrentItem(3);
				return true;
		}
		return false;
	}


}
