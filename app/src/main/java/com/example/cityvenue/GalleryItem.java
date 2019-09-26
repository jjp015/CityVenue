package com.example.cityvenue;

class GalleryItem {
    private final String mImageUrl;     // Url of the venue's photo

    GalleryItem(String imageUrl) {
        mImageUrl = imageUrl;
    }

    String getImageUrl() {
        return mImageUrl;
    }
}
