package com.example.cityvenue;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VenueAdapter extends RecyclerView.Adapter<VenueAdapter.VenueViewHolder> {
    private Context mContext;
    static ArrayList<VenueItem> mVenueItemArrayList;
    private OnItemClickListener mOnItemClickListener;
    private final String TAG = "VenueAdapter";

    public interface OnItemClickListener {
        void onItemClick(int i);
        void onBookmarkClick(int i);
    }

    void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    VenueAdapter(Context context, ArrayList<VenueItem> venueItemArrayList) {
        mContext = context;
        mVenueItemArrayList = venueItemArrayList;
    }

    class VenueViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView mImageView, mBookmark;
        AppCompatTextView mName, mLocationView, mCategory;

        VenueViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.venue_image);
            mName = itemView.findViewById(R.id.venue_name);
            mLocationView = itemView.findViewById(R.id.venue_location);
            mCategory = itemView.findViewById(R.id.venue_category);
            mBookmark = itemView.findViewById(R.id.venue_bookmark);

            itemView.setOnClickListener(v -> {
                if(onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemClick(position);
                    }
                }
            });

            mBookmark.setOnClickListener(view -> {
                if(onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onBookmarkClick(position);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public VenueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.venue_item, parent, false);
        return new VenueViewHolder(v, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull VenueViewHolder holder, int position) {
        VenueItem currentItem = mVenueItemArrayList.get(position);

        if(currentItem.getImageUrl() != null) {
            Picasso.with(mContext)
                    .load(currentItem.getImageUrl())
                    .fit()
                    .centerInside()
                    .into(holder.mImageView);
        }
        holder.mName.setText(currentItem.getName());
        holder.mLocationView.setText(currentItem.getLocation());
        holder.mCategory.setText(currentItem.getCategory());

        if(currentItem.getBookmark())
            holder.mBookmark.setImageResource(R.drawable.ic_bookmark_black_48);
        else
            holder.mBookmark.setImageResource(R.drawable.ic_bookmark_border_black_48);

        holder.mBookmark.setOnClickListener(view -> {
            if(currentItem.getBookmark()) {
                currentItem.setBookmark(false);
                holder.mBookmark.setImageResource(R.drawable.ic_bookmark_border_black_48);

                MainActivity.mBookmarkMap.remove(mVenueItemArrayList.get(position).getVenueId());
                Toast.makeText(mContext, "Bookmark removed", Toast.LENGTH_SHORT).show();
            }
            else {
                currentItem.setBookmark(true);
                holder.mBookmark.setImageResource(R.drawable.ic_bookmark_black_48);

                MainActivity.mBookmarkMap
                        .put(VenueAdapter.mVenueItemArrayList.get(position).getVenueId(),
                        new VenueItem(
                        VenueAdapter.mVenueItemArrayList.get(position).getVenueId(),
                        VenueAdapter.mVenueItemArrayList.get(position).getImageUrl(),
                        VenueAdapter.mVenueItemArrayList.get(position).getName(),
                        VenueAdapter.mVenueItemArrayList.get(position).getLocation(),
                        VenueAdapter.mVenueItemArrayList.get(position).getCategory(),
                        VenueAdapter.mVenueItemArrayList.get(position).getBookmark()));
                Toast.makeText(mContext, "Bookmarked", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Bookmarked size " + MainActivity.mBookmarkMap.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVenueItemArrayList.size();
    }
}
