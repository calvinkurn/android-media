package com.tokopedia.discovery.newdiscovery.hotlist.view.customview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.design.text.CopyPromoVoucher;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistPromo;

import java.util.List;

/**
 * Created by nakama on 12/5/17.
 */

public class HotlistPromoView extends BaseCustomView {

    private TextView titlePromo;
    private CopyPromoVoucher widgetCopyVoucher;
    private TextView buttonTermCondition;
    private TextView textPeriod;
    private TextView textMinTransaction;

    public HotlistPromoView(@NonNull Context context) {
        super(context);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.customview_hotlist_promo, this);
        titlePromo = view.findViewById(R.id.title_promo);
        widgetCopyVoucher = view.findViewById(R.id.widget_copy_promo_voucher);
        buttonTermCondition = view.findViewById(R.id.button_term_and_condition);
        textPeriod = view.findViewById(R.id.text_period_date);
        textMinTransaction = view.findViewById(R.id.text_min_transaction);
    }

    private void init(AttributeSet attrs) {
        init();
    }

    public HotlistPromoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public HotlistPromoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void renderData(final HotlistPromo data) {
        titlePromo.setText(data.getTitle());
        textPeriod.setText(generatePromoPeriod(data.getPromoPeriod()));
        textMinTransaction.setText(generateMinTransaction(data.getMinimunTransaction()));
        widgetCopyVoucher.setTextVoucherCode(data.getVoucherCode());
        buttonTermCondition.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onTermConditionClickListener(data);
            }
        });
    }

    private void onTermConditionClickListener(HotlistPromo data) {
        if (getContext() != null
                && getContext().getApplicationContext() instanceof IDigitalModuleRouter
                && ((IDigitalModuleRouter) getContext().getApplicationContext()).isSupportedDelegateDeepLink(data.getApplinkTermCondition())) {
            ((IDigitalModuleRouter) getContext().getApplicationContext())
                    .actionNavigateByApplinksUrl((Activity) getContext(), data.getApplinkTermCondition(), new Bundle());
        } else {
            openWebViewURL(data.getUrlTermCondition(), getContext());
        }
    }

    private void openWebViewURL(String url, Context context) {
        if (url != "" && context != null) {
            Intent intent = new Intent(context, BannerWebView.class);
            intent.putExtra("url", url);
            context.startActivity(intent);
        }
    }

    private String generateMinTransaction(String minimunTransaction) {
        return getContext().getString(R.string.template_widget_promo_min_transaction, minimunTransaction);
    }

    private String generatePromoPeriod(String promoPeriod) {
        return getContext().getString(R.string.template_widget_promo_period, promoPeriod);
    }
}
