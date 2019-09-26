package com.example.cityvenue;

class CityItem {
    private final int mImage;           // The image of the default city
    private final String mLocation;     // The city and state of the default city

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
