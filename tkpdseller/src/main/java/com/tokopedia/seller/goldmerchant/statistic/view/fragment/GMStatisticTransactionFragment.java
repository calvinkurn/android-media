package com.tokopedia.seller.goldmerchant.statistic.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.api.GMStatisticTransactionApi;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetTransactionGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.table.GetTransactionTable;
import com.tokopedia.seller.goldmerchant.statistic.di.component.DaggerGMTransactionComponent;
import com.tokopedia.seller.goldmerchant.statistic.utils.BaseWilliamChartConfig;
import com.tokopedia.seller.lib.williamchart.view.LineChartView;

import java.util.HashMap;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author normansyahputa on 7/6/17.
 */

public class GMStatisticTransactionFragment extends BaseDaggerFragment {
    public static final String TAG = "GMStatisticTransactionF";

    @Inject
    ImageHandler imageHandler;

    @Inject
    GMStatisticTransactionApi gmStatisticTransactionApi;

    @Inject
    SessionHandler sessionHandler;

    BaseWilliamChartConfig baseWilliamChartConfig;

    private View rootView;
    private LineChartView gmStatisticIncomeGraph;
    private LinearLayout gmStatisticGraphContainerInner;
    private HorizontalScrollView gmStatisticGraphContainer;

    public static Fragment createInstance() {
        return new GMStatisticTransactionFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_gmstat, container, false);
        initView();
        initVar();
        return rootView;
    }

    private void initVar() {

    }

    private void initView() {
        if (rootView == null)
            return;

        gmStatisticGraphContainer = (HorizontalScrollView) rootView.findViewById(R.id.gm_statistic_transaction_graph_container);
        gmStatisticGraphContainerInner = (LinearLayout) rootView.findViewById(R.id.gm_statistic_transaction_graph_container_inner);
        gmStatisticIncomeGraph = (LineChartView) rootView.findViewById(R.id.gm_statistic_transaction_income_graph);
    }

    @Override
    protected void initInjector() {
        DaggerGMTransactionComponent
                .builder()
                .appComponent(getComponent(AppComponent.class))
                .build().inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        gmStatisticTransactionApi.getTransactionGraph(sessionHandler.getShopID(), new HashMap<String, String>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Response<GetTransactionGraph>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(Response<GetTransactionGraph> stringResponse) {
                        Log.e(TAG, stringResponse.body().toString());
                    }
                })
        ;
        gmStatisticTransactionApi.getTransactionTable(sessionHandler.getShopID(), new HashMap<String, String>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Response<GetTransactionTable>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(Response<GetTransactionTable> stringResponse) {
                        Log.e(TAG, stringResponse.body().toString());
                    }
                })
        ;
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
