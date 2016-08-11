package com.udacity.rasulava.capstone_project;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.udacity.rasulava.capstone_project.model.Food;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mrasulava on 8/10/2016.
 */
public class SearchProductFragment extends Fragment implements OnProductsSearchListener {
    public static final String POS = "pos";

    @BindView(R.id.autocomplete_textview)
    EditText searchEditText;

    @BindView(R.id.search_listview)
    ListView searchListview;

    @BindView(R.id.search_progress)
    ProgressBar searchProgress;

    @BindView(R.id.empty_text)
    TextView emptyTextTextView;

    private SearchProductAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search_product, container, false);
        ButterKnife.bind(this, rootView);

        ArrayList<Food> locationListAdapterContainers = new ArrayList<>();
        adapter = new SearchProductAdapter(getActivity(), locationListAdapterContainers, this);
        //Calling addFooterView() before setting the adapter and calling removeFooterView() after it is a workaround to get the footer view displayed on Android prior to 4.4 (19)
        searchListview.setAdapter(adapter);
        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!TextUtils.isEmpty(s) && adapter.getCount() == 0) {
                    searchProgress.setVisibility(View.VISIBLE);
                    emptyTextTextView.setVisibility(View.GONE);
                    searchListview.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) {
                    adapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        return rootView;
    }

    @Override
    public void onSearchSuccessful() {
        searchProgress.setVisibility(View.GONE);
        emptyTextTextView.setVisibility(View.GONE);
        searchListview.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSearchEmpty() {
        searchProgress.setVisibility(View.GONE);
        emptyTextTextView.setVisibility(View.VISIBLE);
        searchListview.setVisibility(View.GONE);
    }

    @Override
    public void onSearchFailed() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                searchProgress.setVisibility(View.GONE);
                emptyTextTextView.setVisibility(View.VISIBLE);
                searchListview.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onProductSelected(String foodId) {
        Intent intent = new Intent();
        intent.putExtra(DetailsFragment.EXTRA_FOOD_ID_SELECTED, foodId);
        getActivity().setResult(Activity.RESULT_OK , intent);
        getActivity().finish();
    }
}
