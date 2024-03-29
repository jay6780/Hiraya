package com.shs.app.Activity.Admin.Adminsettings;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shs.app.Class.Announce.Announcement;
import com.shs.app.DialogUtils.Dialog_task;
import com.shs.app.R;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class addtastk extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText title, announce_content;
    private Button saveBtn;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private StorageReference storageReference;
    private Uri imageUri;
    private ImageView imagePick,task,addQuiz;
    TextView fullname;
    ImageView imageView2;
    private static final int FILE_SELECT_CODE = 0;
    private Uri fileUri;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; show dialog to confirm going back to menu
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to go back to the menu?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), assestment_activity.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        finish();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtastk);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add task");
        Drawable homeButton = getResources().getDrawable(R.drawable.ic_home);
// Set its color to black
        homeButton.setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
// Set the modified drawable as the home button icon
        getSupportActionBar().setHomeAsUpIndicator(homeButton);
        SpannableString text = new SpannableString(getSupportActionBar().getTitle());
        text.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(text);
        changeStatusBarColor(getResources().getColor(R.color.maroon));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7D0A0A")));
        fullname = findViewById(R.id.text7);
        imageView2 = findViewById(R.id.profileImageView);
        title = findViewById(R.id.titleTextView);
        task = findViewById(R.id.addtask);
        announce_content = findViewById(R.id.contentEditText);
        saveBtn = findViewById(R.id.saveButton);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving Announcement...");
        progressDialog.setCancelable(false);
        imagePick = findViewById(R.id.imagepick);
        addQuiz = findViewById(R.id.selectQuiz);

        String databaseReferenceName = getIntent().getStringExtra("databaseReferenceName");

        // Set the databaseReferenceName to the appropriate TextView

        title.setText(databaseReferenceName);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // User is already logged in
            // Perform necessary operations for logged-in user
            String email = user.getEmail();
            String phoneNumber = user.getPhoneNumber(); // Get the phone number

            if (email != null) {
                String displayText = phoneNumber;

                // Load the user's profile image using Glide (circular)
                DatabaseReference adminLoginRef = FirebaseDatabase.getInstance().getReference().child("ADMIN").child(user.getUid());
                adminLoginRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String fullName = dataSnapshot.child("name").getValue(String.class);
                            String fullNameText = fullName;
                            fullname.setText(fullNameText); // Display full name

                            // Load the user's profile image using Glide (circular)
                            String imageUrl = dataSnapshot.child("image").getValue(String.class);
                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                RequestOptions requestOptions = new RequestOptions().circleCrop();
                                Glide.with(addtastk.this)
                                        .load(imageUrl)
                                        .apply(requestOptions)
                                        .into(imageView2);
                            }
                        } else {

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle error
                    }
                });
            } else {
                fullname.setText("Email not set");
            }
        } else {
            // User is not logged in
            fullname.setText("User not logged in");
        }

        storageReference = FirebaseStorage.getInstance().getReference("TaskImage");
        databaseReference = FirebaseDatabase.getInstance().getReference("Task");

        addQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectQuizUpload();
            }
        });

        imagePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAnnouncement();
            }
        });
        task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFile();
            }
        });
    }

    private void selectQuizUpload() {
        Dialog_task dialog = new Dialog_task();
        dialog.showtaskDialog(addtastk.this);

    }

    private void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        String[] mimeTypes = {"application/pdf", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(Intent.createChooser(intent, "Select a file"), FILE_SELECT_CODE);
    }


    private void changeStatusBarColor(int color) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    private void saveAnnouncement() {
        String announcementTitle = title.getText().toString().trim();
        String announcementContent = announce_content.getText().toString().trim();

        if (TextUtils.isEmpty(announcementTitle) || TextUtils.isEmpty(announcementContent)) {
            // Show an error message if any field is empty
            Toast.makeText(this, "Please complete all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();

        // Generate a new unique key for the announcement
        String announcementId = databaseReference.push().getKey();

        // Create a new announcement object with the provided title, content, and ID
        Announcement announcement = new Announcement(announcementId, announcementTitle, announcementContent);

        // Add the current time and date to the announcement
        announcement.setTime(getCurrentTime());
        announcement.setDate(getCurrentDate());

        // Check if an image is selected
        if (imageUri != null) {
            uploadImage(announcementId, announcement);
        } else if (fileUri != null) {
            uploadFile(announcementId, announcement);
        } else {
            // Check whether the title is set from getIntent or not
            if (getIntent().getStringExtra("databaseReferenceName") != null) {
                // If the title is set from getIntent, proceed with saving the announcement without showing the toast
                saveAnnouncementToDatabase(announcementId, announcement);
            } else {
                // If the title is not set from getIntent, show the toast
                Toast.makeText(this, "Please upload an image or select a file", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }
    }


    // Add this method to handle file upload
    private void uploadFile(String announcementId, Announcement announcement) {
        progressDialog.setMessage("Uploading File...");

        // Create a reference to the location where the file will be stored in Firebase Storage
        StorageReference fileRef = FirebaseStorage.getInstance().getReference("TaskFiles").child(announcementId);

        // Upload the file to Firebase Storage
        UploadTask uploadTask = fileRef.putFile(fileUri);
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }

            // Retrieve the URL of the uploaded file
            return fileRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();

                // Set the file URL in the announcement object
                announcement.setFileUrl(downloadUri.toString());

                // Save the announcement to the database
                saveAnnouncementToDatabase(announcementId, announcement);
            } else {
                progressDialog.dismiss();
                showErrorDialog();
            }
        });
    }

    private void uploadImage(String announcementId, Announcement announcement) {
        progressDialog.setMessage("Uploading Image...");

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);

            while (byteArrayOutputStream.toByteArray().length / 1024 > 1024) {
                byteArrayOutputStream.reset();
                quality -= 10;
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
            }

            byte[] compressedImageData = byteArrayOutputStream.toByteArray();

            // Create a reference to the location where the image will be stored in Firebase Storage
            StorageReference imageRef = storageReference.child(announcementId + ".jpg");

            // Upload the compressed image to Firebase Storage
            UploadTask uploadTask = imageRef.putBytes(compressedImageData);
            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Retrieve the URL of the uploaded image
                return imageRef.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();

                    // Set the image URL in the announcement object
                    announcement.setImageUrl(downloadUri.toString());

                    // Save the announcement to the database
                    saveAnnouncementToDatabase(announcementId, announcement);
                } else {
                    progressDialog.dismiss();
                    showErrorDialog();
                }
            });
        } catch (IOException e) {
            Toast.makeText(this, "Failed to compress image", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

    private void saveAnnouncementToDatabase(String announcementId, Announcement announcement) {
        getUploaderFullName(new UploaderFullNameCallback() {
            @Override
            public void onFullNameReceived(String name) {
                if (name != null) {
                    announcement.setName(name);
                }

                databaseReference.child(announcementId).setValue(announcement).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Show a success message if the announcement is saved successfully
                            showCompletionDialog();
                        } else {
                            // Show an error message if there was an error while saving the announcement
                            showErrorDialog();
                        }
                    }
                });
            }
        });
    }

    private void getUploaderFullName(UploaderFullNameCallback callback) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uploaderId = currentUser.getUid();
            DatabaseReference adminReference = FirebaseDatabase.getInstance().getReference("ADMIN").child(uploaderId);
            adminReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        callback.onFullNameReceived(name);
                    } else {
                        callback.onFullNameReceived(null);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    callback.onFullNameReceived(null);
                }
            });
        } else {
            // Handle the case where the user is not authenticated
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            callback.onFullNameReceived(null);
        }
    }

    interface UploaderFullNameCallback {
        void onFullNameReceived(String fullName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            // Set the selected image to an ImageView if needed
            imagePick.setImageURI(imageUri);
        } else if (requestCode == FILE_SELECT_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            fileUri = data.getData();
            String fileName = getFileName(fileUri);
            if (title != null) {
                title.setText(fileName);
            }
        }
    }
    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        String suffix = (hourOfDay < 12) ? "am" : "pm";
        hourOfDay = (hourOfDay > 12) ? hourOfDay - 12 : hourOfDay;
        String time = String.format(Locale.getDefault(), "%02d:%02d %s", hourOfDay, minute, suffix);

        return time;
    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Format the date as "Month dd, yyyy" format
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        calendar.set(year, month, dayOfMonth);
        return sdf.format(calendar.getTime());
    }

    private void showCompletionDialog() {
        // Display a completion dialog with a 100% progress indication
        ProgressDialog completionDialog = new ProgressDialog(this);
        completionDialog.setMessage("Forum Upload successfully!");
        completionDialog.setProgress(100);
        completionDialog.setCancelable(true);
        completionDialog.show();

        // Automatically dismiss the dialog after 2 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                completionDialog.dismiss();

                // Clear the EditText fields
                title.setText("");
                announce_content.setText("");

                // Clear the selected image
                imageUri = null;
                imagePick.setImageURI(null);

                // Navigate to another activity (announce_view) after a successful upload
                Intent intent = new Intent(addtastk.this, assestment_activity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        }, 2000);
    }


    private void showErrorDialog() {
        // Display an error dialog if there was an error while saving the announcement
        ProgressDialog errorDialog = new ProgressDialog(this);
        errorDialog.setMessage("Error while saving announcement. Please try again.");
        errorDialog.setCancelable(true);
        errorDialog.show();
    }

    public void onBackPressed(){
        Toast.makeText(getApplicationContext(),"Closed",Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(),assestment_activity.class);
        startActivity(i);
        overridePendingTransition(0,0);
        finish();
        super.onBackPressed();
    }

}