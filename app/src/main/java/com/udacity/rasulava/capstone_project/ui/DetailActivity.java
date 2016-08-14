package com.udacity.rasulava.capstone_project.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.udacity.rasulava.capstone_project.R;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Maryia on 09.07.2016.
 */
public class DetailActivity extends TrackedActivity {

    public static final String EXTRA_DATE = "date";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DetailsFragment detailsFragment = (DetailsFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_details);

        if (getIntent().hasExtra(EXTRA_DATE)) {
            date = (Date) getIntent().getSerializableExtra(EXTRA_DATE);
            detailsFragment.setDate(date);
        } else if (savedInstanceState != null) {
            date = (Date) savedInstanceState.getSerializable(EXTRA_DATE);
            detailsFragment.setDate(date);
        } else finish();
    }

    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(EXTRA_DATE, date);
        super.onSaveInstanceState(outState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        date = (Date) savedInstanceState.getSerializable(EXTRA_DATE);
    }

}