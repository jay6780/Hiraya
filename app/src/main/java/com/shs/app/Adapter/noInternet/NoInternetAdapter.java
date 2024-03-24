package com.shs.app.Adapter.noInternet;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shs.app.R;

import java.util.ArrayList;

public class NoInternetAdapter extends BaseAdapter {
    private Context context;

    public NoInternetAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        // We just need one item for displaying the message
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_no_internet, parent, false);
            holder = new ViewHolder();
            holder.textView = convertView.findViewById(R.id.noInternet);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    static class ViewHolder {
        TextView textView;
    }
}
