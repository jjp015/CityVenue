package com.example.cityvenue;

import java.io.Serializable;

class VenueItem implements Serializable {
    private final String mVenueId;      // Venue Id
    private final String mImageUrl;     // Url of the venue's best photo
    private final String mName;         // Name of the venue
    private final String mLocation;     // Address of the venue
    private final String mCategory;     // Category of the venue
    private Boolean mIsBookmark;        // Bookmark state of the venue

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
