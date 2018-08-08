package com.tokopedia.tkpdpdp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.app.TActivity;


/**
 * @author by errysuprayogi on 8/22/17.
 */

public class DinkFailedActivity extends TActivity {

    TextView toolbarTitle;
    Toolbar toolbar;
    TextView tvPromoProduct;
    TextView tvPromoExpired;
    View buttonTopAds;

    public static final String EXTRA_PRODUCT = "EXTRA_PRODUCT";
    public static final String EXTRA_TIME_EXP = "EXTRA_TIME_EXP";
    private static final String ads_url = "https://www.tokopedia.com/iklan?medium=android&source=dink";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dink_fail);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        tvPromoProduct = findViewById(R.id.tv_promo_product);
        tvPromoExpired = findViewById(R.id.tv_promo_expired);
        buttonTopAds = findViewById(R.id.button_use_topads);
        buttonTopAds.setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(ads_url));
            startActivity(i);
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            tvPromoProduct.setText(bundle.getString(EXTRA_PRODUCT));
            tvPromoExpired.setText(String.format(getString(R.string.dink_expired),
                    bundle.getString(EXTRA_TIME_EXP)));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.promote, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_close)
            finish();
        return super.onOptionsItemSelected(item);
    }

}
