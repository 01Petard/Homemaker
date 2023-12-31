package com.wjx.homemaker.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;


public class MessageGroupFragmentAdapter extends FragmentStatePagerAdapter {
	private List<Fragment>list;
	public MessageGroupFragmentAdapter(FragmentManager fm, List<Fragment> list) {
		super(fm);

		this.list = list;
	}

	public MessageGroupFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		return list.size();
	}
}
