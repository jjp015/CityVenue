package com.example.cityvenue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

public class BookmarksActivity extends AppCompatActivity {
    private static final String BOOKMARK_MAP = "bookmark_map";

    public static Intent newIntent(Context packageContext, HashMap<String, VenueItem> bookmarkMap) {
        Intent intent = new Intent(packageContext, BookmarksActivity.class);
        intent.putExtra(BOOKMARK_MAP, bookmarkMap);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        Intent intent = getIntent();
        //noinspection unchecked
        HashMap<String, VenueItem> bookmarkMap =
                (HashMap<String, VenueItem>)intent.getSerializableExtra(BOOKMARK_MAP);
        ArrayList<VenueItem> bookmarkList = new ArrayList<>();
        if (bookmarkMap != null)
            bookmarkList.addAll(bookmarkMap.values());

        AppCompatTextView loading = findViewById(R.id.bookmarks_loading_list);
        if(bookmarkMap == null) {
            loading.setText(R.string.empty_bookmark);
        } else {
            if(bookmarkMap.isEmpty()) loading.setText(R.string.empty_bookmark);
            else loading.setVisibility(View.GONE);
        }

        RecyclerView recyclerView = findViewById(R.id.bookmarks_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        BookmarksAdapter bookmarksAdapter = new BookmarksAdapter(BookmarksActivity.this,
                bookmarkList);
        recyclerView.setAdapter(bookmarksAdapter);
    }
}
