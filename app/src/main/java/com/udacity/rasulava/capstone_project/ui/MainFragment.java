package com.udacity.rasulava.capstone_project.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.udacity.rasulava.capstone_project.R;
import com.udacity.rasulava.capstone_project.Utils;
import com.udacity.rasulava.capstone_project.model.HistoryItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by Maryia on 09.07.2016.
 */
public class MainFragment extends Fragment {

    private OnDayClickListener listener;

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Nullable
    @BindView(R.id.arc_progress)
    ArcProgress arcProgress;

    @Nullable
    @BindView(R.id.tv_date)
    TextView tvDateToday;

    @Nullable
    @BindView(R.id.tv_fpc)
    TextView tvFpcToday;

    @BindView(R.id.rv_history)
    RecyclerView mRecyclerView;

    private RecyclerView.Adapter adapter;

    private boolean isTabletMode;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnDayClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnDayClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);

        if (toolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        } else {
            isTabletMode = true;
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (isTabletMode)
            adapter = new WeekAdapterTwoPane(listener);
        else
            adapter = new WeekAdapterOnePane(getActivity(), listener);
        mRecyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateViews();
    }

    private void updateViews() {

        List<HistoryItem> list = new ArrayList<>();
        HistoryItem today = Utils.getTodayData(getActivity());
        if (!isTabletMode) {
            int kcalToday = today.getKcal();
            int kcalDaily = Utils.getDailyKcal(getActivity());
            tvDateToday.setText(getString(R.string.date_today, Utils.dateToString(new Date())));
            tvFpcToday.setText(getString(R.string.today_fcp, today.getFat(), today.getCarbs(), today.getProtein()));
            int max = kcalDaily;
            int finishedColor = getResources().getColor(R.color.colorAccent);
            int textColor = Color.WHITE;
            if (kcalToday > kcalDaily) {
                max = kcalToday;
                finishedColor = getResources().getColor(R.color.colorLimit);
                textColor = getResources().getColor(R.color.colorLimit);
            }

            arcProgress.setMax(max);
            arcProgress.setFinishedStrokeColor(finishedColor);
            arcProgress.setTextColor(textColor);
            arcProgress.setProgress(kcalToday);
        } else {
            list.add(today);
        }
        list.addAll(Utils.getHistoryForAWeek(getActivity()));

        if (isTabletMode) {
            ((WeekAdapterTwoPane) adapter).setList(list);
        } else {
            ((WeekAdapterOnePane) adapter).setList(list);
        }
    }


    @Optional
    @OnClick(R.id.toolbar)
    public void showDetails(View view) {
        listener.onDayClick(0, new Date());
    }

}
