package com.udacity.rasulava.capstone_project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.udacity.rasulava.capstone_project.CaloriesApplication;
import com.udacity.rasulava.capstone_project.R;
import com.udacity.rasulava.capstone_project.Utils;
import com.udacity.rasulava.capstone_project.db.DBHelper;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

public class MainActivity extends TrackedActivity implements OnDayClickListener {

    private static final String DETAILS_FRAGMENT_TAG = "DETAILS_TAG";

    @Nullable
    @BindView(R.id.container_details)
    FrameLayout containerDetails;

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!Utils.isGooglePlayServicesAvailable(this))
            ((CaloriesApplication) getApplication()).setGoogleServicesAvailable(false);
        ButterKnife.bind(this);
        if (new DBHelper(this).deleteOldData())
            Utils.trackEvent((CaloriesApplication) getApplication(), "db", "delete_old_data");
        if (containerDetails != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                Bundle args = new Bundle();
                args.putSerializable(DetailsFragment.DATE, new Date());

                DetailsFragment fragment = new DetailsFragment();
                fragment.setArguments(args);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_details, fragment, DETAILS_FRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Optional
    @OnClick(R.id.btn_settings)
    public void showSettings(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    @Override
    public void onDayClick(int pos, Date date) {
        if (mTwoPane) {
            ((DetailsFragment) getSupportFragmentManager().findFragmentByTag(DETAILS_FRAGMENT_TAG)).setDate(date);
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_DATE, date);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        }
    }

}
