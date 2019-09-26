package com.example.cityvenue;

class CityItem {
    private final int mImage;
    private final String mLocation;

    CityItem(int image, String location) {
        mImage = image;
        mLocation = location;
    }

    int getImage() {
        return mImage;
    }

    String getLocation() {
        return mLocation;
    }
}
