package com.example.firebaseuploadimage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class showImageActivity extends AppCompatActivity implements imageAdapter.onItemClickListener {
    private RecyclerView recyclerView;
    private imageAdapter mImageAdapter;
    private DatabaseReference databaseReference;
    private List<Upload> uploadList;
    private FirebaseStorage mStorage;
    private ValueEventListener valueEventListener;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        recyclerView = findViewById(R.id.recycleView);
        progressBar = findViewById(R.id.progressBar);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        uploadList = new ArrayList<>();
        mImageAdapter = new imageAdapter(showImageActivity.this, uploadList);
        recyclerView.setAdapter(mImageAdapter);
        mImageAdapter.setOnItemClickListener(showImageActivity.this);
        mStorage = FirebaseStorage.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uploadList.clear();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    Upload upload = keyNode.getValue(Upload.class);
                    upload.setKey(keyNode.getKey());
                    uploadList.add(upload);
                }

                mImageAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(showImageActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "item clicked at positon " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWhatEverCLick(int position) {
        Toast.makeText(this, "menu item clicked at position "+ position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(int position) {
        Upload selectedItem = uploadList.get(position);
        final String selectedKey = selectedItem.getKey();
        StorageReference storageReference = mStorage.getReferenceFromUrl(selectedItem.getImageUri());
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference.child(selectedKey).removeValue();
                Toast.makeText(showImageActivity.this, "image deleted!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(valueEventListener);
    }
}
