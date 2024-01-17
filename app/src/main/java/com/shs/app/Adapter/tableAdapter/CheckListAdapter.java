package com.shs.app.Adapter.tableAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shs.app.Class.Student.Students;
import com.shs.app.R;

import java.util.List;

public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.ViewHolder> {

    private List<Students> studentList;
    private Context context;

    public CheckListAdapter(List<Students> studentList, Context context) {
        this.studentList = studentList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Students student = studentList.get(position);

        // Set the name
        holder.nameTextView.setText(student.getName());

        // Load the image using Glide
        RequestOptions requestOptions = new RequestOptions().circleCrop();

        Glide.with(context)
                .load(student.getImage())
                .apply(requestOptions)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.student_image);
            nameTextView = itemView.findViewById(R.id.student_name);
        }
    }
}
