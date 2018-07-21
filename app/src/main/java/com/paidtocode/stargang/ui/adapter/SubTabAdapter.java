package com.paidtocode.stargang.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by admin on 1/9/17.
 */
public class SubTabAdapter extends FragmentStatePagerAdapter {
	private final List<Fragment> mFragmentList;

	public SubTabAdapter(FragmentManager manager, List<Fragment> fragmentList) {
		super(manager);
		this.mFragmentList = fragmentList;
	}

	@Override
	public Fragment getItem(int position) {
		return this.mFragmentList.get(position);
	}

	@Override
	public int getCount() {
		return this.mFragmentList.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return "";
	}
}
