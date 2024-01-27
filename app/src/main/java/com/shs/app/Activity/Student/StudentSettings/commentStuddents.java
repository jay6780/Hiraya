package com.shs.app.Activity.Student.StudentSettings;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shs.app.Adapter.CommentAdapter.CommentAdapter;
import com.shs.app.Class.Student.Students;
import com.shs.app.Class.comment.Comment;
import com.shs.app.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class commentStuddents extends AppCompatActivity {
    ImageButton picture, sendBtn;
    EditText commentEditText;
    RecyclerView commentsRecyclerView;
    private Uri selectedImageUri;
    private DatabaseReference commentsRef;
    private DatabaseReference studentsRef; // Firebase reference for Students
    private DatabaseReference adminRef;    // Firebase reference for Admin3
    private FirebaseUser currentUser;
    private CommentAdapter commentAdapter;
    private static final int PICK_IMAGE_REQUEST = 1;
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
                        Intent intent = new Intent(getApplicationContext(), Student.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        finish();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
                return true;
//            case R.id.action_refresh:
//                // refresh button clicked; implement refresh logic here
//                Toast.makeText(this, "Refreshing...", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getApplicationContext(), CommentActivity.class);
//                startActivity(intent);
//                overridePendingTransition(0,0);
//                finish();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_studdents);
        changeStatusBarColor(getResources().getColor(R.color.maroon));
        Drawable homeButton = getResources().getDrawable(R.drawable.ic_home);
// Set its color to black
        homeButton.setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
// Set the modified drawable as the home button icon
        getSupportActionBar().setHomeAsUpIndicator(homeButton);
        getSupportActionBar().setTitle("HIRAYA");
        SpannableString text = new SpannableString(getSupportActionBar().getTitle());
        text.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(text);
        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7D0A0A")));

        sendBtn = findViewById(R.id.sendMessageButton);
        commentEditText = findViewById(R.id.messageEditText);
        commentsRecyclerView = findViewById(R.id.recycler);

        // Initialize Firebase references
        studentsRef = FirebaseDatabase.getInstance().getReference("Student");
        adminRef = FirebaseDatabase.getInstance().getReference("ADMIN");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Initialize RecyclerView adapter and set it to the RecyclerView
        commentAdapter = new CommentAdapter(new ArrayList<>()); // Initialize with an empty list
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsRecyclerView.setAdapter(commentAdapter);

        // Retrieve announcement details from the Intent extras
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        String time = intent.getStringExtra("time");
        String date = intent.getStringExtra("date");
        String fullName = intent.getStringExtra("name");
        String imageUrl = intent.getStringExtra("imageUrl");
        String announcementId = intent.getStringExtra("announcementId");
        commentsRef = FirebaseDatabase.getInstance().getReference("comments").child(announcementId);
        // Display announcement details in the CommentActivity layout
        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView contentTextView = findViewById(R.id.contentTextView);
        TextView timeTextView = findViewById(R.id.timeTextView);
        TextView dateTextView = findViewById(R.id.dateTextView);
        TextView fullNameTextView = findViewById(R.id.fullNameTextView);
        ImageView imageView = findViewById(R.id.imageView);

        titleTextView.setText(title);
        contentTextView.setText(content);
        timeTextView.setText(time);
        dateTextView.setText(date);
        fullNameTextView.setText("Upload by: "+fullName);

        // Load and display the image using Picasso
        if (imageUrl != null) {
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.baseline_person_24)
                    .error(R.drawable.baseline_person_24)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.baseline_person_24);
            imageView.setVisibility(View.GONE);
        }


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePopup(imageUrl); // Pass the image URL to the method
            }
        });


        picture = findViewById(R.id.buttonImage);
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }

        });

        // Set an OnClickListener for the send button to add a new comment
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text from the EditText
                String messageText = commentEditText.getText().toString().trim();
                if (selectedImageUri != null) {
                    uploadImageToFirebaseStorage();
                } else if (fileUri != null) {
                    String fileName = getFileName(fileUri);
                    uploadFileToFirebaseStorage(fileUri, fileName);
                } else if (!messageText.isEmpty()) {
                    String commentId = commentsRef.push().getKey();
                    String userId = currentUser.getUid();
                    getUserDetailsAndSaveComment(userId, messageText, commentId, null, "", "");
                } else {
                    // Handle empty message input or no file selected
                    Toast.makeText(getApplicationContext(), "Please enter a message or select a file", Toast.LENGTH_SHORT).show();
                }
            }
        });



        // Listen for changes in the database and update the RecyclerView
        commentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Comment> comments = new ArrayList<>();
                for (DataSnapshot commentSnapshot : dataSnapshot.getChildren()) {
                    Comment comment = commentSnapshot.getValue(Comment.class);
                    comments.add(comment);
                }
                commentAdapter.setComments(comments);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database errors
            }
        });
    }

    private void changeStatusBarColor(int color) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }


    private void openImagePicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(commentStuddents.this);
        builder.setTitle("Choose from?");
        CharSequence[] options = {"Gallery", "File", "Cancel"};

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals("Gallery")) {
                    // Choose from Gallery
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
                } else if (options[which].equals("File")) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    String[] mimeTypes = {"application/pdf", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"};
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                    startActivityForResult(Intent.createChooser(intent, "Select a file"), FILE_SELECT_CODE);
            } else if (options[which].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Handle image upload
            Uri imageUri = data.getData();
            picture.setImageURI(imageUri);
            Uri compressedImageUri = compressImage(imageUri);
            selectedImageUri = compressedImageUri;
            uploadImageToFirebaseStorage();
        } else if (requestCode == FILE_SELECT_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Handle file upload
            fileUri = data.getData();

            // Get the file name from the Uri
            String fileName = getFileName(fileUri);

            // Pass the file name when calling uploadFileToFirebaseStorage
            uploadFileToFirebaseStorage(fileUri, fileName);
        }
    }

    private void uploadFileToFirebaseStorage(Uri fileUri, String fileName) {
        if (fileUri != null) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference fileRef = storageRef.child("files/" + fileName);

            ProgressDialog progressDialog = new ProgressDialog(commentStuddents.this);
            progressDialog.setMessage("Uploading file...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.setCancelable(false);
            progressDialog.show();

            UploadTask uploadTask = fileRef.putFile(fileUri);
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setProgress((int) progress);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();

                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUrl) {
                            String fileUrl = downloadUrl.toString();
                            String commentId = commentsRef.push().getKey();

                            // Pass the file name when calling getUserDetailsAndSaveComment
                            getUserDetailsAndSaveComment(currentUser.getUid(), null, commentId, null, fileName,fileUrl);


                            // Clear the selected file URI
                            commentStuddents.this.fileUri = null; // Use the class-level field here
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed to retrieve file URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Failed to upload file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    @SuppressLint("Range")
    private String getFileName(Uri fileUri) {
        String fileName = null;
        String scheme = fileUri.getScheme();
        if (scheme != null && scheme.equals("content")) {
            Cursor cursor = getContentResolver().query(fileUri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                cursor.close();
            }
        } else if (scheme != null && scheme.equals("file")) {
            fileName = new File(fileUri.getPath()).getName();
        }
        return fileName != null ? fileName : "unknown_file";
    }



// Inside uploadImageToFirebaseStorage method

    private void uploadImageToFirebaseStorage() {
        if (selectedImageUri != null) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = storageRef.child("images3/" + UUID.randomUUID().toString());

            ProgressDialog progressDialog = new ProgressDialog(commentStuddents.this);
            progressDialog.setMessage("Uploading image...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.setCancelable(false);
            progressDialog.show();

            UploadTask uploadTask = imageRef.putFile(selectedImageUri);
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setProgress((int) progress);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();

                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUrl) {
                            String imageContent = downloadUrl.toString();
                            String commentId = commentsRef.push().getKey();
                            getUserDetailsAndSaveComment(currentUser.getUid(), null, commentId, imageContent, null, null);

                            // Reset the selectedImageUri to null after successful upload
                            selectedImageUri = null;

                            // Show the image drawable again
                            picture.setImageResource(R.drawable.ic_baseline_image_24);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed to retrieve image URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private Uri compressImage(Uri imageUri) {
        try {
            // Use getImageUri function to get the Bitmap and compress it
            Uri compressedImageUri = getImageUri(imageUri);
            if (compressedImageUri != null) {
                return compressedImageUri;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Uri getImageUri(Uri imageUri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(imageUri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        inputStream.close();

        // Define the desired image size in kilobytes
        int maxSizeKB = 50; // Adjust this value as needed

        // Compress the bitmap to the desired size using the provided function
        return compressBitmap(bitmap, maxSizeKB);
    }

    private Uri compressBitmap(Bitmap bitmap, int maxSizeKB) throws IOException {
        // Compress the bitmap to the desired size
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int compressQuality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, outputStream);

        while (outputStream.toByteArray().length / 1024 > maxSizeKB && compressQuality > 10) {
            compressQuality -= 10;
            outputStream.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, outputStream);
        }

        // Save the compressed bitmap to a file and get the file URI
        File cachePath = new File(getCacheDir(), "temp_image.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream(cachePath);
        fileOutputStream.write(outputStream.toByteArray());
        fileOutputStream.flush();
        fileOutputStream.close();

        // Get the file URI from the cache path
        return Uri.fromFile(cachePath);
    }



    private void showImagePopup(String imageUrl) {
        // Create a custom dialog for displaying the image
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_image_popup);

        ImageView imageView = dialog.findViewById(R.id.imageViewPopup);
        ImageButton closeButton = dialog.findViewById(R.id.closeButton);

        // Load and display the image using Picasso
        if (imageUrl != null) {
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.baseline_person_24)
                    .error(R.drawable.baseline_person_24)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.baseline_person_24);
        }

        // Set an OnClickListener for the close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // Dismiss the dialog when the close button is clicked
            }
        });

        // Show the dialog
        dialog.show();
    }

    private void getUserDetailsAndSaveComment(String userId, String messageText, String commentId, String imageContent, String fileName, String fileUrl) {
        DatabaseReference userRef = studentsRef.child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Students student = dataSnapshot.getValue(Students.class);
                    if (student != null) {
                        // Handle the student details
                        String fullName = student.getName();
                        String imageUrl = student.getImage();
                        String profession = ""; // You can set this to an appropriate value for students
                        String email = student.getEmail();

                        // Pass the user details to the saveMessage method
                        saveMessage(fullName, messageText, imageUrl, profession, email, commentId, imageContent, fileName, fileUrl, userId);
                    }
                } else {
                    // If not found in Students, check in Admin3
                    DatabaseReference adminUserRef = adminRef.child(userId);
                    adminUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Students admin = dataSnapshot.getValue(Students.class);
                                if (admin != null) {
                                    // Handle the admin details
                                    String fullName = admin.getName();
                                    String imageUrl = admin.getImage();
                                    String profession = ""; // You can set this to an appropriate value for admins
                                    String email = admin.getEmail();

                                    // Pass the user details to the saveMessage method
                                    saveMessage(fullName, messageText, imageUrl, profession, email, commentId, imageContent, fileName, fileUrl, userId);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle errors
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }
    private void saveMessage(String fullName, String messageText, String imageUrl, String profession, String email, String commentId, String imageContent, String fileName, String fileUrl, String uid) {
        Comment comment = new Comment(fullName, messageText, imageUrl, profession, email, imageContent, fileName, fileUrl, uid);

        // Save the comment to the "comments" database reference with the generated commentId
        commentsRef.child(commentId).setValue(comment)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Comment successfully added
                        commentEditText.setText(""); // Clear the input field
                        // Display a success toast message
                        Toast.makeText(getApplicationContext(), "Message sent successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the error
                        Toast.makeText(getApplicationContext(), "Failed to add comment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }




    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), Student.class);
        startActivity(i);
        overridePendingTransition(0, 0);
        finish();
        super.onBackPressed();
    }
}