package com.tokopedia.tkpdpdp.customview;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;
import com.tokopedia.tkpdpdp.viewmodel.AffiliateInfoViewModel;

public class ButtonAffiliate extends BaseCustomView {

    private View buttonAffiliate;
    private View loadingAffiliate;
    private ProductDetailView listener;

    public ButtonAffiliate(@NonNull Context context) {
        super(context);
        init();
    }

    public ButtonAffiliate(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ButtonAffiliate(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.layout_button_affiliate_large, this);
        buttonAffiliate = view.findViewById(R.id.buttonAffiliate);
        loadingAffiliate = view.findViewById(R.id.loadingAffiliate);
    }

    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }

    public void renderView(AffiliateInfoViewModel affiliate) {
        setVisibility(VISIBLE);
        loadingAffiliate.setVisibility(affiliate != null ? GONE : VISIBLE);
        buttonAffiliate.setOnClickListener(view -> {
            if (affiliate != null) {
                listener.onByMeClicked(affiliate, false);
            }
        });
    }
}
