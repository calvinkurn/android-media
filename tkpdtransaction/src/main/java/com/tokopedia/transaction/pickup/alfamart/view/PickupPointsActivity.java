package com.tokopedia.transaction.pickup.alfamart.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.transaction.R;

import java.util.HashMap;

import static com.tokopedia.transaction.pickup.alfamart.view.PickupPointContract.Constant.INTENT_DATA_PARAMS;

public class PickupPointsActivity extends BaseActivity {

    public static Intent createInstance(Activity activity, HashMap<String, String> params) {
        Intent intent = new Intent(activity, PickupPointsActivity.class);
        intent.putExtra(INTENT_DATA_PARAMS, params);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_point);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(
                    com.tokopedia.core.R.drawable.ic_webview_back_button
            );
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
