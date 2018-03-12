package com.tokopedia.discovery.imagesearch.domain.usecase;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.discovery.R;

/**
 * Created by sachinbansal on 1/10/18.
 */

public class ImageSearchResultActivity extends BaseActivity {

    private AuctionsAdapter mAdapter;
    private RecyclerView recyclerView;
    private TextView erroView;

    private int SLICE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search_result);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        erroView = (TextView) findViewById(R.id.tv_error);

        Toolbar toolbar = findViewById(R.id.my_toolbar);

        setSupportActionBar(toolbar);
        // Get the ActionBar here to configure the way it behaves.
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        erroView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        toolbar.setTitle("Image Search Result");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        NewImageSearchResponse imageSearchResponse = (NewImageSearchResponse) getIntent().getSerializableExtra("Response");


        if (imageSearchResponse != null) {
//            Log.d("MainActivity", imageSearchResponse.getTrace().getSearch().getId());

            if (imageSearchResponse.getAuctionsArrayList() != null && imageSearchResponse.getAuctionsArrayList().size() > 0) {
                mAdapter = new AuctionsAdapter(ImageSearchResultActivity.this, imageSearchResponse.getAuctionsArrayList());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
                recyclerView.setVisibility(View.VISIBLE);
                erroView.setVisibility(View.GONE);
            } else {
                erroView.setVisibility(View.VISIBLE);
                erroView.setText("No Results Found");
            }
        } else {
            erroView.setText("Error in searching");
            erroView.setVisibility(View.VISIBLE);
        }
    }
}