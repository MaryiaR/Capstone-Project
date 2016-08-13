package com.udacity.rasulava.capstone_project.ui;

import android.app.Activity;
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
import com.udacity.rasulava.capstone_project.R;
import com.udacity.rasulava.capstone_project.Utils;
import com.udacity.rasulava.capstone_project.db.DBHelper;
import com.udacity.rasulava.capstone_project.db.Intake;
import com.udacity.rasulava.capstone_project.db.Product;
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
public class DetailsFragment extends Fragment {

    public static final String POS = "pos";
    public static final int REQUEST_CODE_SEARCH = 100;

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

    private String date;

    private IntakeAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, rootView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new IntakeAdapter();
        mRecyclerView.setAdapter(adapter);

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

    public void setDate(String date) {
        this.date = date;
        if (!Utils.dateToString(new Date()).equalsIgnoreCase(date)) {
            addFab.setVisibility(View.GONE);
        }
        updateData();
    }

    private void updateData() {
        List<Intake> list = DBHelper.getInstance(getActivity()).getHistoryForDate(date);
        HistoryItem item = Utils.intakesToHistoryItem(list);
        adapter.setList(list);

        int kcalDaily = Utils.getDailyKcal(getActivity());
        tvDate.setText(getString(R.string.date_today, date));
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

        private List<Intake> list = new ArrayList<>();

        public void setList(List<Intake> list) {
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
                Intake item = list.get(position - 1);
                Product product = item.getProduct();

                holder.tvName.setText(product.getName());
                holder.tvWeight.setText(String.valueOf(item.getWeight()));
                holder.tvFat.setText(String.valueOf(product.getFat()));
                holder.tvCarbs.setText(String.valueOf(product.getCarbohydrate()));
                holder.tvProt.setText(String.valueOf(product.getProtein()));
                holder.tvKcal.setText(String.valueOf(product.getCalories()));
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

    public class HeaderViewHolder extends IntakeViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
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
    }
}
