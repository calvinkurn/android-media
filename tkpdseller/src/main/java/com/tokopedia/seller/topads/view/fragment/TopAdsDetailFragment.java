package com.tokopedia.seller.topads.view.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.model.data.Ad;
import com.tokopedia.seller.topads.presenter.TopAdsDetailPresenter;
import com.tokopedia.seller.topads.view.listener.TopAdsDetailViewListener;
import com.tokopedia.seller.topads.view.widget.TopAdsLabelSwitch;
import com.tokopedia.seller.topads.view.widget.TopAdsLabelView;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class TopAdsDetailFragment<T extends TopAdsDetailPresenter> extends TopAdsDatePickerFragment<T> implements TopAdsDetailViewListener, CompoundButton.OnCheckedChangeListener {

    @BindView(R2.id.container_detail_topads)
    LinearLayout containerDetail;

    @BindView(R2.id.name)
    TopAdsLabelView name;

    @BindView(R2.id.status)
    TopAdsLabelSwitch status;

    @BindView(R2.id.max_bid)
    TopAdsLabelView maxBid;

    @BindView(R2.id.avg_cost)
    TopAdsLabelView avgCost;

    @BindView(R2.id.start)
    TopAdsLabelView start;

    @BindView(R2.id.end)
    TopAdsLabelView end;

    @BindView(R2.id.daily_budget)
    TopAdsLabelView dailyBudget;

    @BindView(R2.id.sent)
    TopAdsLabelView sent;

    @BindView(R2.id.impr)
    TopAdsLabelView impr;

    @BindView(R2.id.click)
    TopAdsLabelView click;

    @BindView(R2.id.ctr)
    TopAdsLabelView ctr;

    @BindView(R2.id.favorite)
    TopAdsLabelView favorite;

    protected Ad adFromIntent;
    protected ProgressDialog progressDialog;

    protected abstract void refreshAd();

    public TopAdsDetailFragment() {
        // Required empty public constructor
    }

    @Override
    protected void initView(View view) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void setupArguments(Bundle bundle) {
        super.setupArguments(bundle);
        adFromIntent = bundle.getParcelable(TopAdsExtraConstant.EXTRA_AD);
    }

    @Override
    protected void initialVar() {
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected void loadData() {
        progressDialog.show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(presenter.getRangeDateFormat(startDate, endDate));
        if (adFromIntent != null) {
            onAdLoaded(adFromIntent);
            adFromIntent = null;
        } else {
            refreshAd();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        if (checked) {
            turnOnAd();
        } else {
            turnOffAd();
        }
    }

    protected void turnOnAd() {
        progressDialog.show();
    }

    protected void turnOffAd() {
        progressDialog.show();
    }

    @Override
    public void onAdLoaded(Ad ad) {
        progressDialog.dismiss();
        loadAdDetail(ad);
    }

    @Override
    public void onLoadAdError() {
        progressDialog.dismiss();
    }

    @Override
    public void onTurnOnAdSuccess() {
        loadData();
        setResultAdStatusChanged();
    }

    @Override
    public void onTurnOnAdError() {
        setStatusSwitch(!status.isChecked());
        progressDialog.dismiss();
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                setStatusSwitch(true);
                turnOnAd();
            }
        }).showRetrySnackbar();
    }

    @Override
    public void onTurnOffAdSuccess() {
        loadData();
        setResultAdStatusChanged();
    }

    @Override
    public void onTurnOffAdError() {
        setStatusSwitch(!status.isChecked());
        progressDialog.dismiss();
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                setStatusSwitch(false);
                turnOffAd();
            }
        }).showRetrySnackbar();
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
        maxBid.setContent(ad.getPriceBidFmt());
        avgCost.setContent(ad.getStatTotalSpent());
        start.setContent(ad.getStartDate() + " - " + ad.getStartTime());
        end.setContent(ad.getEndDate() + " - " + ad.getEndTime());
        dailyBudget.setContent(ad.getPriceDailyFmt());
        sent.setContent(ad.getPriceDailySpentFmt());
        impr.setContent(ad.getStatTotalImpression());
        click.setContent(ad.getStatTotalClick());
        ctr.setContent(ad.getStatTotalCtr());
        favorite.setContent(ad.getStatTotalConversion());
    }

    protected void setStatusSwitch(boolean checked) {
        status.setListenerValue(null);
        status.setChecked(checked);
        status.setListenerValue(this);
    }

    private void setResultAdStatusChanged() {
        Intent intent = new Intent();
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_STATUS_CHANGED, true);
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.promo_topads_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_date) {
            openDatePicker();
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