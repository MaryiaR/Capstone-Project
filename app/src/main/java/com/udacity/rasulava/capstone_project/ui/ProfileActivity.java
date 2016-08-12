package com.udacity.rasulava.capstone_project.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.udacity.rasulava.capstone_project.R;
import com.udacity.rasulava.capstone_project.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Maryia on 12.08.2016.
 */
public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tv_kcal_total)
    TextView tvKcal;

    @BindView(R.id.et_age)
    EditText etAge;

    @BindView(R.id.rg_gender)
    RadioGroup rgGender;

    @BindView(R.id.et_weight)
    EditText etWeight;

    @BindView(R.id.et_height)
    EditText etHeight;

    @BindView(R.id.spinner_exercise)
    Spinner spinnerExercise;

    @BindView(R.id.spinner_goal)
    Spinner spinnerGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvKcal.setText(getString(R.string.total_kcal, 1200));

        ArrayAdapter<String> adapterExercise = createSpinnerAdapter(R.array.exercise_array);
        spinnerExercise.setAdapter(adapterExercise);

        ArrayAdapter<String> adapterGoal = createSpinnerAdapter(R.array.goals_array);
        spinnerGoal.setAdapter(adapterGoal);
    }


    @OnClick(R.id.btn_save)
    public void addProduct(View view) {
        if (!validate())
            return;
        int age = Integer.parseInt(etAge.getText().toString());
        Utils.GENDER gender = rgGender.getCheckedRadioButtonId() == R.id.rb_female ? Utils.GENDER.FEMALE : Utils.GENDER.MALE;
        int weight = Integer.parseInt(etWeight.getText().toString());
        int height = Integer.parseInt(etHeight.getText().toString());
        Utils.EXERCISE exercise = Utils.EXERCISE.values()[spinnerExercise.getSelectedItemPosition()];
        Utils.GOAL goal = Utils.GOAL.values()[spinnerGoal.getSelectedItemPosition()];
        double kcal = Utils.getCaloriesNeed(gender, age, weight, height, exercise, goal);
        tvKcal.setText(getString(R.string.total_kcal, kcal));
    }

    private boolean validate() {
        if (etAge.getText().toString().length() == 0) {
            etAge.requestFocus();
            etAge.setError(getString(R.string.error_empty_value));
        } else if (etWeight.getText().toString().length() == 0) {
            etWeight.requestFocus();
            etWeight.setError(getString(R.string.error_empty_value));
        } else if (etHeight.getText().toString().length() == 0) {
            etHeight.requestFocus();
            etHeight.setError(getString(R.string.error_empty_value));
        } else
            return true;
        return false;
    }

    private ArrayAdapter<String> createSpinnerAdapter(int stringArrayId) {
        return new ArrayAdapter<String>(this, R.layout.spinner_item, getResources().getStringArray(stringArrayId)) {

            public View getView(int position, View convertView, ViewGroup parent) {
                return super.getView(position, convertView, parent);

            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setGravity(Gravity.CENTER);
                return v;
            }

        };
    }


}
