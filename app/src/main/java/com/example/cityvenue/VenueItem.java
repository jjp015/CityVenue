package com.example.cityvenue;

class VenueItem {
    private int mPosition;
    private String mVenueId;
    private String mImageUrl;
    private String mName;
    private String mLocation;
    private String mCategory;
    private Boolean mIsBookmark;

    VenueItem(int position, String venueId, String imageUrl, String name, String location,
              String category, Boolean bookmark) {
        mPosition = position;
        mVenueId = venueId;
        mImageUrl = imageUrl;
        mName = name;
        mLocation = location;
        mCategory = category;
        mIsBookmark = bookmark;
    }

    int getPosition() { return mPosition; }

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
