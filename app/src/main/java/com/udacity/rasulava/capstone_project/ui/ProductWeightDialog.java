package com.udacity.rasulava.capstone_project.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.udacity.rasulava.capstone_project.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Maryia on 12.08.2016.
 */
public class ProductWeightDialog extends DialogFragment {

    public static final String ARG_LISTENER = "listener";
    public static final String ARG_NAME = "name";

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.et_weight)
    EditText etWeight;

    private AddProductFragment.OnDismissListener listener;

    public static ProductWeightDialog createDialog(String name, AddProductFragment.OnDismissListener listener) {
        ProductWeightDialog dialog = new ProductWeightDialog();

        Bundle args = new Bundle();
        args.putSerializable(ProductWeightDialog.ARG_LISTENER, listener);
        args.putString(ProductWeightDialog.ARG_NAME, name);
        dialog.setArguments(args);

        return dialog;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_product_weight, null);
        ButterKnife.bind(this, rootView);
        listener = (AddProductFragment.OnDismissListener) getArguments().getSerializable(ARG_LISTENER);
        String name = getArguments().getString(ARG_NAME);
        tvTitle.setText(getString(R.string.weight_dialog_title, name));
        return rootView;
    }


    @OnClick(R.id.btn_save)
    public void saveProduct(View view) {
        if (etWeight.getText().toString().length() == 0) {
            etWeight.requestFocus();
            etWeight.setError(getString(R.string.error_empty_value));
        } else {
            int weight = Integer.parseInt(etWeight.getText().toString());
            listener.onDismissed(weight);
            dismiss();
        }

    }

    @OnClick(R.id.btn_cancel)
    public void cancel(View view) {
        dismiss();
    }
}
