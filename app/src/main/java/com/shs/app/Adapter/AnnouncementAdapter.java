package com.shs.app.Adapter;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shs.app.Activity.Admin.Adminsettings.adminComment;
import com.shs.app.Class.Announcement;
import com.shs.app.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnnouncementAdapter extends ArrayAdapter<Announcement> {
    private ViewHolder currentViewHolder;
    private static class ViewHolder {
        TextView titleTextView;
        TextView contentTextView;
        TextView timeTextView,likeCountTextView,commentCounts;
        TextView dateTextView;
        TextView fullNameTextView;
        ImageView imageView,userImage;
        AppCompatButton deleteButton;
        ImageButton commentBtn;
        ImageButton likeThumb;
    }

    private Dialog webViewDialog;

    public AnnouncementAdapter(Context context, List<Announcement> announcements) {
        super(context, 0, announcements);
        fetchLikedStatusForAnnouncements(announcements);

    }

    private void fetchLikedStatusForAnnouncements(List<Announcement> announcements) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Task");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        for (Announcement announcement : announcements) {
            if (user != null) {
                databaseReference.child(announcement.getId())
                        .child("likes")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                boolean isLiked = dataSnapshot.hasChild(user.getUid());
                                announcement.setLiked(isLiked);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle error
                            }
                        });
            }
        }
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Announcement announcement = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_announcement, parent, false);
            viewHolder.titleTextView = convertView.findViewById(R.id.titleTextView);
            viewHolder.commentCounts = convertView.findViewById(R.id.commentCount);
            viewHolder.likeCountTextView = convertView.findViewById(R.id.likecount);
            viewHolder.likeThumb = convertView.findViewById(R.id.likeThumb);
            viewHolder.userImage = convertView.findViewById(R.id.imageUser);
            viewHolder.contentTextView = convertView.findViewById(R.id.contentTextView);
            viewHolder.timeTextView = convertView.findViewById(R.id.timeTextView);
            viewHolder.dateTextView = convertView.findViewById(R.id.dateTextView);
            viewHolder.fullNameTextView = convertView.findViewById(R.id.fullNameTextView);
            viewHolder.imageView = convertView.findViewById(R.id.imageView);
            viewHolder.deleteButton = convertView.findViewById(R.id.deleteButton);
            viewHolder.commentBtn = convertView.findViewById(R.id.commentBtn);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        currentViewHolder = viewHolder;

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("LikedAnnouncements", Context.MODE_PRIVATE);
        boolean isLiked = getLikeStatus(announcement.getId(), sharedPreferences);

        if (isLiked) {
            viewHolder.likeThumb.setImageResource(R.drawable.baseline_thumb_up_25);
        } else {
            viewHolder.likeThumb.setImageResource(R.drawable.baseline_thumb_up_24);
        }

        if (announcement != null) {
            viewHolder.titleTextView.setText(announcement.getTitle());
            viewHolder.likeCountTextView.setText(String.valueOf(announcement.getLikesCount()));
            viewHolder.timeTextView.setText(announcement.getTime());
            viewHolder.dateTextView.setText(announcement.getDate());
            viewHolder.fullNameTextView.setText(announcement.getName());


            viewHolder.titleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String fileUrl = announcement.getFileUrl();
                    if (fileUrl != null && !fileUrl.isEmpty()) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fileUrl));
                        getContext().startActivity(browserIntent);
                    } else {
                        Toast.makeText(getContext(), "File URL not available", Toast.LENGTH_SHORT).show();
                    }
                }
            });



            // Load and display the image using Picasso
            if (announcement.getImageUrl() != null) {
                Picasso.get()
                        .load(announcement.getImageUrl())
                        .into(viewHolder.imageView);
                viewHolder.imageView.setVisibility(View.VISIBLE);
            } else {
                viewHolder.imageView.setImageResource(R.drawable.baseline_person_24);
                viewHolder.imageView.setVisibility(View.GONE);
            }

            if (announcement.getImage() != null) {
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.baseline_person_24)
                        .transform(new CircleCrop()); // Transforming the image into a circle

                Glide.with(getContext())
                        .load(announcement.getImageUrl())
                        .apply(requestOptions)
                        .into(viewHolder.userImage);
            } else {
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.baseline_person_24)
                        .error(R.drawable.baseline_person_24)
                        .transform(new CircleCrop()); // Transforming the placeholder image into a circle

                Glide.with(getContext())
                        .load(R.drawable.baseline_person_24)
                        .apply(requestOptions)
                        .into(viewHolder.userImage);
            }


            SpannableString spannableString = new SpannableString(announcement.getContent());

