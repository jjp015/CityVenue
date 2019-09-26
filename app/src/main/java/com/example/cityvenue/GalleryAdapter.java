package com.example.cityvenue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {
    private final Context mContext;
    private final ArrayList<GalleryItem> mGalleryItemArrayList;

    GalleryAdapter(Context context, ArrayList<GalleryItem> galleryItemArrayList) {
        mContext = context;
        mGalleryItemArrayList = galleryItemArrayList;
    }

    class GalleryViewHolder extends RecyclerView.ViewHolder {
        final AppCompatImageView mImageView;

        GalleryViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.gallery_image);
        }
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.gallery_item, parent, false);
        return new GalleryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        GalleryItem currentItem = mGalleryItemArrayList.get(position);

        /* Load the venue's photo for the grid gallery */
        if(currentItem.getImageUrl() != null) {
            Picasso.with(mContext)
                    .load(currentItem.getImageUrl())
                    .fit()
                    .centerInside()
                    .into(holder.mImageView);
        }
    }

    @Override
    public int getItemCount() {
        return mGalleryItemArrayList.size();
    }
}
