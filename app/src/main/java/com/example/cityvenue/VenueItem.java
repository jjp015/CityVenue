package com.example.cityvenue;

import java.io.Serializable;

class VenueItem implements Serializable {
    private String mVenueId;
    private String mImageUrl;
    private String mName;
    private String mLocation;
    private String mCategory;
    private Boolean mIsBookmark;

    VenueItem(String venueId, String imageUrl, String name, String location,
              String category, Boolean bookmark) {
        mVenueId = venueId;
        mImageUrl = imageUrl;
        mName = name;
        mLocation = location;
        mCategory = category;
        mIsBookmark = bookmark;
    }

    String getVenueId() { return mVenueId; }

    String getImageUrl() {
        return mImageUrl;
    }

    String getName() { return mName; }

    String getLocation() {
        return mLocation;
    }

    String getCategory() { return mCategory; }

    Boolean getBookmark() { return mIsBookmark; }

    void setBookmark(Boolean bookmark) { mIsBookmark = bookmark; }
}
