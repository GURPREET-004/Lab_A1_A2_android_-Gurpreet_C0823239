package com.example.assignment1.main;

import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import android.annotation.SuppressLint;
import android.os.Bundle;
import com.example.assignment1.R;
import com.example.assignment1.base.BaseActivity;
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
        return new ViewModelProvider(this).get(MainViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        viewModel.setContext(MainActivity.this);
        viewModel.callFirstTime();
        viewModel.setViewPager(viewPager,getSupportFragmentManager(),tabLayout);
    }

    @Override
    public void onBackPressed() {

    }
}