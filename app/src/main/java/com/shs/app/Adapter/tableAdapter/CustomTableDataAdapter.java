package com.shs.app.Adapter.tableAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.codecrafters.tableview.TableDataAdapter;

public class CustomTableDataAdapter extends TableDataAdapter<String[]> {

    public CustomTableDataAdapter(Context context, String[][] data) {
        super(context, data);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        String[] rowData = getRowData(rowIndex); // <-- This is likely causing the issue
        String cellText = rowData[columnIndex];  // <-- This is likely causing the issue

        TextView textView = new TextView(getContext());
        textView.setText(cellText);
        textView.setTextSize(12); // Set the desired text size

        return textView;
    }
}
