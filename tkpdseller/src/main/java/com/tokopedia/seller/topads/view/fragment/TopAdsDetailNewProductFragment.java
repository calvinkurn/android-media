package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;
import android.content.Intent;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.di.TopAdsDetailNewProductDI;
import com.tokopedia.seller.topads.view.activity.TopAdsAddProductListActivity;
import com.tokopedia.seller.topads.view.model.TopAdsDetailProductViewModel;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailNewProductPresenter;

public class TopAdsDetailNewProductFragment extends TopAdsDetailNewFragment<TopAdsDetailNewProductPresenter> {

    public static Fragment createInstance() {
        Fragment fragment = new TopAdsDetailNewProductFragment();
        return fragment;
    }

    @Override
    protected void initialPresenter() {
        presenter = TopAdsDetailNewProductDI.createPresenter(getActivity());
        presenter.attachView(this);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_detail_new_product;
    }

    @Override
    protected void saveAd() {
        super.saveAd();
        presenter.saveAd((TopAdsDetailProductViewModel) detailAd, topAdsProductList);
    }

    @Override
    protected void setActionVar() {
        super.setActionVar();
        detailAd = new TopAdsDetailProductViewModel();
        ((TopAdsDetailProductViewModel) detailAd).setShopId(Long.parseLong(SessionHandler.getShopID(getActivity())));
        ((TopAdsDetailProductViewModel) detailAd).setType(TopAdsNetworkConstant.TYPE_PRODUCT_STAT);
    }

    @Override
    protected void addProduct() {
        Intent intent = new Intent(getActivity(), TopAdsAddProductListActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_HIDE_EXISTING_GROUP, false);
        intent.putExtra(TopAdsExtraConstant.EXTRA_HIDE_ETALASE, false);
        intent.putParcelableArrayListExtra(TopAdsExtraConstant.EXTRA_SELECTIONS, topAdsProductList);
        startActivityForResult(intent, ADD_PRODUCT_REQUEST_CODE);
    }
}