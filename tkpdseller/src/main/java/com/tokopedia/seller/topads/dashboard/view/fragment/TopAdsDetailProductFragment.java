package com.tokopedia.seller.topads.dashboard.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.widget.LabelView;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.dashboard.view.model.Ad;
import com.tokopedia.seller.topads.dashboard.data.model.data.ProductAd;
import com.tokopedia.seller.topads.dashboard.data.source.cloud.apiservice.TopAdsManagementService;
import com.tokopedia.seller.topads.dashboard.data.source.local.TopAdsCacheDataSourceImpl;
import com.tokopedia.seller.topads.dashboard.data.source.local.TopAdsDbDataSourceImpl;
import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsProductAdInteractorImpl;
import com.tokopedia.seller.topads.dashboard.view.activity.TopAdsDetailEditProductActivity;
import com.tokopedia.seller.topads.dashboard.view.activity.TopAdsDetailGroupActivity;
import com.tokopedia.seller.topads.dashboard.view.activity.TopAdsGroupEditPromoActivity;
import com.tokopedia.seller.topads.dashboard.view.activity.TopAdsGroupManagePromoActivity;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailProductPresenter;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailProductPresenterImpl;

import org.parceler.Parcels;

/**
 * Created by zulfikarrahman on 12/29/16.
 */

public class TopAdsDetailProductFragment extends TopAdsDetailStatisticFragment<TopAdsDetailProductPresenter> {

    public static final String PRODUCT_AD_PARCELABLE = "PRODUCT_AD_PARCELABLE";

    public interface TopAdsDetailProductFragmentListener {
        void goToProductActivity(String productUrl);
        void startShowCase();
    }

    private LabelView promoGroupLabelView;

    private LabelView priceAndSchedule;

    private ProductAd productAd;
    private TopAdsDetailProductFragmentListener listener;

    public static Fragment createInstance(ProductAd productAd, String adId) {
        Fragment fragment = new TopAdsDetailProductFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TopAdsExtraConstant.EXTRA_AD, productAd);
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttachListener(Context context) {
        super.onAttachListener(context);
        if (context instanceof TopAdsDetailProductFragmentListener) {
            listener = (TopAdsDetailProductFragmentListener) context;
        }
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        promoGroupLabelView = (LabelView) view.findViewById(R.id.label_view_promo_group);
        priceAndSchedule = (LabelView) view.findViewById(R.id.title_price_and_schedule);

        name.setTitle(getString(R.string.top_ads_title_product));
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
        presenter = new TopAdsDetailProductPresenterImpl(getActivity(), this, new TopAdsProductAdInteractorImpl(
                new TopAdsManagementService(new SessionHandler(getActivity()).getAccessToken(getActivity())),
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
                    productAd.getId(), TopAdsGroupEditPromoFragment.EXIST_GROUP, productAd.getGroupName(),
                    String.valueOf(productAd.getGroupId()));
            startActivityForResult(intent, REQUEST_CODE_AD_EDIT);
        } else if (productAd != null) {
            Intent intent = new Intent(getActivity(), TopAdsDetailEditProductActivity.class);
            intent.putExtra(TopAdsExtraConstant.EXTRA_NAME, String.valueOf(productAd.getName()));
            intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, productAd.getId());
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
        productAd = (ProductAd) ad;
        super.onAdLoaded(ad);
        if (listener!= null) {
            listener.startShowCase();
        }
    }

    @Override
    protected void updateMainView(Ad ad) {
        super.updateMainView(ad);
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

    private void onNameClicked() {
        if (listener != null) {
            listener.goToProductActivity(productAd.getProductUri());
        }
    }

    private void onPromoGroupClicked() {
        if (isHasGroupAd()) {
            Intent intent = new Intent(getActivity(), TopAdsDetailGroupActivity.class);
            intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, productAd.getGroupId());
            startActivity(intent);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem manageGroupMenuItem = menu.findItem(R.id.menu_manage_group);
        if (manageGroupMenuItem != null) {
            manageGroupMenuItem.setVisible(!isHasGroupAd());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_manage_group) {
            manageGroup();
            return true;
        } else if (item.getItemId() == R.id.menu_delete) {
            showDeleteConfirmation(getString(R.string.title_delete_promo), getString(R.string.top_ads_delete_product_alert));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void manageGroup() {
        Intent intent = TopAdsGroupManagePromoActivity.createIntent(getActivity(), String.valueOf(productAd.getId()),
                TopAdsGroupManagePromoFragment.NOT_IN_GROUP, productAd.getGroupName(), String.valueOf(productAd.getGroupId()));
        startActivityForResult(intent, REQUEST_CODE_AD_EDIT);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PRODUCT_AD_PARCELABLE, Parcels.wrap(productAd));
    }

    @Override
    public void onRestoreState(Bundle savedInstanceState) {
        super.onRestoreState(savedInstanceState);
        productAd = Parcels.unwrap(savedInstanceState.getParcelable(PRODUCT_AD_PARCELABLE));
        onAdLoaded(productAd);
    }

    // for show case
    public View getStatusView(){
        return getView().findViewById(R.id.status);
    }
}