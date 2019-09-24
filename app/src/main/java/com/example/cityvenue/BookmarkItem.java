package com.example.cityvenue;

class BookmarkItem {
    private String mVenueId;
    private String mImageUrl;
    private String mName;
    private String mLocation;
    private String mCategory;

    BookmarkItem(String imageUrl, String name, String location, String category) {
        mImageUrl = imageUrl;
        mName = name;
        mLocation = location;
        mCategory = category;
    }

    String getImageUrl() {
        return mImageUrl;
    }

    String getName() { return mName; }

    String getLocation() {
        return mLocation;
    }

    String getCategory() { return mCategory; }
}
