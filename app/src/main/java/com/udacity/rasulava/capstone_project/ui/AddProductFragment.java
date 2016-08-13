package com.udacity.rasulava.capstone_project.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.udacity.rasulava.capstone_project.R;
import com.udacity.rasulava.capstone_project.Utils;
import com.udacity.rasulava.capstone_project.db.DBHelper;
import com.udacity.rasulava.capstone_project.db.Intake;
import com.udacity.rasulava.capstone_project.db.Product;

import java.io.Serializable;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Maryia on 11.08.2016.
 */
public class AddProductFragment extends DialogFragment {

    @BindView(R.id.et_product_name)
    EditText etName;

    @BindView(R.id.et_fat)
    EditText etFat;

    @BindView(R.id.et_carbs)
    EditText etCarb;

    @BindView(R.id.et_prot)
    EditText etProt;

    @BindView(R.id.et_kcal)
    EditText etKcal;

    private OnDismissListener listener;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_product, null);
        ButterKnife.bind(this, rootView);

        etFat.setFilters(new InputFilter[]{new InputFilterMinMax()});
        etCarb.setFilters(new InputFilter[]{new InputFilterMinMax()});
        etProt.setFilters(new InputFilter[]{new InputFilterMinMax()});
        listener = new OnDismissListener() {
            @Override
            public void onDismissed(int weight) {
                Product product = new Product();
                product.setName(etName.getText().toString());
                product.setCalories(Integer.parseInt(etKcal.getText().toString()));
                product.setFat(Integer.parseInt(etFat.getText().toString()));
                product.setCarbohydrate(Integer.parseInt(etCarb.getText().toString()));
                product.setProtein(Integer.parseInt(etProt.getText().toString()));

                product.setId(DBHelper.getInstance(getActivity()).save(product));

                Intake intake = new Intake();
                intake.setDate(Utils.dateToString(new Date()));
                intake.setProduct(product);
                intake.setWeight(weight);
                DBHelper.getInstance(getActivity()).save(intake);
            }
        };
        return rootView;
    }

    @OnClick(R.id.btn_save)
    public void addProduct(View view) {
        if (etName.getText().toString().length() == 0) {
            etName.requestFocus();
            etName.setError(getString(R.string.error_empty_name));
        } else if (etFat.getText().toString().length() == 0) {
            etFat.requestFocus();
            etFat.setError(getString(R.string.error_empty_value));
        } else if (etCarb.getText().toString().length() == 0) {
            etCarb.requestFocus();
            etCarb.setError(getString(R.string.error_empty_value));
        } else if (etProt.getText().toString().length() == 0) {
            etProt.requestFocus();
            etProt.setError(getString(R.string.error_empty_value));
        } else if (etKcal.getText().toString().length() == 0) {
            etKcal.requestFocus();
            etKcal.setError(getString(R.string.error_empty_value));
        } else {
            int fat = Integer.parseInt(etFat.getText().toString());
            int carb = Integer.parseInt(etCarb.getText().toString());
            int prot = Integer.parseInt(etProt.getText().toString());
            if (fat + carb + prot > 100)
                Toast.makeText(getActivity(), "The total weight should not exceed 100 gr!", Toast.LENGTH_LONG).show();
            else {
                ProductWeightDialog dialog = ProductWeightDialog.createDialog(etName.getText().toString(), listener);
                dialog.show(getFragmentManager(), "tag1");
                dismiss();
            }
        }
    }

    @OnClick(R.id.btn_cancel)
    public void cancel(View view) {
        dismiss();
    }

    public class InputFilterMinMax implements InputFilter {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                String parseThis = dest.toString().substring(0, dstart) + source.toString() + dest.toString().substring(dend);
                int input = Integer.parseInt(parseThis);
                if (input < 100)
                    return null;
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
            return "";
        }
    }

    interface OnDismissListener extends Serializable {
        void onDismissed(int weight);
    }
}
