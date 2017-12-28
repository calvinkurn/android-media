package com.tokopedia.topads.sdk.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.domain.model.CpmData;
import com.tokopedia.topads.sdk.domain.model.CpmModel;
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
    private ImageLoader imageLoader;

    private ImageView iconImg;
    private TextView sloganTxt;

    public TopAdsBannerView(Context context) {
        super(context);
        inflateView(context, null, 0);
        initPresenter();
    }

    public TopAdsBannerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflateView(context, attrs, 0);
        initPresenter();
    }

    public TopAdsBannerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateView(context, attrs, defStyleAttr);
    }

    private void inflateView(Context context, AttributeSet attrs, int defStyle) {
        imageLoader = new ImageLoader(context);
        inflate(getContext(), R.layout.layout_ads_banner_shop, this);
        iconImg = (ImageView) findViewById(R.id.icon);
        sloganTxt = (TextView) findViewById(R.id.slogan);
        initPresenter();
    }

    public void setConfig(Config config) {
        presenter.setConfig(config);
    }

    public void setAdsListener(TopAdsListener adsListener) {
        this.adsListener = adsListener;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void displayAds(CpmModel cpmModel) {
        CpmData data = cpmModel.getData().get(0);
        if(data.getCpm().getCpmShop()!=null) {
            sloganTxt.setText(data.getCpm().getCpmShop().getSlogan());
        } else {
            sloganTxt.setText(data.getCpm().getDecription());
        }
        imageLoader.loadImage(data.getCpm().getCpmImage().getFullEcs(), data.getCpm().getCpmImage().getFullUrl(), iconImg);
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
        if(adsListener!=null){
            adsListener.onTopAdsFailToLoad(errorCode, message);
        }
    }

    public void initPresenter() {
        presenter = new BannerAdsPresenter(getContext());
        presenter.attachView(this);
    }
}
