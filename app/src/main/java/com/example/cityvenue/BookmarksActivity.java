package com.example.cityvenue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.Serializable;
import java.util.HashMap;

public class BookmarksActivity extends AppCompatActivity {
    private static final String BOOKMARK_MAP = "bookmark_map";
    private HashMap<String, VenueItem> mBookmarkMap;
    AppCompatTextView loading;
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

        loading = findViewById(R.id.bookmarks_loading_list);

        if(mBookmarkMap.isEmpty()) loading.setText(R.string.empty_bookmark);
        else loading.setVisibility(View.GONE);

        if(mBookmarkMap != null)
            Log.d(TAG, "Size is: " + mBookmarkMap.size());
        else Log.d(TAG, "Null");
    }
}
