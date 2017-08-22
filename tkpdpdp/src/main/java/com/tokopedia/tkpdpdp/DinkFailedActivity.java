package com.tokopedia.tkpdpdp;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.core.app.TActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author by errysuprayogi on 8/22/17.
 */

public class DinkFailedActivity extends TActivity {

    @BindView(R2.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.tv_promo_product)
    TextView tvPromoProduct;
    @BindView(R2.id.tv_promo_expired)
    TextView tvPromoExpired;

    public static final String EXTRA_PRODUCT = "EXTRA_PRODUCT";
    public static final String EXTRA_TIME_EXP = "EXTRA_TIME_EXP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dink_fail);
        ButterKnife.bind(this);
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

    @OnClick(R2.id.button_use_topads)
    public void onViewClicked() {

    }
}
