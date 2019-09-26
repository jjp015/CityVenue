package com.example.cityvenue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.preference.PowerPreference;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    static HashMap<String, VenueItem> mBookmarkMap;
    ArrayList<CityItem> cityList;
    RecyclerView mRecyclerView;
    static final String SAVE_MAP= "save";
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HashMap<String, VenueItem> checkSave = PowerPreference
                .getDefaultFile()
                .getMap(SAVE_MAP, HashMap.class, String.class, VenueItem.class);
        if(checkSave != null)
            mBookmarkMap = checkSave;
        else
            mBookmarkMap = new HashMap<>();

        cityList = new ArrayList<>();
        cityList.add(new CityItem(R.drawable.miami, getString(R.string.miami)));
/*        cityList.add(new CityItem(R.drawable.new_york, getString(R.string.new_york)));
        cityList.add(new CityItem(R.drawable.san_diego, getString(R.string.san_diego)));
        cityList.add(new CityItem(R.drawable.san_francisco, getString(R.string.san_francisco)));
        cityList.add(new CityItem(R.drawable.seattle, getString(R.string.seattle)));*/

        mRecyclerView = findViewById(R.id.city_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        CityAdapter cityAdapter = new CityAdapter(MainActivity.this, cityList);
        mRecyclerView.setAdapter(cityAdapter);

        cityAdapter.setOnItemClickListener(i -> {
            Intent intent = VenueActivity.newIntent(this,
                    cityList.get(i).getLocation());
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = BookmarksActivity.newIntent(this, mBookmarkMap);

        if (id == R.id.action_bookmarks) {
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
