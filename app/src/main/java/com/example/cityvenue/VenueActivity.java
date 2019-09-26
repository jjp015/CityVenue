package com.example.cityvenue;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VenueActivity extends AppCompatActivity {
    private static final String LOCATION = "location";
    private final int REQUEST_CODE_GALLERY = 0;
    final String SIZE = "100x100";
    private AppCompatTextView loading;
    private RecyclerView mRecyclerView;
    private VenueAdapter mVenueAdapter;
    private ArrayList<VenueItem> mVenueList;
    private int position;
//    final String CLIENT_ID = "DNINJQ2XAJW2HNULYPTJNU1V1EWPJVK14QT13CWBU5PAHBER";
//    final String CLIENT_SECRET = "BRRZMTL10K3UEZJVUFA2KNR4OGLLW3YKM032450QMS3JBMNY";
    final String CLIENT_ID = "DSTODPGPWUBLQ2MCQXMNGDMRGF4LQW1IUCZB35J3UUYMVYIT";
    final String CLIENT_SECRET = "GLKRQCEZLHBYXK5EQP5BN2PA5I1L5P0AIZ2VQYWQSQNUJEYI";
//    final String CLIENT_ID = "YO3Z404HOIUP1RMBCJCRI2UK2FGFVV1IFK5CEXPR5XXEU2TV";
//    final String CLIENT_SECRET = "G4JKLS2JUSAGPBK2XTUC3RYRHAB5NYVNJA34YNSMK0IXRR3Y";
    final String CLIENT_VERSION = "20180323";
    final int LIMIT_LOCATION = 1;
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

        loading = findViewById(R.id.venue_loading_list);

        mRecyclerView = findViewById(R.id.venue_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mVenueList = new ArrayList<>();



        //Dummy testing if API isn't fetching
        mVenueList.add(new VenueItem("venueIdNumber1",
                "https://previews.123rf.com/images/artshock/artshock1209/artshock120900045/15221647-imag-of-heart-in-the-blue-sky-against-a-background-of-white-clouds-.jpg",
                "CloudsPt1",
                "First Line\nSecond Line", "Outdoors", false));
        mVenueList.add(new VenueItem("venueIdNumber2",
                "https://previews.123rf.com/images/artshock/artshock1209/artshock120900045/15221647-imag-of-heart-in-the-blue-sky-against-a-background-of-white-clouds-.jpg",
                "CloudsPt2",
                "First Line\nSecond Line", "Outdoors", false));

        for(int i = 0; i < mVenueList.size(); i++) {
            if(MainActivity.mBookmarkMap.containsKey(mVenueList.get(i).getVenueId())) {
                mVenueList.get(i).setBookmark(true);
            }
        }

        if(mVenueList.size() > 0)
            loading.setVisibility(View.GONE);
        else loading.setVisibility(View.VISIBLE);

/*        for(int i = 0; i < mVenueList.size(); i++) {
            if()
        }*/

        Log.d(TAG, "Calling the adapter");
        mVenueAdapter = new
                VenueAdapter(VenueActivity.this,
                mVenueList);
        mRecyclerView.setAdapter(mVenueAdapter);
        mVenueAdapter.setOnItemClickListener(
                i -> {
                    Log.d(TAG, "Position sending is " + i);
                    Intent intent = GalleryActivity
                            .newIntent(VenueActivity.this,
                                    i, mVenueList.get(i).getVenueId(),
                                    mVenueList.get(i).getBookmark(),
                                    mVenueList.get(i).getName());
                    startActivityForResult(intent, REQUEST_CODE_GALLERY);
                });
        // Dummy end



        Intent intent = getIntent();
        String location = intent.getStringExtra(LOCATION);

        RequestQueue queue = Volley.newRequestQueue(this);
        RequestQueue queue1 = Volley.newRequestQueue(this);
        if (location != null) {
            Log.d(TAG, location);
        }

        String searchUrl = "https://api.foursquare.com/v2/venues/search" +
                "?client_id="+CLIENT_ID +
                "&client_secret="+CLIENT_SECRET +
                "&v=" + CLIENT_VERSION +
                "&limit=" + LIMIT_LOCATION +
                "&near=" + location;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, searchUrl, null,
                response -> {
                    try {
                        JSONObject jsonObject = response.getJSONObject("response");
                        JSONArray venueRef = jsonObject.getJSONArray("venues");

                        if(venueRef.length() == 0) loading.setText(R.string.empty_venues);
                        for(int i = 0; i < venueRef.length(); i++) {
                            position = i;
                            JSONObject venue = venueRef.getJSONObject(i);
                            String name = venue.getString("name");
                            String firstAddress = venue.getJSONObject("location")
                                    .getJSONArray("formattedAddress").getString(0);
                            String secondAddress = venue.getJSONObject("location")
                                    .getJSONArray("formattedAddress").getString(1);
                            String fullAddress = firstAddress + "\n" + secondAddress;
                            String category;
                            String venueId = venue.getString("id");

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
                            JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET,
                                    photoUrl, null, response1 -> {
                                        try {
                                            JSONObject jsonObject1 = response1
                                                    .getJSONObject("response");
                                            JSONObject photoRef = jsonObject1.getJSONObject("venue")
                                                    .optJSONObject("bestPhoto");
                                            String imageUrl;

                                            if (photoRef == null) imageUrl = null;
                                            else {
                                                String prefix = photoRef.getString("prefix");
                                                String suffix = photoRef.getString("suffix");
                                                imageUrl = prefix + SIZE+ suffix;
                                            }

                                            Log.d(TAG, "Venue name is: " + name);
                                            Log.d(TAG, "VenueId is: " + venueId);
                                            Log.d(TAG, "Venue Address is: " + fullAddress);
                                            Log.d(TAG, "Category is: " + category);
                                            Log.d(TAG, "Image url inside is: " + imageUrl);

                                            mVenueList.add(new VenueItem(venueId, imageUrl, name,
                                                    fullAddress, category, false));

                                            for(int j = 0; j < mVenueList.size(); j++) {
                                                if(MainActivity.mBookmarkMap.containsKey(mVenueList.get(j).getVenueId())) {
                                                    mVenueList.get(j).setBookmark(true);
                                                }
                                            }

                                            if(mVenueList.size() > 0)
                                                loading.setVisibility(View.GONE);
                                            else loading.setVisibility(View.VISIBLE);

                                            Log.d(TAG, "Calling the adapter");
                                            mVenueAdapter = new
                                                    VenueAdapter(VenueActivity.this,
                                                    mVenueList);
                                            mRecyclerView.setAdapter(mVenueAdapter);
                                            mVenueAdapter.setOnItemClickListener(
                                                    i1 -> {
                                                        Intent intent1 = GalleryActivity
                                                                .newIntent(VenueActivity.this,
                                                                        i1,
                                                                        mVenueList.get(i1).getVenueId(),
                                                                        mVenueList.get(i1).getBookmark(),
                                                                        mVenueList.get(i1).getName());
                                                        startActivityForResult(intent1, REQUEST_CODE_GALLERY);
                                                    });

                                        } catch (JSONException e) {
                                            Log.d(TAG, "Second response catch: " + e);
                                            e.printStackTrace();
                                        }
                                    }, Throwable::printStackTrace);
                            queue1.add(request1);
                        }
                    } catch (JSONException e) {
                        Log.d(TAG, "First response catch:" + e);
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        queue.add(request);

        request.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition( R.anim.stay, R.anim.exit);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean bookmark;

        if (requestCode == REQUEST_CODE_GALLERY) {
            if (data == null) {
                Log.d(TAG, "NULL");
                return;
            }

            position = data.getIntExtra(GalleryActivity.EXTRA_POSITION, -1);
            bookmark = data.getBooleanExtra(GalleryActivity.EXTRA_BOOKMARK, false);

            Log.d(TAG, "Receiving back position " + position);

            if(position == -1) return;
            else mVenueList.get(position).setBookmark(bookmark);

            if(bookmark) mVenueList.get(position).setBookmark(true);
            else mVenueList.get(position).setBookmark(false);

            //Set adapter again to check bookmark toggle state from GalleryActivity
            mRecyclerView.setAdapter(mVenueAdapter);
        }
    }
}
