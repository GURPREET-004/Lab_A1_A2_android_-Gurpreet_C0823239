package com.example.assignment1.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment1.R;
import com.example.assignment1.adapter.AdapterProvider;
import com.example.assignment1.main.MainViewModel;
import com.example.assignment1.type.MyType2;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class ProvidersFragment extends Fragment {
    @BindView(R.id.providersRecyclerView)
    RecyclerView recyclerView;

    AdapterProvider adapterProducts;
    MainViewModel viewModel;
    public ProvidersFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_providers, container, false);
        ButterKnife.bind(this,view);
        viewModel= ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        List<MyType2> list=viewModel.readProvidersData();
        adapterProducts=new AdapterProvider(list);
        recyclerView.setAdapter(adapterProducts);

        return view;
    }
}