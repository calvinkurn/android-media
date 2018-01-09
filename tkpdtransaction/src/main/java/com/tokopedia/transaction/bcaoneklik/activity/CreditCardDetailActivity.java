package com.tokopedia.transaction.bcaoneklik.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.transaction.R;

/**
 * @author Aghny A. Putra on 09/01/18
 */

public class CreditCardDetailActivity extends TActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.credit_card_detail_layout);
    }

    @Override
    protected void setupToolbar() {
        toolbar = (Toolbar) findViewById(com.tokopedia.core.R.id.app_bar);
        toolbar.setTitle(getTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (isLightToolbarThemes()) {
            setLightToolbarStyle();
        }
    }

    private void setLightToolbarStyle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setBackgroundResource(com.tokopedia.core.R.color.white);
        } else {
            toolbar.setBackgroundResource(com.tokopedia.core.R.drawable.bg_white_toolbar_drop_shadow);
        }

        if (getSupportActionBar() != null)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        toolbar.setTitleTextAppearance(this, com.tokopedia.core.R.style.WebViewToolbarText);
        toolbar.setSubtitleTextAppearance(this, com.tokopedia.core.R.style
                .WebViewToolbarSubtitleText);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
