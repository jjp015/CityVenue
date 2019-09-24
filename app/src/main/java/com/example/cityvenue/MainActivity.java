package com.example.cityvenue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<CityItem> cityList;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityList = new ArrayList<>();
        cityList.add(new CityItem(R.drawable.miami, getString(R.string.miami)));
/*        cityList.add(new CityItem(R.drawable.new_york, getString(R.string.new_york)));
        cityList.add(new CityItem(R.drawable.san_diego, getString(R.string.san_diego)));
        cityList.add(new CityItem(R.drawable.san_francisco, getString(R.string.san_francisco)));
        cityList.add(new CityItem(R.drawable.seattle, getString(R.string.seattle)));*/

        mRecyclerView = findViewById(R.id.city_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        CityAdapter cityAdapter = new CityAdapter(cityList);
        mRecyclerView.setAdapter(cityAdapter);

        cityAdapter.setOnItemClickListener(i -> {
            Intent intent = VenueActivity.newIntent(this,
                    cityList.get(i).getLocation());
            startActivity(intent);
        });
    }
}
