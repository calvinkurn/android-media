package com.tokopedia.topads.sdk.domain.interactor;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import com.tokopedia.topads.sdk.base.UseCase;
import com.tokopedia.topads.sdk.base.adapter.Visitable;
import com.tokopedia.topads.sdk.data.datasource.CloudTopAdsDataSource;
import com.tokopedia.topads.sdk.data.datasource.TopAdsDataSource;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.TopAdsModel;
import com.tokopedia.topads.sdk.view.AdsView;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.ProductGridViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.ShopGridViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by errysuprayogi on 3/27/17.
 */

public class TopAdsUseCase extends UseCase<TopAdsParams, AdsView> {

    private TopAdsDataSource dataSource;
    private String sessionId;
    private AsyncTask<TopAdsParams, Void, TopAdsModel> task;

    public TopAdsUseCase(Context context) {
        this.dataSource = new CloudTopAdsDataSource(context);
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public void execute(TopAdsParams params, final AdsView view) {
        task = new AsyncTask<TopAdsParams, Void, TopAdsModel>() {

            @Override
            protected void onPreExecute() {
                view.initLoading();
            }

            @Override
            protected TopAdsModel doInBackground(TopAdsParams... params) {
                return dataSource.getTopAds(sessionId, params[0].getParam());
            }

            @Override
            protected void onPostExecute(TopAdsModel topAdsModel) {
                if (topAdsModel.getStatus().getErrorCode() == 0) {
                    List<Visitable> visitables = new ArrayList<>();
                    for (Data data : topAdsModel.getData()) {
                        if (data.getProduct() != null) {
                            visitables.add(convertToProductViewModel(data));
                        } else if (data.getShop() != null) {
                            visitables.add(convertToShopViewModel(data));
                        }
                    }
                    view.displayAds(visitables);
                } else {
                    view.notifyAdsErrorLoaded(topAdsModel.getStatus().getErrorCode(),
                            topAdsModel.getStatus().getMessage());
                }
                view.finishLoading();
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                view.finishLoading();
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            task.execute(params);
        }
    }

    private ShopGridViewModel convertToShopViewModel(Data data) {
        ShopGridViewModel viewModel = new ShopGridViewModel();
        viewModel.setData(data);
        return viewModel;
    }

    private ProductGridViewModel convertToProductViewModel(Data data) {
        ProductGridViewModel viewModel = new ProductGridViewModel();
        viewModel.setData(data);
        return viewModel;
    }

    @Override
    public void unsubscribe() {
        if (task != null) {
            task.cancel(true);
        }
    }
}
