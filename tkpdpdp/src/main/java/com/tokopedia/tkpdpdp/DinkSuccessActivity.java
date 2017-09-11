package com.tokopedia.tkpdpdp;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.tokopedia.core.app.TActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author by errysuprayogi on 8/22/17.
 */

public class DinkSuccessActivity extends TActivity {


    @BindView(R2.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.tv_title)
    TextView tvTitle;

    public static final String EXTRA_PRODUCT = "EXTRA_PRODUCT";
    private static final String ads_url = "https://www.tokopedia.com/iklan?medium=android&source=dink";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dink_success);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String productTitle = bundle.getString(EXTRA_PRODUCT, "");
            String text = String.format(getString(R.string.promo_dink_success), productTitle);
            SpannableString spannableString = new SpannableString(text);
            StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
            int start = 18;
            int end = 18 + productTitle.length();
            spannableString.setSpan(boldSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvTitle.setText(spannableString);
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
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(ads_url));
        startActivity(i);
    }
}
