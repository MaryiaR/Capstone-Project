package com.udacity.rasulava.capstone_project;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by Maryia on 09.07.2016.
 */
public class DetailsFragment extends Fragment {

    public static final String POS = "pos";
    private TableLayout mTableLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        mTableLayout = (TableLayout) rootView.findViewById(R.id.tl_food);
        for (int i = 0; i < 6; i++) {
            addTableRow();
        }
        return rootView;
    }

    private void addTableRow() {
        final TableRow tr = (TableRow) getActivity().getLayoutInflater().inflate(R.layout.table_row_item, null);

        TextView tv;
        tv = (TextView) tr.findViewById(R.id.tv_food_name);
        tv.setText("food");

        tv = (TextView) tr.findViewById(R.id.tv_weight);
        tv.setText("100");

        tv = (TextView) tr.findViewById(R.id.tv_fat);
        tv.setText("food");

        tv = (TextView) tr.findViewById(R.id.tv_carbs);
        tv.setText("100");

        tv = (TextView) tr.findViewById(R.id.tv_prot);
        tv.setText("food");

        tv = (TextView) tr.findViewById(R.id.tv_kcal);
        tv.setText("100");

        mTableLayout.addView(tr);

        // Draw separator
        tv = new TextView(getActivity());
        tv.setBackgroundColor(Color.parseColor("#80808080"));
        tv.setHeight(1);
        mTableLayout.addView(tv);

        // If you use context menu it should be registered for each table row
        registerForContextMenu(tr);
    }
}
