package com.udacity.rasulava.capstone_project.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.rasulava.capstone_project.R;
import com.udacity.rasulava.capstone_project.RequestHelper;
import com.udacity.rasulava.capstone_project.ResultListener;
import com.udacity.rasulava.capstone_project.model.FoodDetails;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Maryia on 09.07.2016.
 */
public class DetailsFragment extends Fragment {

    public static final String POS = "pos";
    public static final int REQUEST_CODE_SEARCH = 100;
    public static final String EXTRA_FOOD_ID_SELECTED = "food_id_selected";

    @BindView(R.id.tl_food)
    TableLayout mTableLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, rootView);

        for (int i = 0; i < 6; i++) {
            addTableRow();
        }
        return rootView;
    }

    private void addTableRow() {
        final TableRow tr = (TableRow) getActivity().getLayoutInflater().inflate(R.layout.table_row_item, null);

        TextView tv;

        tv = ButterKnife.findById(tr, R.id.tv_food_name);
        tv.setText("food");

        tv = ButterKnife.findById(tr, R.id.tv_weight);
        tv.setText("100");

        tv = ButterKnife.findById(tr, R.id.tv_fat);
        tv.setText("food");

        tv = ButterKnife.findById(tr, R.id.tv_carbs);
        tv.setText("100");

        tv = ButterKnife.findById(tr, R.id.tv_prot);
        tv.setText("food");

        tv = ButterKnife.findById(tr, R.id.tv_kcal);
        tv.setText("100");

        mTableLayout.addView(tr);

        // Draw separator
        tv = new TextView(getActivity());
        tv.setBackgroundColor(Color.parseColor("#80808080"));
        tv.setHeight(1);
        mTableLayout.addView(tv);

        // If you use context menu it should be registered for each table row
        registerForContextMenu(tr);
    }

    @OnClick(R.id.add_fab)
    public void addProduct(View view) {
        Intent intent = new Intent(getActivity(), SearchProductActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SEARCH);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SEARCH && data != null) {
            String id = data.getStringExtra(EXTRA_FOOD_ID_SELECTED);
            if (id != null) {
                new RequestHelper().getFoodById(getActivity(), id, new ResultListener<FoodDetails>() {
                    @Override
                    public void onSuccess(FoodDetails result) {
                        Toast.makeText(getActivity(), result.getName(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(getActivity(), "waisted!", Toast.LENGTH_LONG).show();
                    }
                });

            }
        }
    }
}
