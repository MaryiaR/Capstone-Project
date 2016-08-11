package com.udacity.rasulava.capstone_project;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.udacity.rasulava.capstone_project.model.Food;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mrasulava on 8/10/2016.
 */
public class SearchProductAdapter extends ArrayAdapter<Food> {

    private Activity context;
    private LayoutInflater inflater;
    private Filter filter;
    private String TAG = SearchProductAdapter.class.getSimpleName();
    private OnProductsSearchListener listener;

    public SearchProductAdapter(Activity context, List<Food> listAdapterContainers, OnProductsSearchListener listener) {
        super(context, 0, listAdapterContainers);
        this.context = context;
        filter = new ProductFilter();
        inflater = LayoutInflater.from(this.context);
        this.listener = listener;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            SearchViewHolder vh = new SearchViewHolder(v, position);
            v.setTag(vh);
        }
        SearchViewHolder vh = (SearchViewHolder) v.getTag();
        vh.position = position;
        Food food = getItem(position);
        vh.text.setText(food.getName());
        return v;
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        @BindView(android.R.id.text1)
        TextView text;

        int position;

        public SearchViewHolder(View itemView, int position) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.position = position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        @OnClick(android.R.id.text1)
        public void onClick(View view) {
            listener.onProductSelected(getItem(position).getId());
        }
    }

    public class ProductFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults result = new FilterResults();
            final List<Food> resultList = new ArrayList<>();
            // if constraint is empty return the original names
            if (!TextUtils.isEmpty(constraint)) {
                String filterString = constraint.toString().toLowerCase();
                resultList.addAll(new RequestHelper().getFood(context, filterString));
            }
            result.values = resultList;
            Log.v("", "Filtered count " + resultList.size());
            result.count = resultList.size();
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();

            try {
                List<Food> foodList = (List<Food>) results.values;
                if (results.count == 0)
                    listener.onSearchEmpty();
                else
                    listener.onSearchSuccessful();

                for (Food food : foodList) {
                    add(food);
                }
                notifyDataSetChanged();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
    }

}

