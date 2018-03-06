package com.tokopedia.topads.dashboard.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.topads.R;
import com.tokopedia.seller.common.datepicker.view.constant.DatePickerConstant;
import com.tokopedia.seller.common.widget.LabelView;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.BulkAction;
import com.tokopedia.topads.dashboard.data.model.data.GroupAdBulkAction;
import com.tokopedia.topads.dashboard.data.model.data.ProductAd;
import com.tokopedia.topads.dashboard.data.model.data.ProductAdBulkAction;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.TopAdsManagementService;
import com.tokopedia.topads.dashboard.data.source.local.TopAdsCacheDataSourceImpl;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsProductAdInteractorImpl;
import com.tokopedia.topads.dashboard.view.activity.TopAdsAddCreditActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsDetailGroupActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsEditProductMainPageActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsGroupEditPromoActivity;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailProductPresenter;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailProductViewPresenterImpl;

/**
 * Created by zulfikarrahman on 12/29/16.
 */

public class TopAdsDetailProductFragment extends TopAdsDetailStatisticFragment<TopAdsDetailProductPresenter, ProductAd> {

    private boolean isEnoughDeposit;
    private boolean isDismissToTopUp;

    public interface TopAdsDetailProductFragmentListener {
        void goToProductActivity(String productUrl);
        void startShowCase();
    }

    private LabelView promoGroupLabelView;
    private LabelView priceAndSchedule;

    private TopAdsDetailProductFragmentListener listener;

    public static Fragment createInstance(ProductAd productAd, String adId, boolean isEnoughDeposit) {
        Fragment fragment = new TopAdsDetailProductFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TopAdsExtraConstant.EXTRA_AD, productAd);
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        bundle.putBoolean(TopAdsNewScheduleNewGroupFragment.EXTRA_IS_ENOUGH_DEPOSIT, isEnoughDeposit);
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

