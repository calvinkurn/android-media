package com.tokopedia.topads.sdk.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.domain.model.Badge;
import com.tokopedia.topads.sdk.domain.model.CpmData;
import com.tokopedia.topads.sdk.domain.model.CpmModel;
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.presenter.BannerAdsPresenter;
import com.tokopedia.topads.sdk.utils.ImageLoader;

/**
 * Created by errysuprayogi on 12/28/17.
 */

public class TopAdsBannerView extends LinearLayout implements BannerAdsContract.View {

    private static final String TAG = TopAdsBannerView.class.getSimpleName();
    private BannerAdsPresenter presenter;
    private TopAdsListener adsListener;
    private TopAdsBannerClickListener topAdsBannerClickListener;
    private ImageLoader imageLoader;

    public TopAdsBannerView(Context context) {
        super(context);
        init();
    }

    public TopAdsBannerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TopAdsBannerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void createViewCpmShop(Context context, CpmData.Cpm cpm) {
        inflate(getContext(), R.layout.layout_ads_banner_shop, this);
        ImageView iconImg = (ImageView) findViewById(R.id.icon);
        TextView promotedTxt = (TextView) findViewById(R.id.title_promote);
        TextView nameTxt = (TextView) findViewById(R.id.shop_name);
        TextView descriptionTxt = (TextView) findViewById(R.id.description);
        LinearLayout badgeContainer = (LinearLayout) findViewById(R.id.badges_container);
        imageLoader.loadImage(cpm.getCpmImage().getFullEcs(), cpm.getCpmImage().getFullUrl(), iconImg);
        promotedTxt.setText(cpm.getPromotedText());
        nameTxt.setText(cpm.getName());
        descriptionTxt.setText(cpm.getDecription());
        badgeContainer.removeAllViews();
        for (Badge badge : cpm.getBadges()) {
            ImageView badgeImg = new ImageView(context);
            badgeImg.setLayoutParams(new LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.badge_size),
                    context.getResources().getDimensionPixelSize(R.dimen.badge_size)));
            imageLoader.loadImage(badge.getImageUrl(), badgeImg);
            badgeContainer.addView(badgeImg);
        }
    }

    private void createViewCpmDigital(Context context, final CpmData.Cpm cpm) {
        inflate(getContext(), R.layout.layout_ads_banner_digital, this);
        ImageView iconImg = (ImageView) findViewById(R.id.icon);
        TextView nameTxt = (TextView) findViewById(R.id.name);
        TextView descriptionTxt = (TextView) findViewById(R.id.description);
        imageLoader.loadImage(cpm.getCpmImage().getFullEcs(), cpm.getCpmImage().getFullUrl(), iconImg);
        nameTxt.setText(cpm.getName());
        descriptionTxt.setText(cpm.getDecription());
    }

    public void setConfig(Config config) {
        presenter.setConfig(config);
    }

    public void setAdsListener(TopAdsListener adsListener) {
        this.adsListener = adsListener;
    }

    public void setTopAdsBannerClickListener(TopAdsBannerClickListener topAdsBannerClickListener) {
        this.topAdsBannerClickListener = topAdsBannerClickListener;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void displayAds(CpmModel cpmModel) {
        if (cpmModel.getData().size() > 0) {
            final CpmData data = cpmModel.getData().get(0);
            if (data.getCpm().getCpmShop() != null) {
                createViewCpmShop(getContext(), data.getCpm());
            } else {
                createViewCpmDigital(getContext(), data.getCpm());
            }
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(topAdsBannerClickListener !=null){
                        topAdsBannerClickListener.onBannerAdsClicked(data.getApplinks());
                    }
                }
            });
        }
    }

    @Override
    public void onCanceled() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void loadTopAds() {
        presenter.loadTopAds();
    }

    @Override
    public void notifyAdsErrorLoaded(int errorCode, String message) {
        if (adsListener != null) {
            adsListener.onTopAdsFailToLoad(errorCode, message);
        }
    }

    public void init() {
        imageLoader = new ImageLoader(getContext());
        presenter = new BannerAdsPresenter(getContext());
        presenter.attachView(this);
    }
}
