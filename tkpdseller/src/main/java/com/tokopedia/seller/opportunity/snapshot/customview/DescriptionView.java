package com.tokopedia.seller.opportunity.snapshot.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.customview.DescriptionTextView;
import com.tokopedia.seller.opportunity.snapshot.listener.SnapShotFragmentView;

/**
 * Created by hangnadi on 3/1/17.
 */
public class DescriptionView extends BaseView<ProductDetailData, SnapShotFragmentView> {

    DescriptionTextView tvDesc;
    ImageView ivToggle;

    private boolean isExpand = false;

    @Override
    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(getLayoutView(), this, true);

        tvDesc = (DescriptionTextView) findViewById(R.id.tv_desc);
        ivToggle = (ImageView) findViewById(R.id.iv_toggle);
    }

    public DescriptionView(Context context) {
        super(context);
    }

    public DescriptionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(SnapShotFragmentView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_description_product_snapshot;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        ivToggle.setImageResource(R.drawable.chevron_down);
        tvDesc.setMaxLines(5);
        isExpand = false;
        ivToggle.setOnClickListener(new DescriptionView.ClickToggle());
        setVisibility(GONE);
    }

    private void renderCollapse() {
        ivToggle.setImageResource(R.drawable.chevron_down);
        tvDesc.setMaxLines(5);
        tvDesc.requestFocus();
        isExpand = false;
    }

    private void renderExpand() {
        ivToggle.setImageResource(R.drawable.chevron_up);
        tvDesc.setMaxLines(Integer.MAX_VALUE);
        tvDesc.requestFocus();
        isExpand = true;
    }

    @Override
    public void renderData(@NonNull ProductDetailData data) {
        tvDesc.setText(data.getInfo().getProductDescription() == null
                || data.getInfo().getProductDescription().equals("")
                || data.getInfo().getProductDescription().equals("0")
                ? "Tidak ada deskripsi" : data.getInfo().getProductDescription());
        tvDesc.post(new Runnable() {
            @Override
            public void run() {
                int lineCnt = tvDesc.getLineCount();
                if (lineCnt > 5)
                    ivToggle.setVisibility(VISIBLE);
                else
                    ivToggle.setVisibility(INVISIBLE);
            }
        });
        setVisibility(VISIBLE);
        tvDesc.setAutoLinkMask(0);
        Linkify.addLinks(tvDesc, Linkify.WEB_URLS);
        tvDesc.setText(MethodChecker.fromHtml(tvDesc.getText().toString()));
    }

    private class ClickToggle implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (!isExpand) {
                renderExpand();
                UnifyTracking.eventPDPExpandDescription(v.getContext());
            } else {
                renderCollapse();
            }
        }
    }
}
