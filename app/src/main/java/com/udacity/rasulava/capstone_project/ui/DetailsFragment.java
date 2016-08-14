package com.udacity.rasulava.capstone_project.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.udacity.rasulava.capstone_project.CaloriesApplication;
import com.udacity.rasulava.capstone_project.R;
import com.udacity.rasulava.capstone_project.Utils;
import com.udacity.rasulava.capstone_project.db.DBHelper;
import com.udacity.rasulava.capstone_project.model.HistoryItem;
import com.udacity.rasulava.capstone_project.model.IntakeItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by Maryia on 09.07.2016.
 */
public class DetailsFragment extends Fragment {

    public static final int REQUEST_CODE_SEARCH = 100;
    public static final String DATE = "date";

    @BindView(R.id.arc_progress)
    ArcProgress arcProgress;

    @BindView(R.id.tv_date)
    TextView tvDate;

    @BindView(R.id.tv_fpc)
    TextView tvFpc;

    @BindView(R.id.add_fab)
    FloatingActionButton addFab;

    @BindView(R.id.rv_food)
    RecyclerView mRecyclerView;

    private Date date;

    private IntakeAdapter adapter;
    private DBHelper dbHelper;
    private boolean isToday;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, rootView);

        dbHelper = new DBHelper(getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new IntakeAdapter();
        mRecyclerView.setAdapter(adapter);

        if (getArguments() != null && getArguments().getSerializable(DATE) != null) {
            date = (Date) getArguments().getSerializable(DATE);
            setDate(date);
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }

    @OnClick(R.id.add_fab)
    public void addProduct(View view) {
        Intent intent = new Intent(getActivity(), SearchProductActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SEARCH);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SEARCH && resultCode == Activity.RESULT_OK) {

        }
    }

    public void setDate(Date date) {
        this.date = date;
        isToday = Utils.dateToString(new Date()).equalsIgnoreCase(Utils.dateToString(date));
        if (!isToday) {
            addFab.setVisibility(View.GONE);
        } else
            addFab.setVisibility(View.VISIBLE);
        updateData();
    }

    private void updateData() {
        if (dbHelper == null)
            dbHelper = new DBHelper(getActivity());
        List<IntakeItem> list = dbHelper.getHistoryForDate(date);
        HistoryItem item = Utils.intakesToHistoryItem(list);
        adapter.setList(list);

        int kcalDaily = Utils.getDailyKcal(getActivity());
        if (isToday)
            tvDate.setText(getString(R.string.date_today, Utils.dateToString(date)));
        else
            tvDate.setText(Utils.dateToString(date));

        tvFpc.setText(getString(R.string.today_fcp, item.getFat(), item.getCarbs(), item.getProtein()));
        if (item.getKcal() > kcalDaily) {
            arcProgress.setMax(item.getKcal());
            arcProgress.setFinishedStrokeColor(getResources().getColor(R.color.colorLimit));
            arcProgress.setTextColor(getResources().getColor(R.color.colorLimit));
        } else {
            arcProgress.setMax(kcalDaily);
            arcProgress.setFinishedStrokeColor(getResources().getColor(R.color.colorAccent));
            arcProgress.setTextColor(Color.WHITE);
        }
        arcProgress.setProgress(item.getKcal());
    }


    class IntakeAdapter extends RecyclerView.Adapter<IntakeViewHolder> {

        private static final int HEADER_VIEW = 1;

        private List<IntakeItem> list = new ArrayList<>();

        public void setList(List<IntakeItem> list) {
            this.list.clear();
            this.list.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public IntakeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;

            if (viewType == HEADER_VIEW) {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.intake_list_item_header, parent, false);
                return new HeaderViewHolder(v);
            }

            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.intake_list_item, parent, false);
            return new IntakeViewHolder(v);
        }

        @Override
        public void onBindViewHolder(IntakeViewHolder holder, int position) {
            if (holder instanceof HeaderViewHolder) {
                return;
            } else {
                IntakeItem item = list.get(position - 1);

                holder.tvName.setText(item.getName());
                holder.tvWeight.setText(String.valueOf(item.getWeight()));
                holder.tvFat.setText(String.valueOf(item.getFat()));
                holder.tvCarbs.setText(String.valueOf(item.getCarbs()));
                holder.tvProt.setText(String.valueOf(item.getProtein()));
                holder.tvKcal.setText(String.valueOf(item.getKcal()));
            }
        }

        @Override
        public int getItemCount() {
            if (list == null || list.isEmpty()) {
                return 0;
            }
            return list.size() + 1;
        }

        public IntakeItem getItem(int position) {
            return list.get(position - 1);
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return HEADER_VIEW;
            }
            return super.getItemViewType(position);
        }
    }

    public class HeaderViewHolder extends IntakeViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }

        @OnLongClick(R.id.ll_root)
        public boolean onDeleteClick(View view) {

            return true;
        }
    }

    public class IntakeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_food_name)
        TextView tvName;

        @BindView(R.id.tv_weight)
        TextView tvWeight;

        @BindView(R.id.tv_fat)
        TextView tvFat;

        @BindView(R.id.tv_carbs)
        TextView tvCarbs;

        @BindView(R.id.tv_prot)
        TextView tvProt;

        @BindView(R.id.tv_kcal)
        TextView tvKcal;

        public IntakeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnLongClick(R.id.ll_root)
        public boolean onDeleteClick(View view) {
            final IntakeItem intakeItem = adapter.getItem(getAdapterPosition());
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(getString(R.string.delete_item, intakeItem.getName()))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dbHelper.delete(intakeItem.getId());
                                    updateData();
                                    Utils.trackEvent((CaloriesApplication) getActivity().getApplication(), "db", "delete_intake");
                                    dialog.cancel();
                                }
                            })
                    .setNegativeButton(getString(R.string.cancel), null);
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        }
    }
}
