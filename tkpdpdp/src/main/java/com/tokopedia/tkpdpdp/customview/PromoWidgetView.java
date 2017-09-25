package com.tokopedia.tkpdpdp.customview;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.promowidget.PromoAttributes;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

/**
 * Created by alifa on 9/13/17.
 */

public class PromoWidgetView extends BaseView<PromoAttributes, ProductDetailView> {

    private TextView textPromoTitle;
    private TextView textPromoDesc;
    private TextView textPromoCode;
    private TextView btnCopyCode;
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
        textPromoTitle = (TextView) findViewById(R.id.promo_widget_title);
        textPromoDesc = (TextView) findViewById(R.id.promo_widget_desc);
        textPromoCode = (TextView) findViewById(R.id.promo_widget_code);
        btnCopyCode = (TextView) findViewById(R.id.text_copy_code);

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
        textPromoTitle.setText(MethodChecker.fromHtml(data.getShortDescHtml()));
        textPromoDesc.setText(MethodChecker.fromHtml(data.getShortCondHtml()));
        textPromoCode.setText(MethodChecker.fromHtml(data.getCodeHtml()));
        btnCopyCode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(data.getCode(), data.getCode());
                clipboard.setPrimaryClip(clip);
                listener.onPromoWidgetCopied();
            }
        });
        textPromoTitle.setOnClickListener(new OnClickListener() {
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

