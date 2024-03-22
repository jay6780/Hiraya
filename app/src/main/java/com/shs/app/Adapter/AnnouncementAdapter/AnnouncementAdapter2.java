package com.shs.app.Adapter.AnnouncementAdapter;



import android.app.Activity;
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
import android.util.Log;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shs.app.Activity.Admin.Adminsettings.Admin;
import com.shs.app.Activity.Admin.Adminsettings.adminComment;
import com.shs.app.Activity.Student.StudentSettings.Quiz.Gen_Physics2;
import com.shs.app.Activity.Student.StudentSettings.Quiz.Mil_quizActivity;
import com.shs.app.Activity.Student.StudentSettings.Quiz.PEquiz;
import com.shs.app.Activity.Student.StudentSettings.Quiz.Research_project_quizType;
import com.shs.app.Activity.Student.StudentSettings.Quiz.generalchemistry2_quiz;
import com.shs.app.Activity.Student.StudentSettings.Quiz.practicalResearch2_quiz;
import com.shs.app.Activity.Student.StudentSettings.Student;
import com.shs.app.Activity.Student.StudentSettings.commentStuddents;
import com.shs.app.Class.Announce.Announcement;
import com.shs.app.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnnouncementAdapter2 extends ArrayAdapter<Announcement> {
    private ViewHolder currentViewHolder;
    private static class ViewHolder {
        TextView titleTextView,teacherSub;
        TextView contentTextView;
        TextView timeTextView,likeCountTextView,commentCounts;
        TextView dateTextView;
        TextView fullNameTextView;
        ImageView imageView,userImage;

        ImageButton commentBtn;
        ImageButton likeThumb;
    }

    private Dialog webViewDialog;

    public AnnouncementAdapter2(Context context, List<Announcement> announcements) {
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_announcement2, parent, false);
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
            viewHolder.commentBtn = convertView.findViewById(R.id.commentBtn);
            viewHolder.teacherSub = convertView.findViewById(R.id.subjectName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        currentViewHolder = viewHolder;

        boolean isLiked = getLikeStatus(announcement.getId(), getCurrentUserUID(),viewHolder);

        if (isLiked) {
            viewHolder.likeThumb.setImageResource(R.drawable.baseline_thumb_up_25);
        } else {
            viewHolder.likeThumb.setImageResource(R.drawable.baseline_thumb_up_24);
        }

        if (announcement != null) {
            viewHolder.titleTextView.setText(announcement.getTitle());
            viewHolder.likeCountTextView.setText(String.valueOf(announcement.getLikesCount()));
            viewHolder.timeTextView.setText(announcement.getTime());
            viewHolder.teacherSub.setText(announcement.getTeacherSubject());
            viewHolder.dateTextView.setText(announcement.getDate());
            viewHolder.fullNameTextView.setText(announcement.getName());

            SpannableString spannableTitle = new SpannableString(announcement.getTitle());

            spannableTitle.setSpan(new android.text.style.ForegroundColorSpan(Color.RED), 0, spannableTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableTitle.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, spannableTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            viewHolder.titleTextView.setText(spannableTitle);

            // Inside getView method
            viewHolder.titleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = announcement.getTitle();
                    String fileUrl = announcement.getFileUrl();
                    //intentPutExtra check if has quiz
                    if (title.contains("Gen_Physics2")) {
                        Intent gP = new Intent(getContext(), Gen_Physics2.class);
                        gP.putExtra("title", title);
                        getContext().startActivity(gP);
                        ((Activity) getContext()).overridePendingTransition(0, 0); // Disable animation
                        ((Activity) getContext()).finish();
                    } else if (title.contains("PE")) {
                        Intent peIntent = new Intent(getContext(), PEquiz.class);
                        peIntent.putExtra("title", title);
                        getContext().startActivity(peIntent);
                        ((Activity) getContext()).overridePendingTransition(0, 0); // Disable animation
                        ((Activity) getContext()).finish();

                    } else if (title.contains("generalchemistry2")) {
                        Intent gE = new Intent(getContext(), generalchemistry2_quiz.class);
                        gE.putExtra("title", title);
                        getContext().startActivity(gE);
                        ((Activity) getContext()).overridePendingTransition(0, 0); // Disable animation
                        ((Activity) getContext()).finish();

                    } else if (title.contains("Practical_Research2")) {
                        Intent Pr2 = new Intent(getContext(), practicalResearch2_quiz.class);
                        Pr2.putExtra("title", title);
                        getContext().startActivity(Pr2);
                        ((Activity) getContext()).overridePendingTransition(0, 0); // Disable animation
                        ((Activity) getContext()).finish();

                    } else if (title.contains("Research_project")) {
                        Intent Rp = new Intent(getContext(), Research_project_quizType.class);
                        Rp.putExtra("title", title);
                        getContext().startActivity(Rp);
                        ((Activity) getContext()).overridePendingTransition(0, 0); // Disable animation
                        ((Activity) getContext()).finish();

                    } else if (title.contains("MIL")) {
                        Intent mil = new Intent(getContext(), Mil_quizActivity.class);
                        mil.putExtra("title", title);
                        getContext().startActivity(mil);
                        ((Activity) getContext()).overridePendingTransition(0, 0); // Disable animation
                        ((Activity) getContext()).finish();

                    } else if (fileUrl != null && !fileUrl.isEmpty() && isValidUrl(fileUrl)) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fileUrl));
                        getContext().startActivity(browserIntent);
                    } else {
                        // Handle other cases or show a toast
                        Toast.makeText(getContext(), "Invalid title or file URL not found", Toast.LENGTH_SHORT).show();
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

            if (announcement.getAdminImg() != null) {
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.baseline_person_24)
                        .transform(new CircleCrop()); // Transforming the image into a circle

                Glide.with(getContext())
                        .load(announcement.getAdminImg())
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
                    updateLikeUI(viewHolder,announcement.isLiked()); // Update like UI
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
            viewHolder.commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create an Intent to open the CommentActivity
                    Intent intent = new Intent(getContext(), commentStuddents.class);
                    // Pass the announcement details as extras
                    intent.putExtra("announcementId", announcement.getId()); // Pass the announcement ID
                    intent.putExtra("title", announcement.getTitle());
                    intent.putExtra("content", announcement.getContent());
                    intent.putExtra("time", announcement.getTime());
                    intent.putExtra("date", announcement.getDate());
                    intent.putExtra("name", announcement.getName());
                    intent.putExtra("imageUrl", announcement.getImageUrl());
                    intent.putExtra("adminImg", announcement.getAdminImg());
                    intent.putExtra("teacherSubject", announcement.getTeacherSubject());
                    getContext().startActivity(intent);
                    ((Activity) getContext()).overridePendingTransition(0, 0); // Disable animation
                    ((Activity) getContext()).finish();
                }
            });

        }

        return convertView;
    }


    private boolean isValidUrl(String url) {
        Pattern pattern = Patterns.WEB_URL;
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

    private void updateLikeUI(AnnouncementAdapter2.ViewHolder holder, boolean isLiked) {
        if (isLiked) {
            holder.likeThumb.setImageResource(R.drawable.baseline_thumb_up_25);
        } else {
            holder.likeThumb.setImageResource(R.drawable.baseline_thumb_up_24);
        }
    }
    private boolean getLikeStatus(String announcementId, String currentUserUID, AnnouncementAdapter2.ViewHolder viewHolder) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Task").child(announcementId).child("likes");

        // Check if the user has liked the announcement
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isLiked = dataSnapshot.hasChild(currentUserUID);
                updateLikeUI(viewHolder, isLiked);
                Log.d("LikeStatus", "Announcement ID: " + announcementId + ", User ID: " + currentUserUID + ", Liked: " + isLiked);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("LikeStatus", "Error retrieving like status", databaseError.toException());
            }
        });
        return false;
    }

    private void likeFood(Announcement announcement, String announcementId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Task").child(announcementId);
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
                    updateLikeUI(currentViewHolder, announcement.isLiked());
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
                    updateLikeUI(currentViewHolder, announcement.isLiked());
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
            final RelativeLayout webViewContainer = webViewDialog.findViewById(R.id.webViewContainer);
            final ImageButton fullscreenButton = webViewDialog.findViewById(R.id.fullscreenButton);

            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(url);

            // Enable zoom controls
            WebSettings webSettings = webView.getSettings();
            webSettings.setUseWideViewPort(true);
            webSettings.setJavaScriptEnabled(true);
            webSettings.setLoadWithOverviewMode(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setDisplayZoomControls(false);

            fullscreenButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleFullscreen(webView, webViewContainer, fullscreenButton);
                }
            });


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

    private void toggleFullscreen(WebView webView, RelativeLayout webViewContainer, ImageButton fullscreenButton) {
        if (webViewContainer.getSystemUiVisibility() == View.SYSTEM_UI_FLAG_VISIBLE) {
            // Switch to fullscreen
            webViewContainer.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webViewDialog.setCancelable(false);
            fullscreenButton.setVisibility(View.GONE);
        } else {
            // Switch to normal mode
            webViewContainer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            webView.getSettings().setLoadWithOverviewMode(false);
            webView.getSettings().setUseWideViewPort(false);
            webViewDialog.setCancelable(true);
            fullscreenButton.setVisibility(View.VISIBLE);
        }
    }
}