package com.udacity.rasulava.capstone_project.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.udacity.rasulava.capstone_project.R;
import com.udacity.rasulava.capstone_project.db.model.Product;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Maryia on 12.08.2016.
 */
public class ProductWeightDialog extends DialogFragment {

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.et_weight)
    EditText etWeight;

    private Product product;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_product_weight, null);
        ButterKnife.bind(this, rootView);
        product = (Product) getArguments().getSerializable("product");
        tvTitle.setText(getString(R.string.weight_dialog_title, product.getName()));
        return rootView;
    }


    @OnClick(R.id.btn_save)
    public void saveProduct(View view) {
        if (etWeight.getText().toString().length() == 0) {
            etWeight.requestFocus();
            etWeight.setError(getString(R.string.error_empty_value));
        } else {
            int weight = Integer.parseInt(etWeight.getText().toString());
            dismiss();
        }

    }

    @OnClick(R.id.btn_cancel)
    public void cancel(View view) {
        dismiss();
    }
}
