package com.udacity.rasulava.capstone_project.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.rasulava.capstone_project.R;
import com.udacity.rasulava.capstone_project.Utils;
import com.udacity.rasulava.capstone_project.model.HistoryItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Maryia on 14.08.2016.
 */
public class WeekAdapterOnePane extends RecyclerView.Adapter<WeekAdapterOnePane.DateViewHolder> {

    private static final int HEADER_VIEW = 1;
    private List<HistoryItem> list = new ArrayList<>();
    private OnDayClickListener listener;
    private Context context;


    public void setList(List<HistoryItem> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public WeekAdapterOnePane(Context context, OnDayClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public DateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == HEADER_VIEW) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_header, parent, false);
            return new HeaderViewHolder(v);
        }
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new DateViewHolder(v);
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
            holder.tvKcal.setText(context.getString(R.string.history_kcal, item.getKcal()));
            holder.tvFcp.setText(context.getString(R.string.history_fcp, item.getFat(), item.getCarbs(), item.getProtein()));
        }
    }

    @Override
    public int getItemCount() {
        if (list.isEmpty()) {
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

    public class HeaderViewHolder extends DateViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
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

        public DateViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.rl_root)
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            listener.onDayClick(adapterPosition, getItem(adapterPosition - 1).getDate());
        }
    }
}