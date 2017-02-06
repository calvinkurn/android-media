package com.tokopedia.seller.topads.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.model.data.DataDeposit;
import com.tokopedia.seller.topads.model.data.Summary;
import com.tokopedia.seller.topads.presenter.TopAdsDashboardPresenter;
import com.tokopedia.seller.topads.presenter.TopAdsDatePickerPresenter;
import com.tokopedia.seller.topads.presenter.TopAdsDatePickerPresenterImpl;
import com.tokopedia.seller.topads.view.activity.TopAdsAddCreditActivity;
import com.tokopedia.seller.topads.view.listener.TopAdsDashboardFragmentListener;
import com.tokopedia.seller.topads.view.widget.TopAdsStatisticLabelView;

public abstract class TopAdsDashboardFragment<T extends TopAdsDashboardPresenter> extends TopAdsDatePickerFragment<T> implements TopAdsDashboardFragmentListener {

    private static final int REQUEST_CODE_ADD_KREDIT = TopAdsDashboardFragment.class.hashCode();

    public interface Callback {

        void onLoadDataError();

        void onLoadDataSuccess();
    }

    SwipeToRefresh swipeToRefresh;

    ImageView shopIconImageView;
    TextView shopTitleTextView;
    TextView depositDescTextView;
    ImageView addDepositButton;

    View dateRangeLayout;
    TextView rangeDateDescTextView;

