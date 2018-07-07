package com.example.aleksandra.backpack.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aleksandra.backpack.R;

import java.util.List;

import static com.example.aleksandra.backpack.utils.Utils.loadImageWithGlide;
import static com.example.aleksandra.backpack.utils.Utils.loadImageWithGlideCircle;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private List<Uri> picsList;

    public GalleryAdapter(List<Uri> list) {
        this.picsList = list;
    }

    @NonNull
    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return new GalleryAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_gallery, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryAdapter.ViewHolder holder, int position) {
        loadImageWithGlide(picsList.get(position), holder.image);
    }

    @Override
    public int getItemCount() {
        return picsList != null ? picsList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;

        private ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.iw_gallery);
        }
    }
}

