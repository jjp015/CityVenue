package com.example.cityvenue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.preference.PowerPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {
    private static final String POSITION = "position";
    private static final String BOOKMARK = "bookmark";
    private static final String ID = "id";
    private static final String NAME = "name";
    static final String EXTRA_BOOKMARK = "extra_bookmark";
    static final String EXTRA_POSITION = "extra_position";
    final String SIZE = "100x100";
    private AppCompatImageView bookmark;
    private AppCompatTextView name;
    private AppCompatTextView loading;
    private RecyclerView mRecyclerView;
    private GalleryAdapter mGalleryAdapter;
    private ArrayList<GalleryItem> mGalleryList;
//    final String CLIENT_ID = "DNINJQ2XAJW2HNULYPTJNU1V1EWPJVK14QT13CWBU5PAHBER";
//    final String CLIENT_SECRET = "BRRZMTL10K3UEZJVUFA2KNR4OGLLW3YKM032450QMS3JBMNY";
    final String CLIENT_ID = "DSTODPGPWUBLQ2MCQXMNGDMRGF4LQW1IUCZB35J3UUYMVYIT";
    final String CLIENT_SECRET = "GLKRQCEZLHBYXK5EQP5BN2PA5I1L5P0AIZ2VQYWQSQNUJEYI";
//    final String CLIENT_ID = "YO3Z404HOIUP1RMBCJCRI2UK2FGFVV1IFK5CEXPR5XXEU2TV";
//    final String CLIENT_SECRET = "G4JKLS2JUSAGPBK2XTUC3RYRHAB5NYVNJA34YNSMK0IXRR3Y";
    final String CLIENT_VERSION = "20180323";
    final int GALLERY_LIMIT = 1;
    private boolean isBookmark;
    private final String TAG = "GalleryActivity";

    public static Intent newIntent(Context packageContext, int position, String venueId, boolean isBookmark,
                                   String name) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(packageContext, GalleryActivity.class);

        bundle.putInt(POSITION, position);
        bundle.putString(ID, venueId);
        bundle.putBoolean(BOOKMARK, isBookmark);
        bundle.putString(NAME, name);

        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.enter, R.anim.stay);
        setContentView(R.layout.activity_gallery);

        bookmark = findViewById(R.id.gallery_bookmark);
        name = findViewById(R.id.gallery_name);
        loading = findViewById(R.id.gallery_loading_list);

        mRecyclerView = findViewById(R.id.gallery_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        mGalleryList = new ArrayList<>();

        //Dummy testing if API isn't fetching
        mGalleryList.add(new GalleryItem("https://previews.123rf.com/images/artshock/artshock1209/artshock120900045/15221647-imag-of-heart-in-the-blue-sky-against-a-background-of-white-clouds-.jpg"));
        mGalleryList.add(new GalleryItem("https://thumbs.dreamstime.com/z/hands-holding-blue-earth-cloud-sky-elements-imag-background-image-furnished-nasa-61052787.jpg"));

        mGalleryAdapter = new GalleryAdapter(GalleryActivity.this,
                mGalleryList);
        mRecyclerView.setAdapter(mGalleryAdapter);

        if(mGalleryList.size() > 0) loading.setVisibility(View.GONE);
        else loading.setVisibility(View.VISIBLE);
        //Dummy end



        Intent intent = getIntent();
        int position = intent.getIntExtra(POSITION, -1);
        Log.d(TAG, "Position is: " + position);
        String venueId = intent.getStringExtra(ID);
        isBookmark = intent.getBooleanExtra(BOOKMARK, false);
        String venueName = intent.getStringExtra(NAME);

        if(isBookmark) bookmark.setImageResource(R.drawable.ic_bookmark_black_48);
        else bookmark.setImageResource(R.drawable.ic_bookmark_border_black_48);

        bookmark.setOnClickListener(view -> {
            Intent data = new Intent();
            if(isBookmark) {
                isBookmark = false;
                bookmark.setImageResource(R.drawable.ic_bookmark_border_black_48);
                MainActivity.mBookmarkMap.remove(venueId);
                Toast.makeText(this, "Removing bookmark", Toast.LENGTH_SHORT).show();
            }
            else {
                isBookmark = true;
                bookmark.setImageResource(R.drawable.ic_bookmark_black_48);
                MainActivity.mBookmarkMap.put(venueId, new VenueItem(
                        VenueAdapter.mVenueItemArrayList.get(position).getVenueId(),
                        VenueAdapter.mVenueItemArrayList.get(position).getImageUrl(),
                        VenueAdapter.mVenueItemArrayList.get(position).getName(),
                        VenueAdapter.mVenueItemArrayList.get(position).getLocation(),
                        VenueAdapter.mVenueItemArrayList.get(position).getCategory(),
                        VenueAdapter.mVenueItemArrayList.get(position).getBookmark()));
                Toast.makeText(this, "Bookmarking", Toast.LENGTH_SHORT).show();
            }
            data.putExtra(EXTRA_BOOKMARK, isBookmark);
            data.putExtra(EXTRA_POSITION, position);
            setResult(RESULT_OK, data);
            PowerPreference.getDefaultFile().put(MainActivity.SAVE_MAP, MainActivity.mBookmarkMap);
        });

        name.setText(venueName);

        RequestQueue queue = Volley.newRequestQueue(this);

        String photoUrl = "https://api.foursquare.com/v2/venues/" +
                venueId +
                "/photos" +
                "?client_id=" + CLIENT_ID +
                "&client_secret=" + CLIENT_SECRET +
                "&v=" + CLIENT_VERSION +
                "&limit=" + GALLERY_LIMIT;
        Log.d(TAG, "VenueId " + venueId);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, photoUrl, null,
                response -> {
                    try {
                        JSONObject jsonObject = response.getJSONObject("response");
                        JSONArray photoRef = jsonObject.getJSONObject("photos")
                                .getJSONArray("items");

                        Log.d(TAG, "Length is " + photoRef.length());
                        if(photoRef.length() == 0) loading.setText(R.string.empty_photos);
                        for(int i = 0; i < photoRef.length(); i++) {
                            JSONObject photo = photoRef.getJSONObject(i);
                            String prefix = photo.getString("prefix");
                            String suffix = photo.getString("suffix");
                            String imageUrl = prefix + SIZE + suffix;

                            Log.d(TAG, "Adding into gallery list " + imageUrl);
                            mGalleryList.add(new GalleryItem(imageUrl));

                            mGalleryAdapter = new GalleryAdapter(GalleryActivity.this,
                                    mGalleryList);
                            mRecyclerView.setAdapter(mGalleryAdapter);

                            if(mGalleryList.size() > 0) loading.setVisibility(View.GONE);
                            else loading.setVisibility(View.VISIBLE);
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
}
