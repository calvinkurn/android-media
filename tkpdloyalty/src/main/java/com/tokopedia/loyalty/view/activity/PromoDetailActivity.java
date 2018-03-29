package com.tokopedia.loyalty.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.share.ShareActivity;
import com.tokopedia.loyalty.di.component.DaggerPromoDetailComponent;
import com.tokopedia.loyalty.di.component.PromoDetailComponent;
import com.tokopedia.loyalty.view.data.PromoData;
import com.tokopedia.loyalty.view.fragment.PromoDetailFragment;

/**
 * @author Aghny A. Putra on 23/03/18
 */

public class PromoDetailActivity extends BaseSimpleActivity implements HasComponent<PromoDetailComponent>,
        PromoDetailFragment.OnFragmentInteractionListener {

    private static final String EXTRA_PROMO_DATA = "promo_data";

    PromoDetailComponent component;

    public static Intent getCallingIntent(Context context, PromoData promoData) {
        Intent intent = new Intent(context, PromoDetailActivity.class);
        intent.putExtra(EXTRA_PROMO_DATA, promoData);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle("Promo Detail");
    }

    @Override
    protected Fragment getNewFragment() {
        PromoData promoData = getIntent().getParcelableExtra(EXTRA_PROMO_DATA);
        return PromoDetailFragment.newInstance(promoData);
    }

    @Override
    public PromoDetailComponent getComponent() {
        if (this.component == null) initInjector();
        return this.component;
    }

    private void initInjector() {
        this.component = DaggerPromoDetailComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .build();
    }

    @Override
    public void onSharePromo(PromoData promoData) {
        ShareData shareData = ShareData.Builder.aShareData()
                .setType(ShareData.REFERRAL_TYPE)
                .setId(promoData.getId())
                .setName(promoData.getTitle())
                .setTextContent(promoData.getTitle() + " | Tokopedia")
                .setUri(promoData.getPromoLink())
                .build();
        this.startActivity(ShareActivity.createIntent(this, shareData));
    }
}