    TopAdsStatisticLabelView impressionStatisticLabelView;
    TopAdsStatisticLabelView clickStatisticLabelView;
    TopAdsStatisticLabelView ctrStatisticLabelView;
    TopAdsStatisticLabelView conversionStatisticLabelView;
    TopAdsStatisticLabelView averageStatisticLabelView;
    TopAdsStatisticLabelView costStatisticLabelView;

    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected TopAdsDatePickerPresenter getDatePickerPresenter() {
        return new TopAdsDatePickerPresenterImpl(getActivity());
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        swipeToRefresh = (SwipeToRefresh) view.findViewById(R.id.swipe_refresh_layout);
        shopIconImageView = (ImageView) view.findViewById(R.id.image_view_shop_icon);
        shopTitleTextView = (TextView) view.findViewById(R.id.text_view_shop_title);
        depositDescTextView = (TextView) view.findViewById(R.id.text_view_deposit_desc);
        addDepositButton = (ImageView) view.findViewById(R.id.image_button_add_deposit);
        addDepositButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddCredit();
            }
        });
        dateRangeLayout = view.findViewById(R.id.layout_date);
        dateRangeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDateLayoutClicked();
            }
        });
        rangeDateDescTextView = (TextView) view.findViewById(R.id.text_view_range_date);
        impressionStatisticLabelView = (TopAdsStatisticLabelView) view.findViewById(R.id.statistic_label_view_impression);
        clickStatisticLabelView = (TopAdsStatisticLabelView) view.findViewById(R.id.statistic_label_view_click);
        ctrStatisticLabelView = (TopAdsStatisticLabelView) view.findViewById(R.id.statistic_label_view_ctr);
        conversionStatisticLabelView = (TopAdsStatisticLabelView) view.findViewById(R.id.statistic_label_view_conversion);
        averageStatisticLabelView = (TopAdsStatisticLabelView) view.findViewById(R.id.statistic_label_view_average);
        costStatisticLabelView = (TopAdsStatisticLabelView) view.findViewById(R.id.statistic_label_view_cost);
        depositDescTextView.setText(getString(R.string.label_top_ads_deposit_desc, getString(R.string.top_ads_statistic_info_default_value)));
        impressionStatisticLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStatisticImpressionClicked();
            }
        });
        clickStatisticLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStatisticClickClicked();
            }
        });
        ctrStatisticLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStatisticCtrClicked();
            }
        });
        conversionStatisticLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStatisticConversionClicked();
            }
        });
        averageStatisticLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStatisticAverageClicked();
            }
        });
        costStatisticLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStatisticCostClicked();
            }
        });
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        RefreshHandler refresh = new RefreshHandler(getActivity(), getView(), new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                loadData();
            }
        });
    }

    public void loadData() {
        swipeToRefresh.setRefreshing(true);
        rangeDateDescTextView.setText(datePickerPresenter.getRangeDateFormat(startDate, endDate));
        presenter.populateSummary(startDate, endDate);
        presenter.populateDeposit();
        presenter.populateShopInfo();
    }

    @Override
    public void onSummaryLoaded(@NonNull Summary summary) {
        updateSummaryLayout(summary);
        hideLoading();
        onLoadDataSuccess();
    }

    private void updateSummaryLayout(Summary summary) {
        impressionStatisticLabelView.setContent(String.valueOf(summary.getImpressionSumFmt()));
        clickStatisticLabelView.setContent(String.valueOf(summary.getClickSumFmt()));
        ctrStatisticLabelView.setContent(String.valueOf(summary.getCtrPercentageFmt()));
        conversionStatisticLabelView.setContent(String.valueOf(summary.getConversionSumFmt()));
        averageStatisticLabelView.setContent(String.valueOf(summary.getCostAvgFmt()));
        costStatisticLabelView.setContent(String.valueOf(summary.getCostSumFmt()));
    }

    @Override
    public void onLoadSummaryError(@NonNull Throwable throwable) {
        showNetworkError();
        hideLoading();
    }

    @Override
    public void onDepositTopAdsLoaded(@NonNull DataDeposit dataDeposit) {
        depositDescTextView.setText(getString(R.string.label_top_ads_deposit_desc, dataDeposit.getAmountFmt()));
        hideLoading();
        onLoadDataSuccess();
    }

    @Override
    public void onLoadDepositTopAdsError(@NonNull Throwable throwable) {
        showNetworkError();
        hideLoading();
    }

    @Override
    public void onShopDetailLoaded(@NonNull ShopModel shopModel) {
        ImageHandler.loadImageCircle2(getActivity(), shopIconImageView, shopModel.info.shopAvatar);
        shopTitleTextView.setText(shopModel.info.shopName);
        hideLoading();
        onLoadDataSuccess();
    }

    @Override
    public void onLoadShopDetailError(@NonNull Throwable throwable) {
        showNetworkError();
        hideLoading();
    }

    protected void showNetworkError() {
        if (callback != null) {
            callback.onLoadDataError();
        }
    }

    protected void onLoadDataSuccess() {
        if (callback != null) {
            callback.onLoadDataSuccess();
        }
    }

    protected void hideLoading() {
        swipeToRefresh.setRefreshing(false);
    }

    void onDateLayoutClicked() {
        openDatePicker();
    }

    void onStatisticImpressionClicked() {
        Intent intent = new Intent(getActivity(), getClassIntentStatistic());
        Bundle bundle = new Bundle();
        bundle.putInt(TopAdsExtraConstant.EXTRA_STATISTIC_POSITION_KEY, TopAdsExtraConstant.EXTRA_STATISTIC_POSITION_IMPR);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    void onStatisticClickClicked() {
        Intent intent = new Intent(getActivity(), getClassIntentStatistic());
        Bundle bundle = new Bundle();
        bundle.putInt(TopAdsExtraConstant.EXTRA_STATISTIC_POSITION_KEY, TopAdsExtraConstant.EXTRA_STATISTIC_POSITION_CLICK);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    void onStatisticCtrClicked() {
        Intent intent = new Intent(getActivity(), getClassIntentStatistic());
        Bundle bundle = new Bundle();
        bundle.putInt(TopAdsExtraConstant.EXTRA_STATISTIC_POSITION_KEY, TopAdsExtraConstant.EXTRA_STATISTIC_POSITION_CTR);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    void onStatisticConversionClicked() {
        Intent intent = new Intent(getActivity(), getClassIntentStatistic());
        Bundle bundle = new Bundle();
        bundle.putInt(TopAdsExtraConstant.EXTRA_STATISTIC_POSITION_KEY, TopAdsExtraConstant.EXTRA_STATISTIC_POSITION_CONVERTION);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    void onStatisticAverageClicked() {
        Intent intent = new Intent(getActivity(), getClassIntentStatistic());
        Bundle bundle = new Bundle();
        bundle.putInt(TopAdsExtraConstant.EXTRA_STATISTIC_POSITION_KEY, TopAdsExtraConstant.EXTRA_STATISTIC_POSITION_AVG);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    void onStatisticCostClicked() {
        Intent intent = new Intent(getActivity(), getClassIntentStatistic());
        Bundle bundle = new Bundle();
        bundle.putInt(TopAdsExtraConstant.EXTRA_STATISTIC_POSITION_KEY, TopAdsExtraConstant.EXTRA_STATISTIC_POSITION_SPENT);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    void goToAddCredit() {
        Intent intent = new Intent(getActivity(), TopAdsAddCreditActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADD_KREDIT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == getActivity().RESULT_OK && requestCode == REQUEST_CODE_ADD_KREDIT) {
            presenter.populateDeposit();
        }
    }

    protected abstract Class<?> getClassIntentStatistic();

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }
}