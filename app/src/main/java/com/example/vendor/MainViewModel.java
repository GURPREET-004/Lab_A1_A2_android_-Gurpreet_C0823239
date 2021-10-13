package com.example.vendor;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.vendor.adapter.FragmentAdapter;
import com.example.vendor.base.BaseViewModel;
import com.google.android.material.tabs.TabLayout;

public class MainViewModel extends BaseViewModel {
    public void setViewPager(ViewPager viewPager, FragmentManager fragmentManager, TabLayout tabLayout){
        FragmentAdapter fragmentAdapter=new FragmentAdapter(fragmentManager);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }
}
