package com.example.assignment1.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.example.assignment1.fragment.ProductsFragment;
import com.example.assignment1.fragment.ProvidersFragment;

public class FragmentAdapter extends FragmentPagerAdapter {
    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ProvidersFragment();
            case 1:
                return new ProductsFragment();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Shipping";
            case 1:
                return "Shopping";
            default:
                return super.getPageTitle(position);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
