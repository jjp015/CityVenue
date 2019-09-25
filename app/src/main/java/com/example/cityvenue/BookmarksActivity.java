package com.example.cityvenue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

public class BookmarksActivity extends AppCompatActivity {
    private static final String BOOKMARK_MAP = "bookmark_map";
    private HashMap<String, VenueItem> mBookmarkMap;
    private ArrayList<VenueItem> mBookmarkList;
    private AppCompatTextView loading;
    private AppCompatImageView bookmark;
    private RecyclerView mRecyclerView;
    private final String TAG = "BookmarksActivity";

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
        mBookmarkMap = (HashMap<String, VenueItem>) intent.getSerializableExtra(BOOKMARK_MAP);
        mBookmarkList = new ArrayList<>();
        if (mBookmarkMap != null)
            mBookmarkList.addAll(mBookmarkMap.values());

        loading = findViewById(R.id.bookmarks_loading_list);
        if(mBookmarkMap.isEmpty()) loading.setText(R.string.empty_bookmark);
        else loading.setVisibility(View.GONE);

        mRecyclerView = findViewById(R.id.bookmarks_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        BookmarksAdapter bookmarksAdapter = new BookmarksAdapter(BookmarksActivity.this,
                mBookmarkList);
        mRecyclerView.setAdapter(bookmarksAdapter);
    }
}
