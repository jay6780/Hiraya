package com.shs.app.Activity.Admin.Adminsettings;



import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shs.app.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class profile_update2 extends AppCompatActivity {
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
                        Intent intent = new Intent(getApplicationContext(), Admin.class);
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

    private ProgressDialog progressDialog;
    private EditText fullnameEditText, usernameEditText;
    private Button updateButton;
    private ImageView profileImageView;
    private static final int PICK_IMAGE_REQUEST = 1;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    TextView emailEditText;

    private EditText phoneNumberEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update2);

        // Set the title of the action bar
        getSupportActionBar().setTitle("Admin Update");
        Drawable homeButton = getResources().getDrawable(R.drawable.ic_home);
        homeButton.setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(homeButton);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        changeStatusBarColor(getResources().getColor(R.color.maroon));

        SpannableString text = new SpannableString(getSupportActionBar().getTitle());
        text.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(text);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7D0A0A")));
        // Initialize views
        fullnameEditText = findViewById(R.id.fullname);
        usernameEditText = findViewById(R.id.username);
        emailEditText = findViewById(R.id.email);
        updateButton = findViewById(R.id.btn23);
        profileImageView = findViewById(R.id.upload_img);
        phoneNumberEditText = findViewById(R.id.Phone);
        // Initialize Firebase Database and Storage references
        databaseReference = FirebaseDatabase.getInstance().getReference().child("ADMIN");
        storageReference = FirebaseStorage.getInstance().getReference().child("images");


        Intent intent = getIntent();
        if (intent != null) {
            String fullName = intent.getStringExtra("name");
            String username = intent.getStringExtra("username");
            String email = intent.getStringExtra("email");
            String image = intent.getStringExtra("image"); // Retrieve the image URL
            String phoneNumber = intent.getStringExtra("phone");

            // Set the retrieved data to the corresponding views
            fullnameEditText.setText(fullName);
            usernameEditText.setText(username);
            emailEditText.setText(email);
            phoneNumberEditText.setText(String.valueOf(phoneNumber));

            // Load the image into the ImageView using Glide with CircleTransformation
            if (image != null && !image.isEmpty()) {
                RequestOptions requestOptions = new RequestOptions().circleCrop();
                Glide.with(this)
                        .load(image)
                        .apply(requestOptions)
                        .into(profileImageView);
            } else {
                // Set a default drawable image if the user has no image
                profileImageView.setImageResource(R.drawable.ic_baseline_person_27);
            }
        }



        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start intent to select an image from storage
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the user input
                String fullname = fullnameEditText.getText().toString().trim();
                String username = usernameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String phoneNumber = phoneNumberEditText.getText().toString().trim();

                if (TextUtils.isEmpty(phoneNumber)) {
                    phoneNumberEditText.setError("Please enter your phone number");
                    return;
                } else {
                    phoneNumberEditText.setError(null); // Clear the error
                }


                // Check if all fields are filled
                if (TextUtils.isEmpty(fullname)) {
                    fullnameEditText.setError("Please enter your fullname");
                    return;
                } else {
                    fullnameEditText.setError(null); // Clear the error
                }

                if (TextUtils.isEmpty(username)) {
                    usernameEditText.setError("Please enter your username");
                    return;
                } else {
                    usernameEditText.setError(null); // Clear the error
                }




                // Update the profile data in the database
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference userRef = databaseReference.child(userId); // Use the user ID as the child key
                userRef.child("name").setValue(fullname);
                userRef.child("username").setValue(username);
                userRef.child("email").setValue(email);
                userRef.child("phone").setValue(phoneNumber);

                // Upload the image to storage
                Uri imageUri = getImageUri();
                if (imageUri != null) {
                    progressDialog = new ProgressDialog(profile_update2.this);
                    progressDialog.setMessage("Updating profile...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    StorageReference imageRef = storageReference.child(userId + "/image.jpg"); // Use the user ID as the child key and a unique image name
                    imageRef.putFile(imageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Image upload successful
                                    // Get the download URL of the uploaded image
                                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String imageUrl = uri.toString();
                                            userRef.child("image").setValue(imageUrl)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            // Profile update successful
                                                            if (progressDialog != null) {
                                                                progressDialog.dismiss();
                                                            }
                                                            Toast.makeText(profile_update2.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(profile_update2.this, Admin.class);
                                                            startActivity(intent);
                                                            overridePendingTransition(0,0);
                                                            finish();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            // Profile update failed
                                                            if (progressDialog != null) {
                                                                progressDialog.dismiss();
                                                            }
                                                            Toast.makeText(profile_update2.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Image upload failed
                                    if (progressDialog != null) {
                                        progressDialog.dismiss();
                                    }
                                    Toast.makeText(profile_update2.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    // No image selected
                    Toast.makeText(profile_update2.this, "Please select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void changeStatusBarColor(int color) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }


    // ...

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            // Load the selected image into the ImageView using Glide with CircleTransformation
            RequestOptions requestOptions = new RequestOptions().circleCrop();
            Glide.with(this)
                    .load(imageUri)
                    .apply(requestOptions)
                    .into(profileImageView);
        }
    }
    private Uri getImageUri() {
        Drawable drawable = profileImageView.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();

            // Define the desired image size in kilobytes
            int maxSizeKB = 200; // Adjust this value as needed

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
            try {
                File cachePath = new File(getCacheDir(), "temp_image.jpg");
                FileOutputStream fileOutputStream = new FileOutputStream(cachePath);
                fileOutputStream.write(outputStream.toByteArray());
                fileOutputStream.flush();
                fileOutputStream.close();

                // Get the file URI from the cache path
                return Uri.fromFile(cachePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null; // Return null if the image URI couldn't be retrieved
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(profile_update2.this, Admin.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        super.onBackPressed();
    }
}
