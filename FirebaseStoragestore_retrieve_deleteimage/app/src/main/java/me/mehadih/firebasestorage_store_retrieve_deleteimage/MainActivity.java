package me.mehadih.firebasestorage_store_retrieve_deleteimage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
private Button chooseButton, saveButton, displayButton;
private ImageView imageView;
private EditText imageNameeditText;
private ProgressBar progressBar;
private Uri imageUri;

private static final int IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chooseButton=(Button)findViewById(R.id.chooseImageButton);
        saveButton=(Button)findViewById(R.id.SaveImageButton);
        displayButton=(Button)findViewById(R.id.DisplayImageButton);

        imageView=(ImageView)findViewById(R.id.ImageViewId);

        imageNameeditText=(EditText)findViewById(R.id.ImagenameEditTextId);

        progressBar=(ProgressBar)findViewById(R.id.ProgressBarId);

        saveButton.setOnClickListener(this);
        displayButton.setOnClickListener(this);
        chooseButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chooseImageButton:
                openfileChooser();
                break;
            case R.id.SaveImageButton:
                break;
            case R.id.DisplayImageButton:
                break;
        }
    }

    private void openfileChooser() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            imageUri=data.getData();
            Picasso.with(this).load(imageUri).into(imageView);
        }
    }
}













