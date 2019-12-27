package me.mehadih.firebasestorage_store_retrieve_deleteimage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button chooseButton, saveButton, displayButton;
    private ImageView imageView;
    private EditText imageNameeditText;
    private ProgressBar progressBar;
    private Uri imageUri;

    DatabaseReference databaseReference;
    StorageReference storageReference;

    StorageTask uploadTask;

    private static final int IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference("Upload");
        storageReference = FirebaseStorage.getInstance().getReference("Upload");


        chooseButton = (Button) findViewById(R.id.chooseImageButton);
        saveButton = (Button) findViewById(R.id.SaveImageButton);
        displayButton = (Button) findViewById(R.id.DisplayImageButton);

        imageView = (ImageView) findViewById(R.id.ImageViewId);

        imageNameeditText = (EditText) findViewById(R.id.ImagenameEditTextId);

        progressBar = (ProgressBar) findViewById(R.id.ProgressBarId);

        saveButton.setOnClickListener(this);
        displayButton.setOnClickListener(this);
        chooseButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chooseImageButton:
                openfileChooser();
                break;
            case R.id.SaveImageButton:
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(getApplicationContext(), "Uploading in progress", Toast.LENGTH_LONG).show();
                } else {
                    saveData();
                }

                break;
            case R.id.DisplayImageButton:

                Intent intent = new Intent(MainActivity.this, ImageActivity.class);
                startActivity(intent);
                break;
        }
    }

    //getting the extension of the image
    public String getFileExtension(Uri imageUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }

    private void saveData() {
        final String imageName = imageNameeditText.getText().toString().trim();
        if (imageName.isEmpty()) {
            imageNameeditText.setError("Enter the image name");
            imageNameeditText.requestFocus();
            return;
        }
        StorageReference ref = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
        ref.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(), "Image is stored sucessfully", Toast.LENGTH_LONG).show();

                        Task<Uri> uriTask =taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());
                        Uri downloadUrl=uriTask.getResult();

                        Upload upload = new Upload(imageName, downloadUrl.toString());

                        String uploadId = databaseReference.push().getKey();
                        databaseReference.child(uploadId).setValue(upload);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Toast.makeText(getApplicationContext(), "Image is not stored sucessfully", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void openfileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.with(this).load(imageUri).into(imageView);
        }
    }
}













