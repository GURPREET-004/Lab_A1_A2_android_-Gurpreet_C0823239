package com.example.vendor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import com.example.vendor.base.BaseActivity;
import com.example.vendor.database.DbHelper;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class MainActivity extends BaseActivity<MainViewModel> {

    @BindView(R.id.viewPagerMain)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @Override
    protected MainViewModel createViewModel() {
        return new  ViewModelProvider(this).get(MainViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        viewModel.setViewPager(viewPager,getSupportFragmentManager(),tabLayout);
        //new DbHelper(getApplicationContext());
    }

    @Override
    public void onBackPressed() {

    }
}