        initNameAndLabelView();
    }

    @Override
    protected void setupArguments(Bundle bundle) {
        super.setupArguments(bundle);
        isEnoughDeposit = bundle.getBoolean(TopAdsNewScheduleNewGroupFragment.EXTRA_IS_ENOUGH_DEPOSIT, false);
    }

    protected void initNameAndLabelView() {
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
        presenter = new TopAdsDetailProductViewPresenterImpl(getActivity(), this, new TopAdsProductAdInteractorImpl(
                new TopAdsManagementService(new SessionHandler(getActivity())),
                new TopAdsCacheDataSourceImpl(getActivity())));
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_product_detail;
    }

    @Override
    protected void turnOnAd() {
        super.turnOnAd();
        presenter.turnOnAds(ad.getId());
    }

    @Override
    protected void turnOffAd() {
        super.turnOffAd();
        presenter.turnOffAds(ad.getId());
    }

    @Override
    protected void refreshAd() {
        if (ad != null) {
            presenter.refreshAd(startDate, endDate, ad.getId());
        } else {
            presenter.refreshAd(startDate, endDate, adId);
        }
    }

    @Override
    protected void editAd() {
        if (isHasGroupAd()) {
            Intent intent = TopAdsGroupEditPromoActivity.createIntent(getActivity(),
                    ad.getId(), TopAdsGroupEditPromoFragment.EXIST_GROUP, ad.getGroupName(),
                    String.valueOf(ad.getGroupId()));
            startActivityForResult(intent, REQUEST_CODE_AD_EDIT);
        } else if (ad != null) {
            Intent intent = TopAdsEditProductMainPageActivity.createIntent(getActivity(), ad, ad.getId());
            startActivityForResult(intent, REQUEST_CODE_AD_EDIT);
        }
    }

    @Override
    protected void deleteAd() {
        super.deleteAd();
        presenter.deleteAd(ad.getId());
    }

    @Override
    public void onAdLoaded(ProductAd ad) {
        super.onAdLoaded(ad);
        if(!isEnoughDeposit){
            final BottomSheetView bottomSheetView = new BottomSheetView(getActivity());

            bottomSheetView.renderBottomSheet(new BottomSheetView.BottomSheetField
                    .BottomSheetFieldBuilder()
                    .setTitle(getString(R.string.promo_not_active))
                    .setBody(getString(R.string.promo_not_active_body))
                    .setCloseButton(getString(R.string.promo_not_active_add_top_ads_credit))
                    .build());

            bottomSheetView.setBtnCloseOnClick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetView.dismiss();

                    Intent intent = new Intent(getActivity(), TopAdsAddCreditActivity.class);
                    TopAdsDetailProductFragment.this.startActivity(intent);

                    isDismissToTopUp = true;
                }
            });

            bottomSheetView.setBtnOpsiOnClick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetView.dismiss();

                    isDismissToTopUp = false;
                }
            });

            bottomSheetView.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    if(isDismissToTopUp)
                        return;

                    if (listener != null) {
                        listener.startShowCase();
                    }
                }
            });

            bottomSheetView.show();

            isEnoughDeposit = true;
            return;
        }
        if (listener != null) {
            listener.startShowCase();
        }
    }

    @Override
    protected void updateMainView(ProductAd ad) {
        super.updateMainView(ad);
        String groupName = ad.getGroupName();
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

    @Override
    public void onTurnOffAdSuccess(BulkAction dataResponseActionAds) {
        fillToAdObject(dataResponseActionAds);
        super.onTurnOffAdSuccess(dataResponseActionAds);
    }

    private void fillToAdObject(BulkAction dataResponseActionAds) {
        if(dataResponseActionAds != null && dataResponseActionAds instanceof ProductAdBulkAction) {
            Integer status = Integer.valueOf(((ProductAdBulkAction) dataResponseActionAds).getAds().get(0).getStatus());

            CommonUtils.dumper("status from network -> "+status);
            if(adFromIntent != null)
                adFromIntent.setStatus(status);

            if(ad != null)
                ad.setStatus(status);
        }
    }

    @Override
    public void onTurnOnAdSuccess(BulkAction dataResponseActionAds) {
        fillToAdObject(dataResponseActionAds);
        super.onTurnOnAdSuccess(dataResponseActionAds);
    }

    private boolean isHasGroupAd() {
        if (ad == null) {
            return false;
        }
        return !TextUtils.isEmpty(ad.getGroupName()) && ad.getGroupId() > 0;
    }

    private void onNameClicked() {
        UnifyTracking.eventTopAdsProductClickDetailProductPDP();
        if (listener != null && ad != null) {
            listener.goToProductActivity(ad.getProductUri());
        }
    }

    private void onPromoGroupClicked() {
        UnifyTracking.eventTopAdsProductClickDetailGroupPDP();
        if (isHasGroupAd()) {
            Intent intent = new Intent(getActivity(), TopAdsDetailGroupActivity.class);
            intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, Long.toString(ad.getGroupId()));
            intent.putExtra(TopAdsExtraConstant.EXTRA_FORCE_REFRESH, true);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_delete) {
            showDeleteConfirmation(getString(R.string.title_delete_promo), getString(R.string.top_ads_delete_product_alert));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // for show case
    public View getStatusView(){
        return getView().findViewById(R.id.status);
    }

    @Override
    public void onDateChoosen(long sDate, long eDate, int lastSelection, int selectionType) {
        super.onDateChoosen(sDate, eDate, lastSelection, selectionType);
        trackingDateTopAds(lastSelection, selectionType);
    }

    private void trackingDateTopAds(int lastSelection, int selectionType) {
        if(selectionType == DatePickerConstant.SELECTION_TYPE_CUSTOM_DATE){
            UnifyTracking.eventTopAdsProductDetailProductPageDateCustom();
        }else if(selectionType == DatePickerConstant.SELECTION_TYPE_PERIOD_DATE) {
            switch (lastSelection){
                case 0:
                    UnifyTracking.eventTopAdsProductDetailProductPageDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_TODAY);
                    break;
                case 1:
                    UnifyTracking.eventTopAdsProductDetailProductPageDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_YESTERDAY);
                    break;
                case 2:
                    UnifyTracking.eventTopAdsProductDetailProductPageDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_LAST_7_DAY);
                    break;
                case 3:
                    UnifyTracking.eventTopAdsProductDetailProductPageDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_LAST_1_MONTH);
                    break;
                case 4:
                    UnifyTracking.eventTopAdsProductDetailProductPageDatePeriod(AppEventTracking.EventLabel.PERIOD_OPTION_THIS_MONTH);
                    break;
                default:
                    break;
            }
        }
    }
}