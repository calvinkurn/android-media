package com.tokopedia.topads.sdk.presenter;

import android.content.Context;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.interactor.OpenTopAdsUseCase;
import com.tokopedia.topads.sdk.domain.interactor.TopAdsUseCase;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.view.AdsView;

/**
 * Created by errysuprayogi on 3/27/17.
 */

public class TopAdsPresenter implements AdsPresenter {

    private static final String TAG = TopAdsPresenter.class.getSimpleName();
    private AdsView adsView;
    private Context context;
    private TopAdsUseCase adsUseCase;
    private OpenTopAdsUseCase openTopAdsUseCase;
    private TopAdsParams adsParams;

    public TopAdsPresenter(Context context) {
        this.context = context;
        this.adsUseCase = new TopAdsUseCase(context);
        this.openTopAdsUseCase = new OpenTopAdsUseCase(context);
        this.adsParams = new TopAdsParams();
    }

    @Override
    public void setMaxItems(int items) {
        adsParams.getParam().put(TopAdsParams.KEY_ITEM, String.valueOf(items));
    }

    @Override
    public void setSessionId(String sessionId) {
        adsUseCase.setSessionId(sessionId);
    }

    @Override
    public void setEndpoinParam(String ep) {
        switch (ep) {
            case "0":
                adsParams.getParam().remove(TopAdsParams.KEY_EP);
                break;
            case "1":
                adsParams.getParam().put(TopAdsParams.KEY_EP, "product");
                break;
            case "2":
                adsParams.getParam().put(TopAdsParams.KEY_EP, "shop");
                break;
        }
    }

    @Override
    public void loadTopAds() {
        adsUseCase.execute(adsParams, adsView);
    }


    @Override
    public void openProductTopAds(String click_url, final Product product) {
        openTopAdsUseCase.execute(click_url, adsView);
        adsView.notifyProductClickListener(product);
    }

    @Override
    public void openShopTopAds(String click_url, final Shop shop) {
        openTopAdsUseCase.execute(click_url, adsView);
        adsView.notifyShopClickListener(shop);
    }

    @Override
    public void setParams(TopAdsParams adsParams) {
        this.adsParams = adsParams;
    }

    @Override
    public void attachView(AdsView view) {
        this.adsView = view;
    }

    @Override
    public void detachView() {
        this.adsView = null;
        adsUseCase.unsubscribe();
        openTopAdsUseCase.unsubscribe();
    }

    @Override
    public boolean isViewAttached() {
        return adsView != null;
    }

    @Override
    public void checkViewAttached() {
        if (!isViewAttached()) {
            throw new AdsViewNotAttachedException();
        }
    }

    public static class AdsViewNotAttachedException extends RuntimeException {
        public AdsViewNotAttachedException() {
            super("Please call Presenter.attachView() before " +
                    "requesting data to the presenter");
        }
    }
}
