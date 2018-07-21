package com.paidtocode.stargang.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.paidtocode.stargang.R;
import com.paidtocode.stargang.ui.adapter.SubTabAdapter;
import com.paidtocode.stargang.ui.fragment.SubscriptionFragment;

import java.util.ArrayList;
import java.util.List;

public class DashbordActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashbord);

		BottomNavigationView navigation = findViewById(R.id.navigation);
		navigation.setOnNavigationItemSelectedListener(this);
		viewPager = findViewById(R.id.viewPager);
		List<Fragment> fragmentList = new ArrayList<>();
		fragmentList.add(SubscriptionFragment.newInstance());
//		fragmentList.add(OwnerEVFragment.newInstance(null));
		viewPager.setAdapter(new SubTabAdapter(getSupportFragmentManager(), fragmentList));
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		switch (item.getItemId()) {
			case R.id.navigation_home:
				return true;
			case R.id.navigation_dashboard:
				return true;
			case R.id.navigation_notifications:
				return true;
			case R.id.navigation_s:
				return true;
		}
		return false;
	}

}
