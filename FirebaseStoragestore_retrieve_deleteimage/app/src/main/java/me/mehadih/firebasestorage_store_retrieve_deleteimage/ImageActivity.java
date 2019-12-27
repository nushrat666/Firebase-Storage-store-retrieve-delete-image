package me.mehadih.firebasestorage_store_retrieve_deleteimage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ImageActivity extends AppCompatActivity {
private RecyclerView recyclerView;
private MyAdapter myAdapter;
private List<Upload> uploadList;
DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        recyclerView=findViewById(R.id.RecyclerViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

uploadList=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference("Upload");

    }
}
