package com.shs.app.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.shs.app.Class.User3;
import com.shs.app.R;

import java.util.List;

public class UserAdapte2 extends RecyclerView.Adapter<UserAdapte2.ViewHolder> {
    private List<User3> userList;
    private Context context;

    public UserAdapte2(List<User3> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User3 user = userList.get(position);

        // Check if user data is not null before setting to TextViews
        if (user.getName() != null) {
            holder.fullname.setText("Name: " + user.getName());
        } else {
            holder.fullname.setText("Name: N/A");
        }

        if (user.getEmail() != null) {
            holder.email.setText("Email: " + user.getEmail());
        } else {
            holder.email.setText("Email: N/A");
        }

        // Check and set other fields similarly
        if (user.getPhone() != null) {
            holder.phone.setText("Phone#: " + String.valueOf(user.getPhone()));
        } else {
            holder.phone.setText("Phone#: N/A");
        }

        if (user.getBirthday() != null) {
            holder.birthday.setText("Birthday: " + user.getBirthday());
        } else {
            holder.birthday.setText("Birthday: N/A");
        }

        holder.cardView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.recycler_animation3));

        // Load image with Glide and apply CircleCrop transformation
        RequestOptions requestOptions = new RequestOptions().transforms(new CenterCrop(), new CircleCrop());
        Glide.with(context)
                .load(user.getImage())
                .apply(requestOptions)
                .into(holder.userImageView);
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void updateList(List<User3> newList) {
        userList.clear();
        userList.addAll(newList);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userImageView;
        TextView fullname,birthday,email,phone; // Corrected view ID
        CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            birthday = itemView.findViewById(R.id.birthday);
            email = itemView.findViewById(R.id.email);
            phone = itemView.findViewById(R.id.contact);
            userImageView = itemView.findViewById(R.id.userImageView);
            fullname = itemView.findViewById(R.id.fullname); // Corrected view ID
            cardView = itemView.findViewById(R.id.cardView);

        }
    }
}