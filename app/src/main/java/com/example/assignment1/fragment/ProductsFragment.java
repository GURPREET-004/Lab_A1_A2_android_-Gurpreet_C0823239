package com.example.assignment1.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment1.R;
import com.example.assignment1.adapter.AdapterProducts;
import com.example.assignment1.main.MainViewModel;
import com.example.assignment1.type.MyType3;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

@SuppressLint("NonConstantResourceId")
public class ProductsFragment extends Fragment {
    private List<MyType3> list;
    @BindView(R.id.productsRecyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.search_editText)
    EditText search;

    MainViewModel viewModel;

    public ProductsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        ButterKnife.bind(this, view);

        viewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        list = viewModel.readProductsData();
        AdapterProducts adapter = new AdapterProducts(list);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @OnTextChanged(R.id.search_editText)
    void onTextChanged(CharSequence text) {
        List<MyType3> listTemp = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).productName.toLowerCase(Locale.ROOT).contains(text)) {
                listTemp.add(list.get(i));
            }
        }
        AdapterProducts adapter2 = new AdapterProducts(listTemp);
        recyclerView.setAdapter(adapter2);
    }
    @OnTextChanged(value = R.id.search_editText, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onAfterTextChanged(CharSequence text) {

    }

}