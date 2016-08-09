package com.udacity.rasulava.capstone_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements MainFragment.OnDayClickListener {

    private static final String DETAILS_FRAGMENT_TAG = "DETAILS_TAG";
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (findViewById(R.id.container_details) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                DetailsFragment fragment = new DetailsFragment();
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDayClick(int pos) {
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putInt(DetailsFragment.POS, pos);

            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_details, fragment, DETAILS_FRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            startActivity(intent);
        }
    }
}