// Pattern for matching URLs
            Pattern pattern = Patterns.WEB_URL;
            Matcher matcher = pattern.matcher(announcement.getContent());

            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();

                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        String url = announcement.getContent().substring(start, end);
                        showWebViewDialog(url);
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setUnderlineText(true); // Add underline to the link
                        ds.setColor(Color.BLUE); // Set the link color
                    }
                };

                spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            viewHolder.contentTextView.setText(spannableString);
            viewHolder.contentTextView.setMovementMethod(LinkMovementMethod.getInstance());

            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showWebViewDialog(announcement.getImageUrl());
                }
            });

            viewHolder.likeThumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (announcement.isLiked()) {
                        unLikeFood(announcement, announcement.getId());
                    } else {
                        likeFood(announcement, announcement.getId());
                    }
                    updateLikeUI(viewHolder, announcement); // Update like UI
                }
            });



            Query commentsRef = FirebaseDatabase.getInstance().getReference()
                    .child("comments")
                    .child(announcement.getId()); // Use the announcement ID as the child node

            commentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int commentCount = (int) dataSnapshot.getChildrenCount(); // Count the number of comments for this specific announcement
                    viewHolder.commentCounts.setText(String.valueOf(commentCount));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                }
            });

            viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Confirm Delete");
                    builder.setMessage("Are you sure you want to delete this announcement?");

                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Remove the announcement from the list
                            remove(announcement);
                            notifyDataSetChanged();

                            // Delete the announcement from the database
                            DatabaseReference announcementReference = FirebaseDatabase.getInstance().getReference("Task");

                            announcementReference.child(announcement.getId()).removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getContext(), "Announcement deleted successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "Failed to delete announcement", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            // Delete the associated comments
                            DatabaseReference commentReference = FirebaseDatabase.getInstance().getReference("comments");
                            Query commentsQuery = commentReference.orderByChild("announcementId").equalTo(announcement.getId());

                            commentsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        snapshot.getRef().removeValue();
                                    }
                                    Toast.makeText(getContext(), "Comments deleted successfully", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(getContext(), "Failed to delete comments", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });

            viewHolder.commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create an Intent to open the CommentActivity
                    Intent intent = new Intent(getContext(), adminComment.class);
                    // Pass the announcement details as extras
                    intent.putExtra("announcementId", announcement.getId()); // Pass the announcement ID
                    intent.putExtra("title", announcement.getTitle());
                    intent.putExtra("content", announcement.getContent());
                    intent.putExtra("time", announcement.getTime());
                    intent.putExtra("date", announcement.getDate());
                    intent.putExtra("name", announcement.getName());
                    intent.putExtra("imageUrl", announcement.getImageUrl());
                    getContext().startActivity(intent);
                    ((Activity)getContext()).overridePendingTransition(0, 0); // Disable animation
                    ((Activity) getContext()).finish();
                }
            });

        }

        return convertView;
    }

    private void updateLikeUI(ViewHolder holder, Announcement announcement) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("LikedAnnouncements", Context.MODE_PRIVATE);

        if (announcement.isLiked()) {
            holder.likeThumb.setImageResource(R.drawable.baseline_thumb_up_25);
            // Save liked status in SharedPreferences
            saveLikeStatus(announcement.getId(), true, sharedPreferences);
        } else {
            holder.likeThumb.setImageResource(R.drawable.baseline_thumb_up_24);
            // Save unliked status in SharedPreferences
            saveLikeStatus(announcement.getId(), false, sharedPreferences);
        }
    }

    private void saveLikeStatus(String announcementId, boolean isLiked, SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(announcementId, isLiked);
        editor.apply();
    }

    private boolean getLikeStatus(String announcementId, SharedPreferences sharedPreferences) {
        return sharedPreferences.getBoolean(announcementId, false); // Default value is false
    }

    private void likeFood(Announcement announcement, String announcementId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Task").child(announcementId);

        // Check if the user has already liked the announcement
        databaseReference.child("likes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(getCurrentUserUID())) {
                    unLikeFood(announcement, announcementId);
                } else {
                    // If not liked, like it
                    long likes = dataSnapshot.getChildrenCount();
                    databaseReference.child("likes").child(getCurrentUserUID()).setValue(true);
                    databaseReference.child("likesCount").setValue(likes + 1);
                    announcement.setLiked(true);
                    notifyDataSetChanged();
                    updateLikeUI(currentViewHolder, announcement);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }


    private void unLikeFood(Announcement announcement, String announcementId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Task").child(announcementId);
        databaseReference.child("likes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(getCurrentUserUID())) {
                    long likes = dataSnapshot.getChildrenCount();
                    databaseReference.child("likes").child(getCurrentUserUID()).removeValue();
                    databaseReference.child("likesCount").setValue(likes - 1);
                    announcement.setLiked(false);
                    notifyDataSetChanged();
                    updateLikeUI(currentViewHolder, announcement);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private String getCurrentUserUID() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        }
        return "";
    }

    private void showWebViewDialog(String url) {
        if (webViewDialog == null) {
            webViewDialog = new Dialog(getContext());
            webViewDialog.setContentView(R.layout.dialog_webview);
            webViewDialog.setCancelable(true);

            final WebView webView = webViewDialog.findViewById(R.id.webView);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(url);

            // Enable zoom controls
            WebSettings webSettings = webView.getSettings();
            webSettings.setUseWideViewPort(true);
            webSettings.setJavaScriptEnabled(true);
            webSettings.setLoadWithOverviewMode(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setDisplayZoomControls(false);

            // Handle dialog close event
            webViewDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    // Clear the WebView and release resources
                    webView.loadUrl("about:blank");
                    webView.destroy();
                    webViewDialog = null;
                }
            });

            // Exit button
            ImageButton exitButton = webViewDialog.findViewById(R.id.exitButton);
            exitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    webViewDialog.dismiss();
                }
            });
        }

        webViewDialog.show();
    }
}
