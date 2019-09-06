package com.example.firebaseuploadimage;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class imageAdapter extends RecyclerView.Adapter<imageAdapter.ImageViewHolder> {

    private Context context;
    private List<Upload>upload;

    public imageAdapter(Context context, List<Upload> upload) {
        this.context = context;
        this.upload = upload;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.imageviewlayout, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Upload uploadCurrent = upload.get(position);
        holder.textView.setText(uploadCurrent.getName());
        Picasso.with(context)
                .load(uploadCurrent.getImageUri())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return upload.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.imageTextView);
            imageView = itemView.findViewById(R.id.imageViewLayout);
        }
    }
}
