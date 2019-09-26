package com.example.cityvenue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.preference.PowerPreference;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    static HashMap<String, VenueItem> mBookmarkMap; // The data of user's bookmarks
    private ArrayList<CityItem> cityList;           // The list of the 5 cities to search venues
    static final String SAVE_MAP= "save";           // Obtain data from local device key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* The data to retrieve if the user's bookmarks exist in local device */
        HashMap<String, VenueItem> checkSave = PowerPreference
                .getDefaultFile()
                .getMap(SAVE_MAP, HashMap.class, String.class, VenueItem.class);

        // If the data from local device exist, retrieve it to app
        if(checkSave != null)
            mBookmarkMap = checkSave;

        // Else create a new HashMap to be used
        else
            mBookmarkMap = new HashMap<>();

        /* The default cities to search for venues */
        cityList = new ArrayList<>();
        cityList.add(new CityItem(R.mipmap.miami, getString(R.string.miami)));
        cityList.add(new CityItem(R.mipmap.new_york, getString(R.string.new_york)));
        cityList.add(new CityItem(R.mipmap.san_diego, getString(R.string.san_diego)));
        cityList.add(new CityItem(R.mipmap.san_francisco, getString(R.string.san_francisco)));
        cityList.add(new CityItem(R.mipmap.seattle, getString(R.string.seattle)));

        /* Populate the cities to the list */
        RecyclerView recyclerView = findViewById(R.id.city_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CityAdapter cityAdapter = new CityAdapter(cityList);
        recyclerView.setAdapter(cityAdapter);

        /* User selects the city to search venues, send that city and start the next activity */
        cityAdapter.setOnItemClickListener(i -> {
            Intent intent = VenueActivity.newIntent(this,
                    cityList.get(i).getLocation());
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Create a button that will show the bookmarks on the top right corner of the screen
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = BookmarksActivity.newIntent(this, mBookmarkMap);

        // Start the activity and show the user's bookmarks if it exists
        if (id == R.id.action_bookmarks) {
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
