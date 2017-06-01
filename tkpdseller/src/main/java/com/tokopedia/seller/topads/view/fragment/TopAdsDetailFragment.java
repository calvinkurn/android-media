package com.tokopedia.seller.topads.view.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import com.tokopedia.seller.lib.widget.LabelSwitch;
import com.tokopedia.seller.lib.widget.LabelView;
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsDatePickerFragment;
import com.tokopedia.seller.topads.view.listener.TopAdsDetailViewListener;
import com.tokopedia.seller.topads.view.model.Ad;
import com.tokopedia.seller.topads.view.presenter.TopAdsDatePickerPresenter;
import com.tokopedia.seller.topads.view.presenter.TopAdsDatePickerPresenterImpl;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailPresenter;

import static com.tokopedia.core.network.NetworkErrorHelper.createSnackbarWithAction;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class TopAdsDetailFragment<T extends TopAdsDetailPresenter> extends TopAdsDatePickerFragment<T> implements TopAdsDetailViewListener, CompoundButton.OnCheckedChangeListener {

    protected static final int REQUEST_CODE_AD_EDIT = 1;

    protected LabelView name;
    private LabelSwitch status;

    protected SwipeToRefresh swipeToRefresh;
    protected ProgressDialog progressDialog;
    private SnackbarRetry snackbarRetry;

    protected Ad ad;
    protected String adId;
    private boolean adStatusChanged = false;

    protected abstract void refreshAd();

    protected abstract void editAd();

    @Override
    protected TopAdsDatePickerPresenter getDatePickerPresenter() {
        return new TopAdsDatePickerPresenterImpl(getActivity());
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        name = (LabelView) view.findViewById(R.id.name);
        status = (LabelSwitch) view.findViewById(R.id.status);

        swipeToRefresh = (SwipeToRefresh) view.findViewById(R.id.swipe_refresh_layout);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
        swipeToRefresh.setProgressViewOffset(false,
                getResources().getDimensionPixelSize(R.dimen.top_ads_refresher_date_offset),
                getResources().getDimensionPixelSize(R.dimen.top_ads_refresher_date_offset_end));
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
        ad = bundle.getParcelable(TopAdsExtraConstant.EXTRA_AD);
        adId = bundle.getString(TopAdsExtraConstant.EXTRA_AD_ID);
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
            adStatusChanged = intent.getBooleanExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adStatusChanged) {
            refreshAd();
            setResultAdDetailChanged();
            adStatusChanged = false;
        }
    }

    @Override
    protected void loadData() {
        showLoading();
        if (ad != null) {
            onAdLoaded(ad);
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

    protected void showDeleteConfirmation(String title, String content) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
        alertDialog.setTitle(title);
        alertDialog.setMessage(content);
        alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAd();
            }
        });
        alertDialog.setNegativeButton(R.string.No, null);
        alertDialog.show();
    }

    @Override
    public void onAdLoaded(Ad ad) {
        this.ad = ad;
        hideLoading();
        loadAdDetail(ad);
        getActivity().invalidateOptionsMenu();
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
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onTurnOnAdSuccess() {
        ad = null;
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
        ad = null;
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
        hideLoading();
        snackbarRetry = createSnackbarWithAction(getActivity(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                deleteAd();
            }
        });
        snackbarRetry.showRetrySnackbar();
    }

    protected void loadAdDetail(Ad ad) {
        updateMainView(ad);
    }

    protected void updateMainView(Ad ad) {
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
        inflater.inflate(R.menu.menu_top_ads_detail, menu);
        menu.findItem(R.id.menu_edit).setVisible(ad != null);
        menu.findItem(R.id.menu_delete).setVisible(ad != null);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_edit) {
            editAd();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.unSubscribe();
        }
    }
}