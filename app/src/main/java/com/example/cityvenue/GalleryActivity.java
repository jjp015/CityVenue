package com.example.cityvenue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
    private static final String POSITION = "position";      // Obtain position from bundle key
    private static final String BOOKMARK = "bookmark";      // Obtain bookmark from bundle key
    private static final String ID = "id";                  // Obtain venue id from bundle key
    private static final String NAME = "name";              // Obtain venue name from bundle key
    static final String EXTRA_BOOKMARK = "extra_bookmark";  // Store bookmark state to bookmark key
    static final String EXTRA_POSITION = "extra_position";  // To set bookmark state to position key
    private final String SIZE = "100x100";                  // Force width & height thumbnail size
    private AppCompatImageView bookmark;
    private AppCompatTextView loading, name;
    private RecyclerView mRecyclerView;
    private GalleryAdapter mGalleryAdapter;
    private ArrayList<GalleryItem> mGalleryList;
    private boolean isBookmark;                             // Check if the item has been bookmarked
    int GALLERY_LIMIT = 50;                                 // Max images to be loaded to grid
    String CLIENT_VERSION = "20180323";

    //ID & Secret for Foursquare API #1
    final String CLIENT_ID = "DNINJQ2XAJW2HNULYPTJNU1V1EWPJVK14QT13CWBU5PAHBER";
    final String CLIENT_SECRET = "BRRZMTL10K3UEZJVUFA2KNR4OGLLW3YKM032450QMS3JBMNY";

    //ID & Secret for Foursquare API #2
    //final String CLIENT_ID = "DSTODPGPWUBLQ2MCQXMNGDMRGF4LQW1IUCZB35J3UUYMVYIT";
    //final String CLIENT_SECRET = "GLKRQCEZLHBYXK5EQP5BN2PA5I1L5P0AIZ2VQYWQSQNUJEYI";

    //ID & Secret for Foursquare API #3
    //final String CLIENT_ID = "YO3Z404HOIUP1RMBCJCRI2UK2FGFVV1IFK5CEXPR5XXEU2TV";
    //final String CLIENT_SECRET = "G4JKLS2JUSAGPBK2XTUC3RYRHAB5NYVNJA34YNSMK0IXRR3Y";

    /* To send intent method to this activity from VenueActivity */
    public static Intent newIntent(Context packageContext, int position, String venueId,
                                   boolean isBookmark, String name) {
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

        AppCompatTextView name = findViewById(R.id.gallery_name);
        bookmark = findViewById(R.id.gallery_bookmark);
        loading = findViewById(R.id.gallery_loading_list);

        mRecyclerView = findViewById(R.id.gallery_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        mGalleryList = new ArrayList<>();



        /* Dummy testing of data and actions if API isn't fetching */
        for(int i = 0; i < 25; i++) {
            mGalleryList.add(new GalleryItem("https://previews.123rf.com/images/artshock/artshock1209/artshock120900045/15221647-imag-of-heart-in-the-blue-sky-against-a-background-of-white-clouds-.jpg"));
            mGalleryList.add(new GalleryItem("https://thumbs.dreamstime.com/z/hands-holding-blue-earth-cloud-sky-elements-imag-background-image-furnished-nasa-61052787.jpg"));
        }

        mGalleryAdapter = new GalleryAdapter(GalleryActivity.this,
                mGalleryList);
        mRecyclerView.setAdapter(mGalleryAdapter);

        if(mGalleryList.size() > 0) loading.setVisibility(View.GONE);
        else loading.setVisibility(View.VISIBLE);
        /* Dummy end */



        Intent intent = getIntent();

        // The position of the item from the VenueActivity list
        int position = intent.getIntExtra(POSITION, -1);

        // The venue id of the venue from VenueActivity list
        String venueId = intent.getStringExtra(ID);

        // The bookmark state of the venue from VenueActivity list
        isBookmark = intent.getBooleanExtra(BOOKMARK, false);

        // The name of the venue from VenueActivity list
        String venueName = intent.getStringExtra(NAME);

        /* Set the initial bookmark icon depending on the bookmark state */
        if(isBookmark) bookmark.setImageResource(R.drawable.ic_bookmark_black_48);
        else bookmark.setImageResource(R.drawable.ic_bookmark_border_black_48);

        /* Change the bookmark icon on toggle and store the data locally if bookmarked */
        bookmark.setOnClickListener(view -> {
            if(position == -1) {
                Toast.makeText(this, "Error loading from this venue",
                        Toast.LENGTH_SHORT).show();
                finish();
            }

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

        name.setText(venueName);    // Set the heading of the activity to the venue's name

        RequestQueue queue = Volley.newRequestQueue(this);

        String photoUrl = "https://api.foursquare.com/v2/venues/" +
                venueId +
                "/photos" +
                "?client_id=" + CLIENT_ID +
                "&client_secret=" + CLIENT_SECRET +
                "&v=" + CLIENT_VERSION +
                "&limit=" + GALLERY_LIMIT;

        /* Retrieving and then storing venue's photos */
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, photoUrl, null,
                response -> {
                    try {
                        JSONObject jsonObject = response.getJSONObject("response");
                        JSONArray photoRef = jsonObject.getJSONObject("photos")
                                .getJSONArray("items");

                        // Display this message if no photos exist for this venue
                        if(photoRef.length() == 0) loading.setText(R.string.empty_photos);

                        for(int i = 0; i < photoRef.length(); i++) {
                            JSONObject photo = photoRef.getJSONObject(i);
                            String prefix = photo.getString("prefix");
                            String suffix = photo.getString("suffix");
                            String imageUrl = prefix + SIZE + suffix;

                            mGalleryList.add(new GalleryItem(imageUrl));

                            mGalleryAdapter = new GalleryAdapter(GalleryActivity.this,
                                    mGalleryList);
                            mRecyclerView.setAdapter(mGalleryAdapter);
                        }
                        /* Remove load message after list is populated */
                        if(mGalleryList.size() > 0) loading.setVisibility(View.GONE);
                        else loading.setVisibility(View.VISIBLE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        queue.add(request);

        /* Refresh the request after 5 seconds due to no connection */
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
