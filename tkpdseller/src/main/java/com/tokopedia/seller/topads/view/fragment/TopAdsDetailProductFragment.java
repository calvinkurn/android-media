package com.tokopedia.seller.topads.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.TopAdsManagementService;
import com.tokopedia.seller.topads.data.source.local.TopAdsCacheDataSourceImpl;
import com.tokopedia.seller.topads.data.source.local.TopAdsDbDataSourceImpl;
import com.tokopedia.seller.topads.domain.interactor.TopAdsProductAdInteractorImpl;
import com.tokopedia.seller.topads.domain.model.data.Ad;
import com.tokopedia.seller.topads.domain.model.data.ProductAd;
import com.tokopedia.seller.topads.view.activity.TopAdsDetailEditProductActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsDetailGroupActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsGroupEditPromoActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsGroupManagePromoActivity;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailProductPresenter;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailProductPresenterImpl;
import com.tokopedia.seller.topads.view.widget.TopAdsLabelView;

/**
 * Created by zulfikarrahman on 12/29/16.
 */

public class TopAdsDetailProductFragment extends TopAdsDetailFragment<TopAdsDetailProductPresenter> {

    public interface TopAdsDetailProductFragmentListener {
        void goToProductActivity(String productUrl);
    }

    private TopAdsLabelView promoGroupLabelView;

    private ProductAd productAd;
    private TopAdsDetailProductFragmentListener listener;
    private MenuItem manageGroupMenuItem;

    public static Fragment createInstance(ProductAd productAd, int adId) {
        Fragment fragment = new TopAdsDetailProductFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TopAdsExtraConstant.EXTRA_AD, productAd);
        bundle.putInt(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof TopAdsDetailProductFragmentListener) {
            listener = (TopAdsDetailProductFragmentListener) activity;
        }
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        promoGroupLabelView = (TopAdsLabelView) view.findViewById(R.id.label_view_promo_group);
        name.setTitle(getString(R.string.title_top_ads_product));
        name.setContentColorValue(ContextCompat.getColor(getActivity(), R.color.tkpd_main_green));
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNameClicked();
            }
        });
        promoGroupLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPromoGroupClicked();
            }
        });
    }

    @Override
    protected void initialPresenter() {
        super.initialPresenter();
        presenter = new TopAdsDetailProductPresenterImpl(getActivity(), this, new TopAdsProductAdInteractorImpl(new TopAdsManagementService(),
                new TopAdsDbDataSourceImpl(), new TopAdsCacheDataSourceImpl(getActivity())));
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_product_detail;
    }

    @Override
    protected void turnOnAd() {
        super.turnOnAd();
        presenter.turnOnAds(productAd.getId());
    }

    @Override
    protected void turnOffAd() {
        super.turnOffAd();
        presenter.turnOffAds(productAd.getId());
    }

    @Override
    protected void refreshAd() {
        if (productAd != null) {
            presenter.refreshAd(startDate, endDate, productAd.getId());
        } else {
            presenter.refreshAd(startDate, endDate, adId);
        }
    }

    @Override
    protected void editAd() {
        if (isHasGroupAd()) {
            Intent intent = TopAdsGroupEditPromoActivity.createIntent(getActivity(),
                    String.valueOf(productAd.getId()), TopAdsGroupEditPromoFragment.EXIST_GROUP, productAd.getGroupName(),
                    productAd.getGroupId());
            startActivityForResult(intent, REQUEST_CODE_AD_EDIT);
        } else if (productAd != null) {
            Intent intent = new Intent(getActivity(), TopAdsDetailEditProductActivity.class);
            intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, String.valueOf(productAd.getId()));
            startActivityForResult(intent, REQUEST_CODE_AD_EDIT);
        }
    }

    @Override
    protected void deleteAd() {
        super.deleteAd();
        presenter.deleteAd(productAd.getId());
    }

    @Override
    public void onAdLoaded(Ad ad) {
        super.onAdLoaded(ad);
        productAd = (ProductAd) ad;
        String groupName = productAd.getGroupName();
        if (isHasGroupAd()) {
            priceAndSchedule.setTitle(getString(R.string.topads_label_title_price_promo));
            promoGroupLabelView.setContent(groupName);
            promoGroupLabelView.setContentColorValue(ContextCompat.getColor(getActivity(), R.color.tkpd_main_green));
            hiddenItemProductHasGroup();
        } else {
            promoGroupLabelView.setContent(getString(R.string.label_top_ads_empty_group));
            promoGroupLabelView.setContentColorValue(ContextCompat.getColor(getActivity(), android.R.color.tab_indicator_text));
        }
        updateManageGroupMenu();
    }

    /**
     * hidden start, end and daily budget when product has group
     */
    private void hiddenItemProductHasGroup() {
        start.setVisibility(View.GONE);
        end.setVisibility(View.GONE);
        dailyBudget.setVisibility(View.GONE);
    }

    private boolean isHasGroupAd() {
        if (productAd == null) {
            return false;
        }
        return !TextUtils.isEmpty(productAd.getGroupName()) && productAd.getGroupId() > 0;
    }

    void onNameClicked() {
        if (listener != null) {
            listener.goToProductActivity(productAd.getProductUri());
        }
    }

    void onPromoGroupClicked() {
        if (isHasGroupAd()) {
            Intent intent = new Intent(getActivity(), TopAdsDetailGroupActivity.class);
            intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, productAd.getGroupId());
            startActivity(intent);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        manageGroupMenuItem = menu.findItem(R.id.menu_manage_group);
        updateManageGroupMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_manage_group) {
            manageGroup();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void manageGroup() {
        Intent intent = TopAdsGroupManagePromoActivity.createIntent(getActivity(), String.valueOf(productAd.getId()),
                TopAdsGroupManagePromoFragment.NEW_GROUP, productAd.getGroupName(), productAd.getGroupId());
        startActivityForResult(intent, REQUEST_CODE_AD_EDIT);
    }

    private void updateManageGroupMenu() {
        if (manageGroupMenuItem != null) {
            manageGroupMenuItem.setVisible(!isHasGroupAd());
        }
    }
}