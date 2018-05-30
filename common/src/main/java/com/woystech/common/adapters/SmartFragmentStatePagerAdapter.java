package com.woystech.common.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

/**
 * Created by firok on 8/18/2016.
 */
public abstract class SmartFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    //Sparse array to keep track of registered fragments in memory
    SparseArray<Fragment> registeredFragments = new SparseArray<>();


    public SmartFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    //Register the item when the item is instantiated
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position,fragment);
        return fragment;
    }

    //unregister when the item is inactive
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    /**
     * return the fragment for the position (if instantiated)
     * @param position
     * @return
     */
    public Fragment getRegisteredFragment(int position){
        return registeredFragments.get(position);
    }
}
