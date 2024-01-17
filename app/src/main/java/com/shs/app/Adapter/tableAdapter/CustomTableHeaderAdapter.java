package com.shs.app.Adapter.tableAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.codecrafters.tableview.TableHeaderAdapter;

public class CustomTableHeaderAdapter extends TableHeaderAdapter {

    private final String[] headers;
    private final Context context;

    public CustomTableHeaderAdapter(Context context, String... headers) {
        super(context);
        this.context = context;
        this.headers = headers;
    }

    @Override
    public View getHeaderView(int columnIndex, ViewGroup parentView) {
        TextView textView = new TextView(context);
        textView.setText(headers[columnIndex]);
        textView.setTextSize(12);  // Set the desired text size for the header
        return textView;
    }
}
