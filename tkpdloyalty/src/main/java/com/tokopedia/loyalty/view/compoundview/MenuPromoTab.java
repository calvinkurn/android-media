package com.tokopedia.loyalty.view.compoundview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.R2;
import com.tokopedia.loyalty.view.data.PromoMenuData;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author anggaprasetiyo on 08/01/18.
 */

public class MenuPromoTab extends BaseCustomView {

    @BindView(R2.id.iv_icon)
    ImageView ivIcon;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    private PromoMenuData promoMenuData;

    public MenuPromoTab(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public MenuPromoTab(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MenuPromoTab(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(
                R.layout.holder_menu_promo_tab, this, true
        );
        ButterKnife.bind(this);
    }

    public void renderData(PromoMenuData promoMenuData) {
        this.promoMenuData = promoMenuData;
        tvTitle.setText(promoMenuData.getTitle());
        renderNormalState();
    }

    public void renderNormalState() {
        tvTitle.setTextColor(getContext().getResources().getColor(R.color.grey_600));
        ImageHandler.loadImageAndCache(ivIcon, promoMenuData.getIconNormal());
    }

    public void renderActiveState() {
        tvTitle.setTextColor(getContext().getResources().getColor(R.color.tkpd_main_green));
        ImageHandler.loadImageAndCache(ivIcon, promoMenuData.getIconActive());
    }
}
