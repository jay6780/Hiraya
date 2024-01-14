package com.shs.app.Adapter.CommentAdapter;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shs.app.Class.comment.Comment;
import com.shs.app.DialogUtils.Dialog;
import com.shs.app.R;
import com.squareup.picasso.Picasso;

import java.util.List;
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> commentList;

    public CommentAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Comment comment = commentList.get(position);;

        // Load user image using Picasso
        RequestOptions requestOptions = new RequestOptions().transform(new CircleCrop());
        Glide.with(holder.itemView.getContext())
                .load(comment.getImage()) // Use getImageUrl() to get the image URL
                .apply(requestOptions)
                .placeholder(R.drawable.ic_baseline_person_27)
                .error(R.drawable.baseline_person_24)
                .into(holder.userImageView);

        holder.userNameTextView.setText(comment.getName()); // Use getFullName() to get the user's full name

        // Check if the comment has text content
        if (comment.getMessageText() != null && !comment.getMessageText().isEmpty()) {
            holder.commentTextView.setVisibility(View.VISIBLE);
            holder.commentTextView.setText(comment.getMessageText()); // Use getMessageText() to get the comment text
        } else {
            holder.commentTextView.setVisibility(View.GONE);
        }
        if (comment.getFilename() != null && !comment.getFilename().isEmpty()) {
            holder.fileNAME.setVisibility(View.VISIBLE);
            holder.fileNAME.setText(comment.getFilename());

            // Get the current user's UID
            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // Retrieve the admin UID from the "ADMIN" node
            DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference().child("ADMIN");
            adminRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Allow the admin to download the file
                        holder.fileNAME.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                downloadFile(holder.itemView.getContext(), comment.getFilename(), comment.getFileUrl());
                            }
                        });
                    } else {
                        // Only show permission dialog if filename is not null
                        if (comment.getFilename() != null) {
                            Context context = holder.itemView.getContext();
                            Dialog permissions = new Dialog();
                            permissions.permission(context);
                            holder.fileNAME.setOnClickListener(null); // Disable the click event
                        } else {
                            // Handle the case where filename is null (optional)
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle potential errors here
                }
            });
        } else {
            holder.fileNAME.setVisibility(View.GONE);
        }


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // Get the current user's UID
                String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                // Retrieve the admin UID from the "ADMIN" node
                DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference().child("ADMIN");
                adminRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Only allow admin to delete comments
                            DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference().child("comments");
                            commentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        String commentId = snapshot.getKey();
                                        deleteComment(commentId, view.getContext());
                                        break;  // Only delete the first comment, modify as needed
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Handle potential errors here
                                    Toast.makeText(view.getContext(), "Error retrieving comments", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            // Show a message indicating that only admins can delete comments
                            Toast.makeText(view.getContext(), "Only admins can delete comments", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle potential errors here
                        Toast.makeText(view.getContext(), "Error checking admin status", Toast.LENGTH_SHORT).show();
                    }
                });

                return true;
            }
        });

        // Check if the comment has image content
        if (comment.getImageContent() != null && !comment.getImageContent().isEmpty()) {
            holder.imageContentImageView.setVisibility(View.VISIBLE);
            holder.imageContentImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle the click event here, open the WebView
                    openWebViewActivity(holder.itemView.getContext(), comment.getImageContent());
                }
            });

            // Load image content using Picasso
            Picasso.get()
                    .load(comment.getImageContent()) // Use getImageContent() to get the image content URL
                    .into(holder.imageContentImageView);
        } else {
            holder.imageContentImageView.setVisibility(View.GONE);
        }
    }

    public void deleteComment(String commentId, Context context) {
        DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference().child("comments").child(commentId);
        deleteSingleComment(commentRef, context);
    }

    private void deleteSingleComment(DatabaseReference commentRef, Context context) {
        commentRef.removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Comment deleted successfully
                            Toast.makeText(context, "Comment deleted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // Failed to delete the comment
                            Toast.makeText(context, "Failed to delete comment", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void downloadFile(Context context, String fileName, String fileUrl) {
        // Use DownloadManager to handle the file download
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(fileUrl); // Assuming fileUrl is the URL of the file to be downloaded

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

        // Enqueue the download and display a progress dialog
        long downloadId = downloadManager.enqueue(request);
        showDownloadProgressDialog(context, downloadId);
    }

    private void showDownloadProgressDialog(Context context, long downloadId) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Downloading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Set up a BroadcastReceiver to receive download progress updates
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
                    // Dismiss the progress dialog when the download is complete
                    progressDialog.dismiss();
                }
            }
        };

        // Register the BroadcastReceiver
        context.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void openWebViewActivity(Context context, String imageUrl) {
        // Inflate the WebView layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View webViewLayout = inflater.inflate(R.layout.dialog_webview_modal, null);

        // Find the WebView and ImageButton in the inflated layout
        WebView webView = webViewLayout.findViewById(R.id.webView);
        ImageButton buttonExit = webViewLayout.findViewById(R.id.buttonExit);

        // Load the image URL into the WebView
        webView.loadUrl(imageUrl);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setLoadWithOverviewMode(true); // Fit content to WebView's width
        webSettings.setUseWideViewPort(true); // Enable wide viewport
        webView.setInitialScale(1); // Set initial scale to 1 for better fit


        // Create a dialog to display the WebView
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setView(webViewLayout);
        final AlertDialog dialog = dialogBuilder.create();

        // Set a click listener for the exit button
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog when the exit button is clicked
                dialog.dismiss();
            }
        });

        // Show the dialog with the WebView
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public void setComments(List<Comment> comments) {
        commentList = comments; // Update the commentList with new data
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView userImageView;
        TextView userNameTextView;
        TextView fileNAME;
        TextView commentTextView;
        ImageView imageContentImageView; // Add ImageView for image content

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            fileNAME = itemView.findViewById(R.id.finameText);
            userImageView = itemView.findViewById(R.id.userImageView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            imageContentImageView = itemView.findViewById(R.id.imageContent); // Initialize imageContentImageView
        }
    }
}
