package com.udacity.rasulava.capstone_project;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Maryia on 09.07.2016.
 */
public class MainFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private OnDayClickListener listener;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_history);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new RecyclerView.Adapter<PlanetViewHolder>() {

            @Override
            public PlanetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(
                        android.R.layout.simple_list_item_1,
                        parent,
                        false);
                PlanetViewHolder vh = new PlanetViewHolder(v, listener);
                return vh;
            }

            @Override
            public void onBindViewHolder(PlanetViewHolder vh, int position) {
                TextView tv = (TextView) vh.itemView;
                tv.setText("day "+ position);
            }

            @Override
            public int getItemCount() {
                return 6;
            }
        });

        return rootView;
    }

    public class PlanetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView text;
        private OnDayClickListener listener;

        public PlanetViewHolder(View itemView, OnDayClickListener listener) {
            super(itemView);
            this.listener = listener;
            text = (TextView) itemView.findViewById(R.id.text_id);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            listener.onDayClick(adapterPosition);
        }
    }

    interface OnDayClickListener {
        void onDayClick(int pos);
    }
}
