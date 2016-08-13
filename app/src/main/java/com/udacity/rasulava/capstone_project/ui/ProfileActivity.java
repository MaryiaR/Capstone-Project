package com.udacity.rasulava.capstone_project.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.udacity.rasulava.capstone_project.R;
import com.udacity.rasulava.capstone_project.Utils;
import com.udacity.rasulava.capstone_project.model.UserData;

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

        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_ad_app_id));
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest.Builder builder = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
        AdRequest adRequest = builder.build();
        mAdView.loadAd(adRequest);

        ArrayAdapter<String> adapterExercise = createSpinnerAdapter(R.array.exercise_array);
        spinnerExercise.setAdapter(adapterExercise);

        ArrayAdapter<String> adapterGoal = createSpinnerAdapter(R.array.goals_array);
        spinnerGoal.setAdapter(adapterGoal);

        UserData userData = Utils.getUserData(this);
        if (userData.getAge() != 0) {
            etAge.setText(String.valueOf(userData.getAge()), TextView.BufferType.EDITABLE);
            rgGender.check(userData.getGender() == UserData.GENDER.FEMALE ? R.id.rb_female : R.id.rb_male);
            etWeight.setText(String.valueOf(userData.getWeight()), TextView.BufferType.EDITABLE);
            etHeight.setText(String.valueOf(userData.getHeight()), TextView.BufferType.EDITABLE);
            spinnerExercise.setSelection(userData.getExercise().ordinal());
            spinnerGoal.setSelection(userData.getGoal().ordinal());
        }
        tvKcal.setText(getString(R.string.total_kcal, userData.getKcal()));
    }


    @OnClick(R.id.btn_save)
    public void addProduct(View view) {
        if (!validate())
            return;
        UserData userData = new UserData();
        userData.setAge(Integer.parseInt(etAge.getText().toString()));
        userData.setGender(rgGender.getCheckedRadioButtonId() == R.id.rb_female ? UserData.GENDER.FEMALE : UserData.GENDER.MALE);
        userData.setWeight(Integer.parseInt(etWeight.getText().toString()));
        userData.setHeight(Integer.parseInt(etHeight.getText().toString()));
        userData.setExercise(UserData.EXERCISE.values()[spinnerExercise.getSelectedItemPosition()]);
        userData.setGoal(UserData.GOAL.values()[spinnerGoal.getSelectedItemPosition()]);
        int kcal = Utils.getCaloriesNeed(userData);
        userData.setKcal(kcal);
        tvKcal.setText(getString(R.string.total_kcal, kcal));

        Utils.updateUserData(this, userData);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
