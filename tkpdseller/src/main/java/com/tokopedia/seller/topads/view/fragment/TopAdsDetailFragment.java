package com.tokopedia.seller.topads.view.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.data.model.data.Ad;
import com.tokopedia.seller.topads.view.presenter.TopAdsDatePickerPresenter;
import com.tokopedia.seller.topads.view.presenter.TopAdsDatePickerPresenterImpl;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailPresenter;
import com.tokopedia.seller.topads.view.listener.TopAdsDetailViewListener;
import com.tokopedia.seller.topads.view.widget.TopAdsLabelSwitch;
import com.tokopedia.seller.topads.view.widget.TopAdsLabelView;

import static com.tokopedia.core.network.NetworkErrorHelper.createSnackbarWithAction;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class TopAdsDetailFragment<T extends TopAdsDetailPresenter> extends TopAdsDatePickerFragment<T> implements TopAdsDetailViewListener, CompoundButton.OnCheckedChangeListener {

    protected static final int REQUEST_CODE_AD_EDIT = 1;

    protected TopAdsLabelView priceAndSchedule;
    protected TopAdsLabelView name;
    private TopAdsLabelSwitch status;
    private TopAdsLabelView maxBid;
    private TopAdsLabelView avgCost;
    protected TopAdsLabelView start;
    protected TopAdsLabelView end;
    protected TopAdsLabelView dailyBudget;
    private TopAdsLabelView sent;
    private TopAdsLabelView impr;
    private TopAdsLabelView click;
    private TopAdsLabelView ctr;
    protected TopAdsLabelView favorite;

    private SwipeToRefresh swipeToRefresh;
    protected ProgressDialog progressDialog;
    private SnackbarRetry snackbarRetry;

    protected Ad adFromIntent;
    protected int adId;

    protected abstract void refreshAd();

    protected abstract void editAd();

    public TopAdsDetailFragment() {
        // Required empty public constructor
    }

    @Override
    protected TopAdsDatePickerPresenter getDatePickerPresenter() {
        return new TopAdsDatePickerPresenterImpl(getActivity());
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        name = (TopAdsLabelView) view.findViewById(R.id.name);
        status = (TopAdsLabelSwitch) view.findViewById(R.id.status);
        maxBid = (TopAdsLabelView) view.findViewById(R.id.max_bid);
        avgCost = (TopAdsLabelView) view.findViewById(R.id.avg_cost);
        start = (TopAdsLabelView) view.findViewById(R.id.start);
        end = (TopAdsLabelView) view.findViewById(R.id.end);
        dailyBudget = (TopAdsLabelView) view.findViewById(R.id.daily_budget);
        sent = (TopAdsLabelView) view.findViewById(R.id.sent);
        impr = (TopAdsLabelView) view.findViewById(R.id.impr);
        click = (TopAdsLabelView) view.findViewById(R.id.click);
        ctr = (TopAdsLabelView) view.findViewById(R.id.ctr);
        favorite = (TopAdsLabelView) view.findViewById(R.id.favorite);
        swipeToRefresh = (SwipeToRefresh) view.findViewById(R.id.swipe_refresh_layout);
        priceAndSchedule = (TopAdsLabelView) view.findViewById(R.id.title_price_and_schedule);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
        snackbarRetry = createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                refreshAd();
            }
        });
        snackbarRetry.setColorActionRetry(ContextCompat.getColor(getActivity(), R.color.green_400));
    }

    @Override
    protected void setupArguments(Bundle bundle) {
        super.setupArguments(bundle);
        adFromIntent = bundle.getParcelable(TopAdsExtraConstant.EXTRA_AD);
        adId = bundle.getInt(TopAdsExtraConstant.EXTRA_AD_ID);
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        new RefreshHandler(getActivity(), getView(), new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                loadData();
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        if (checked) {
            turnOnAd();
        } else {
            turnOffAd();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // check if the request code is the same
        if (requestCode == REQUEST_CODE_AD_EDIT && intent != null) {
            boolean adStatusChanged = intent.getBooleanExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, false);
            if (adStatusChanged) {
                refreshAd();
                setResultAdDetailChanged();
            }
        }
    }

    @Override
    protected void loadData() {
        showLoading();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(datePickerPresenter.getRangeDateFormat(startDate, endDate));
        if (adFromIntent != null) {
            onAdLoaded(adFromIntent);
            adFromIntent = null;
        } else {
            refreshAd();
        }
    }

    private void showLoading() {
        if (!swipeToRefresh.isRefreshing()) {
            progressDialog.show();
        }
    }

    protected void turnOnAd() {
        showLoading();
    }

    protected void turnOffAd() {
        showLoading();
    }

    protected void deleteAd() {
        showLoading();
    }

    @Override
    public void onAdLoaded(Ad ad) {
        hideLoading();
        loadAdDetail(ad);
    }

    private void hideLoading() {
        progressDialog.dismiss();
        swipeToRefresh.setRefreshing(false);
        snackbarRetry.hideRetrySnackbar();
    }

    @Override
    public void onLoadAdError() {
        hideLoading();
        snackbarRetry = createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                refreshAd();
            }
        });
        snackbarRetry.showRetrySnackbar();
    }

    @Override
    public void onTurnOnAdSuccess() {
        loadData();
        setResultAdDetailChanged();
        snackbarRetry.hideRetrySnackbar();
    }

    @Override
    public void onTurnOnAdError() {
        setStatusSwitch(!status.isChecked());
        hideLoading();
        snackbarRetry = createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                setStatusSwitch(true);
                turnOnAd();
            }
        });
        snackbarRetry.showRetrySnackbar();
    }

    @Override
    public void onTurnOffAdSuccess() {
        loadData();
        setResultAdDetailChanged();
        snackbarRetry.hideRetrySnackbar();
    }

    @Override
    public void onTurnOffAdError() {
        setStatusSwitch(!status.isChecked());
        hideLoading();
        snackbarRetry = createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                setStatusSwitch(false);
                turnOffAd();
            }
        });
        snackbarRetry.showRetrySnackbar();
    }

    @Override
    public void onDeleteAdSuccess() {
        hideLoading();
        setResultAdDeleted();
        if (isAdded()) {
            getActivity().finish();
        }
    }

    @Override
    public void onDeleteAdError() {
        snackbarRetry = createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                deleteAd();
            }
        });
        snackbarRetry.showRetrySnackbar();
    }

    protected void loadAdDetail(Ad ad) {
        name.setContent(ad.getName());
        switch (ad.getStatus()) {
            case TopAdsConstant.STATUS_AD_ACTIVE:
            case TopAdsConstant.STATUS_AD_NOT_SENT:
                setStatusSwitch(true);
                break;
            default:
                setStatusSwitch(false);
                break;
        }
        status.setSwitchStatusText(ad.getStatusDesc());
        maxBid.setContent(getString(R.string.top_ads_bid_format_text, ad.getPriceBidFmt(), ad.getLabelPerClick()));
        avgCost.setContent(ad.getStatAvgClick());
        start.setContent(ad.getStartDate() + " - " + ad.getStartTime());
        if (TextUtils.isEmpty(ad.getEndTime())) {
            end.setContent(ad.getEndDate());
        } else {
            end.setContent(getString(R.string.top_ads_range_date_text, ad.getEndDate(), ad.getEndTime()));
        }
        if(TextUtils.isEmpty(ad.getPriceDailySpentFmt())) {
            dailyBudget.setContent(ad.getPriceDailyFmt());
        }else{
            dailyBudget.setContent(getString(R.string.topads_format_daily_budget, ad.getPriceDailySpentFmt(), ad.getPriceDailyFmt()));
        }
        sent.setContent(ad.getStatTotalSpent());
        impr.setContent(ad.getStatTotalImpression());
        click.setContent(ad.getStatTotalClick());
        ctr.setContent(ad.getStatTotalCtr());
        favorite.setContent(ad.getStatTotalConversion());
    }

    protected void setStatusSwitch(boolean checked) {
        status.setSwitchEnabled(true);
        status.setListenerValue(null);
        status.setChecked(checked);
        status.setListenerValue(this);
    }

    protected void setResultAdDetailChanged() {
        Intent intent = new Intent();
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, true);
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    private void setResultAdDeleted() {
        Intent intent = new Intent();
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_DELETED, true);
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.promo_topads_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_date) {
            openDatePicker();
            return true;
        } else if (item.getItemId() == R.id.menu_edit) {
            editAd();
            return true;
        } else if (item.getItemId() == R.id.menu_delete) {
            deleteAd();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }
}