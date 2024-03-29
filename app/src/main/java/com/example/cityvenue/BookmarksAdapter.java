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
    private final Context mContext;
    private final ArrayList<VenueItem> mBookmarksItemArrayList;

    BookmarksAdapter(Context context, ArrayList<VenueItem> bookmarksItemArrayList) {
        mContext = context;
        mBookmarksItemArrayList = bookmarksItemArrayList;
    }

    class BookmarksViewHolder extends RecyclerView.ViewHolder {
        final AppCompatImageView mImageView;
        final AppCompatImageView mBookmark;
        final AppCompatTextView mName;
        final AppCompatTextView mLocationView;
        final AppCompatTextView mCategory;

        BookmarksViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.venue_image);
            mName = itemView.findViewById(R.id.venue_name);
            mLocationView = itemView.findViewById(R.id.venue_location);
            mCategory = itemView.findViewById(R.id.venue_category);
            mBookmark = itemView.findViewById(R.id.venue_bookmark);

            // Remove the bookmark feature for this activity
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

        /* Load the venue's photo of the venue for the list */
        if(currentItem.getImageUrl() != null) {
            Picasso.with(mContext)
                    .load(currentItem.getImageUrl())
                    .fit()
                    .centerInside()
                    .into(holder.mImageView);
        }

        holder.mName.setText(currentItem.getName());    // Set the venue's name for list
        holder.mLocationView.setText(currentItem.getLocation()); // Set the venue's address for list
        holder.mCategory.setText(currentItem.getCategory()); // Set the venue's category for list
    }

    @Override
    public int getItemCount() {
        return mBookmarksItemArrayList.size();
    }
}
