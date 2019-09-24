package com.example.cityvenue;

class VenueItem {
    private String mVenueId;
    private String mImageUrl;
    private String mName;
    private String mLocation;
    private String mCategory;
    private Boolean mIsBookmark;

    VenueItem(String venueId, String imageUrl, String name, String location, String category, Boolean bookmark) {
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

    void setImageUrl(String imageUrl) { mImageUrl = imageUrl; }

    void setBookmark(Boolean bookmark) { mIsBookmark = bookmark; }
}