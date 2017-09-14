package com.tokopedia.tkpdpdp.customview;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.promowidget.PromoAttributes;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

/**
 * Created by alifa on 9/13/17.
 */

public class PromoWidgetView extends BaseView<PromoAttributes, ProductDetailView> {

    private TextView promoTitle;
    private TextView promoDesc;
    private TextView promoCode;
    private TextView copyCode;
    private Context context;

    public PromoWidgetView(Context context) {
        super(context);
    }

    public PromoWidgetView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initView(Context context) {
        super.initView(context);
        this.context = context;
        promoTitle = (TextView) findViewById(R.id.promo_widget_title);
        promoDesc = (TextView) findViewById(R.id.promo_widget_desc);
        promoCode = (TextView) findViewById(R.id.promo_widget_code);
        copyCode = (TextView) findViewById(R.id.text_copy_code);

    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_promo_widget;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    @SuppressLint("DefaultLocale")
    public void renderData(@NonNull final PromoAttributes data) {
        promoTitle.setText(MethodChecker.fromHtml(data.getShortDesc()));
        promoDesc.setText(MethodChecker.fromHtml(data.getShortCond()));
        promoCode.setText(MethodChecker.fromHtml(data.getCode()));
        copyCode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(data.getCode(), data.getCode());
                clipboard.setPrimaryClip(clip);
            }
        });
        promoTitle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BannerWebView.class);
                intent.putExtra(BannerWebView.EXTRA_TITLE, context.getString(R.string.title_activity_promo));
                intent.putExtra(BannerWebView.EXTRA_URL, data.getTargetUrl());
                context.startActivity(intent);
            }
        });
        setVisibility(VISIBLE);
    }



}

