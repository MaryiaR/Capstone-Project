package com.udacity.rasulava.capstone_project.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
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

/**
 * Created by Maryia on 09.07.2016.
 */
public class MainFragment extends Fragment {

    private OnDayClickListener listener;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.arc_progress)
    ArcProgress arcProgress;

    @BindView(R.id.tv_date)
    TextView tvDateToday;

    @BindView(R.id.tv_fpc)
    TextView tvFpcToday;

    @BindView(R.id.rv_history)
    RecyclerView mRecyclerView;

    private WeekAdapter adapter;

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

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new WeekAdapter();
        mRecyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateViews();
    }

    private void updateViews() {

        HistoryItem today = Utils.getTodayData(getActivity());
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

        List<HistoryItem> list = Utils.getHistoryForAWeek(getActivity());
        adapter.setList(list);

    }

    class WeekAdapter extends RecyclerView.Adapter<DateViewHolder> {

        private static final int HEADER_VIEW = 1;
        private List<HistoryItem> list = new ArrayList<>();

        public void setList(List<HistoryItem> list) {
            this.list.clear();
            this.list.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public DateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;
            if (viewType == HEADER_VIEW) {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_header, parent, false);
                return new HeaderViewHolder(v);
            }
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
            return new DateViewHolder(v, listener);
        }

        public HistoryItem getItem(int position) {
            return list.get(position);
        }

        @Override
        public void onBindViewHolder(DateViewHolder holder, int position) {
            if (holder instanceof HeaderViewHolder) {
                return;
            } else {
                HistoryItem item = list.get(position - 1);
                holder.tvDate.setText(Utils.dateToString(item.getDate()));
                holder.tvKcal.setText(getString(R.string.history_kcal, item.getKcal()));
                holder.tvFcp.setText(getString(R.string.history_fcp, item.getFat(), item.getCarbs(), item.getProtein()));
            }
        }

        @Override
        public int getItemCount() {
            if (list == null || list.isEmpty()) {
                return 0;
            }
            return list.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return HEADER_VIEW;
            }
            return super.getItemViewType(position);
        }
    }

    public class HeaderViewHolder extends DateViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView, null);
        }

        @OnClick(R.id.rl_root)
        public void onClick(View view) {
        }
    }

    public class DateViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_date)
        TextView tvDate;

        @BindView(R.id.tv_kcal)
        TextView tvKcal;

        @BindView(R.id.tv_fcp)
        TextView tvFcp;

        private OnDayClickListener listener;

        public DateViewHolder(View itemView, OnDayClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;
        }

        @OnClick(R.id.rl_root)
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            listener.onDayClick(adapterPosition, getDateForPosition(adapterPosition - 1));
        }
    }

    interface OnDayClickListener {
        void onDayClick(int pos, Date date);
    }

    @OnClick(R.id.toolbar)
    public void showDetails(View view) {
        listener.onDayClick(0, new Date());
    }

    private Date getDateForPosition(int pos) {
        return adapter.getItem(pos).getDate();
    }
}
