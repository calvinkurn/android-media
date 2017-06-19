package com.tokopedia.tkpdpdp;

import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.product.model.goldmerchant.VideoData;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpdpdp.customview.DescriptionTextView;
import com.tokopedia.tkpdpdp.customview.ProductVideoHorizontalScroll;

import butterknife.ButterKnife;

/**
 * @author by alifa on 5/22/17.
 */

public class DescriptionActivity extends TActivity implements View.OnClickListener {

    public static final String KEY_DESCRIPTION = "PRODUCT_DETAIL_DESCRIPTION";
    public static final String KEY_VIDEO = "PRODUCT_DETAIL_VIDEO";

    private TextView topBarTitle;
    private DescriptionTextView tvDesc;
    private ScrollView descriptionContainer;
    private ProductVideoHorizontalScroll productVideoHorizontalScroll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        initView();
        hideToolbar();
        ButterKnife.bind(this);
        showData();
        setupTopbar();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.simple_top_bar_close_button) {
            finish();
        }
    }

    private void initView() {
        topBarTitle = (TextView) findViewById(R.id.simple_top_bar_title);
        tvDesc = (DescriptionTextView) findViewById(R.id.tv_description);
        descriptionContainer = (ScrollView) findViewById(R.id.tv_desc);
        productVideoHorizontalScroll
                = (ProductVideoHorizontalScroll) findViewById(R.id.product_video_horizontal_scroll);
        ImageButton closeButton = (ImageButton) findViewById(R.id.simple_top_bar_close_button);
        closeButton.setOnClickListener(this);
    }

    private void setupTopbar() {
        topBarTitle.setText(getString(R.string.description_page_title));
    }

    public void showData() {
        String productDescription = getIntent().getStringExtra(KEY_DESCRIPTION);
        tvDesc.setText(productDescription == null
                || productDescription.equals("")
                || productDescription.equals("0")
                ? getResources().getString(R.string.no_description_pdp) : productDescription.);
        tvDesc.setAutoLinkMask(0);
        Linkify.addLinks(tvDesc, Linkify.WEB_URLS);
        descriptionContainer.setVisibility(View.VISIBLE);
        tvDesc.setText(MethodChecker.fromHtml(tvDesc.getText().toString()));
        VideoData videoData = getIntent().getParcelableExtra(KEY_VIDEO);
        if (videoData != null) {
            productVideoHorizontalScroll.setVisibility(View.VISIBLE);
            productVideoHorizontalScroll.renderData(videoData);
        } else {
            productVideoHorizontalScroll.setVisibility(View.GONE);
        }
    }


}