package com.example.cityvenue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.BookmarksViewHolder> {
    private Context mContext;
    private ArrayList<VenueItem> mBookmarksItemArrayList;
    private final String TAG = "BookmarksAdapter";

    BookmarksAdapter(Context context, ArrayList<VenueItem> bookmarksItemArrayList) {
        mContext = context;
        mBookmarksItemArrayList = bookmarksItemArrayList;
    }

    class BookmarksViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView mImageView, mBookmark;
        AppCompatTextView mName, mLocationView, mCategory;

        BookmarksViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.venue_image);
            mName = itemView.findViewById(R.id.venue_name);
            mLocationView = itemView.findViewById(R.id.venue_location);
            mCategory = itemView.findViewById(R.id.venue_category);
            mBookmark = itemView.findViewById(R.id.venue_bookmark);

            mBookmark.setVisibility(View.GONE);
            mBookmark.setEnabled(false);
        }
    }

    @NonNull
    @Override
    public BookmarksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.venue_item, parent, false);
        return new BookmarksViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarksViewHolder holder, int position) {
        VenueItem currentItem = mBookmarksItemArrayList.get(position);

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
    }

    @Override
    public int getItemCount() {
        return mBookmarksItemArrayList.size();
    }
}
