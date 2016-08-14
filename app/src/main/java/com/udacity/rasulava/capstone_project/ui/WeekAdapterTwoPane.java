package com.udacity.rasulava.capstone_project.ui;

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
public class WeekAdapterTwoPane extends RecyclerView.Adapter<WeekAdapterTwoPane.TabletDateViewHolder> {

    private OnDayClickListener listener;
    private List<HistoryItem> list = new ArrayList<>();
    private int selectedPos = 0;

    public WeekAdapterTwoPane(OnDayClickListener listener) {
        this.listener = listener;
    }

    public void setList(List<HistoryItem> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public int getSelectedPos() {
        return selectedPos;
    }

    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
    }

    @Override
    public TabletDateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_day_item, parent, false);
        return new TabletDateViewHolder(v);
    }

    public HistoryItem getItem(int position) {
        return list.get(position);
    }

    @Override
    public void onBindViewHolder(TabletDateViewHolder holder, int position) {
        HistoryItem item = list.get(position);
        holder.tvDate.setText(Utils.dateToString(item.getDate()));
        holder.itemView.setSelected(selectedPos == position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TabletDateViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_date)
        TextView tvDate;


        public TabletDateViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setClickable(true);
        }

        @OnClick(R.id.tv_date)
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            listener.onDayClick(adapterPosition, getItem(adapterPosition).getDate());
            notifyItemChanged(getSelectedPos());
            setSelectedPos(adapterPosition);
            notifyItemChanged(adapterPosition);
        }
    }

}