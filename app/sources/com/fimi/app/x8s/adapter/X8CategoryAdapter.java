package com.fimi.app.x8s.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;
import java.util.List;

public class X8CategoryAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragments;

    public X8CategoryAdapter(FragmentManager fm) {
        super(fm);
    }

    public X8CategoryAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragments = fragmentList;
    }

    public Fragment getItem(int position) {
        return this.fragments == null ? null : (Fragment) this.fragments.get(position);
    }

    public int getCount() {
        return this.fragments == null ? 0 : this.fragments.size();
    }

    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
