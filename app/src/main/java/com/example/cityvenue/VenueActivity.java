package com.example.cityvenue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VenueActivity extends AppCompatActivity {
    private static final String LOCATION = "location";
    private RecyclerView mRecyclerView;
    private VenueAdapter mVenueAdapter;
    private ArrayList<VenueItem> mVenueList;
    private RequestQueue queue, queue1;
    //final String CLIENT_ID = "DNINJQ2XAJW2HNULYPTJNU1V1EWPJVK14QT13CWBU5PAHBER";
    //final String CLIENT_SECRET = "BRRZMTL10K3UEZJVUFA2KNR4OGLLW3YKM032450QMS3JBMNY";
    final String CLIENT_ID = "DSTODPGPWUBLQ2MCQXMNGDMRGF4LQW1IUCZB35J3UUYMVYIT";
    final String CLIENT_SECRET = "GLKRQCEZLHBYXK5EQP5BN2PA5I1L5P0AIZ2VQYWQSQNUJEYI";
    final String SIZE = "500x500";
    final int LIMIT_LOCATION = 2;
    final int LIMIT_IMAGE = 1;
    private final String TAG = "VenueActivity";

    public static Intent newIntent(Context packageContext, String location) {
        Intent intent = new Intent(packageContext, VenueActivity.class);
        intent.putExtra(LOCATION, location);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.enter, R.anim.stay);
        setContentView(R.layout.activity_venue);

        mRecyclerView = findViewById(R.id.venue_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mVenueList = new ArrayList<>();

        Intent intent = getIntent();
        String location = intent.getStringExtra(LOCATION);

        queue = Volley.newRequestQueue(this);
        queue1 = Volley.newRequestQueue(this);
        if (location != null) {
            Log.d(TAG, location);
        }

        final String searchUrl = "https://api.foursquare.com/v2/venues/search" +
                "?client_id="+CLIENT_ID +
                "&client_secret="+CLIENT_SECRET +
                "&v=20180323" +
                "&limit=" + LIMIT_LOCATION +
                "&near=" + location;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, searchUrl, null,
                response -> {
                    try {
                        Log.d(TAG, "In first response");

                        JSONObject jsonObject = response.getJSONObject("response");

                        for(int i = 0; i < jsonObject.getJSONArray("venues").length(); i++) {
                            JSONObject venue = jsonObject.getJSONArray("venues")
                                    .getJSONObject(i);
                            String name = venue.getString("name");
                            String firstAddress = venue.getJSONObject("location")
                                    .getJSONArray("formattedAddress").getString(0);
                            String secondAddress = venue.getJSONObject("location")
                                    .getJSONArray("formattedAddress").getString(1);
                            String fullAddress = firstAddress + "\n" + secondAddress;
                            String category;
                            String venueId = venue.getString("id");
                            String venueImage = null;

                            if(venue.getJSONArray("categories")
                                    .isNull(0)) { category = "Empty Category";
                            } else {
                                category = venue.getJSONArray("categories")
                                        .getJSONObject(0).getString("name");
                            }

                            String photoUrl = "https://api.foursquare.com/v2/venues/" +
                                    venueId +
                                    "?client_id="+CLIENT_ID +
                                    "&client_secret="+CLIENT_SECRET +
                                    "&v=20180323";
                            JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET, photoUrl, null,
                                    response1 -> {
                                        try {
                                            Log.d(TAG, "In second response");

                                            JSONObject jsonObject1 = response1.getJSONObject("response");
                                            JSONObject photoRef = jsonObject1.getJSONObject("venue")
                                                    .optJSONObject("bestPhoto");
                                            String size = "300x500";
                                            String prefix = null;
                                            if (photoRef != null) {
                                                prefix = photoRef.getString("prefix");
                                            }
                                            String suffix = null;
                                            if (photoRef != null) {
                                                suffix = photoRef.getString("suffix");
                                            }
                                            Log.d(TAG, "Image url inside is: " + prefix + size + suffix);
                                        } catch (JSONException e) {
                                            Log.d(TAG, "Second response catch: " + e);
                                            e.printStackTrace();
                                        }
                                    }, Throwable::printStackTrace);

                            Log.d(TAG, "Venue name is: " + name);
                            Log.d(TAG, "VenueId is: " + venueId);
                            Log.d(TAG, "Venue Address is: " + fullAddress);
                            Log.d(TAG, "Category is: " + category);
                            Log.d(TAG, "Image Url is: " + venueImage);

                            mVenueList.add(new VenueItem(venueId, venueImage, name,
                                    fullAddress, category, false));
                            queue1.add(request1);
                        }

                        mVenueAdapter = new VenueAdapter(VenueActivity.this, mVenueList);
                        mRecyclerView.setAdapter(mVenueAdapter);

                        mVenueAdapter.setOnItemClickListener(new VenueAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int i) {

                            }

                            @Override
                            public void onBookmarkClick(int i) {

                            }
                        });

                    } catch (JSONException e) {
                        Log.d(TAG, "First response catch:" + e);
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        queue.add(request);
/*
        request.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition( R.anim.stay, R.anim.exit);
    }
}
