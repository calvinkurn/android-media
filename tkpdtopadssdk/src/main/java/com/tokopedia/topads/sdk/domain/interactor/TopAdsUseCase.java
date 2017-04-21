package com.tokopedia.topads.sdk.domain.interactor;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.UseCase;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.data.ModelConverter;
import com.tokopedia.topads.sdk.data.datasource.CloudTopAdsDataSource;
import com.tokopedia.topads.sdk.data.datasource.TopAdsDataSource;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.TopAdsModel;
import com.tokopedia.topads.sdk.view.AdsView;
import com.tokopedia.topads.sdk.view.DisplayMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by errysuprayogi on 3/27/17.
 */

public class TopAdsUseCase extends UseCase<TopAdsParams, AdsView> {

    private static String TAG = TopAdsUseCase.class.getSimpleName();
    private TopAdsDataSource dataSource;
    private AsyncTask<TopAdsParams, Void, TopAdsModel> task;
    private int displayMode;
    private boolean execute = false;

    public TopAdsUseCase(Context context) {
        this.dataSource = new CloudTopAdsDataSource(context);
        this.displayMode = DisplayMode.GRID;
    }

    @Override
    public void setConfig(Config config) {
        this.dataSource.setConfig(config);
    }

    public void setDisplayMode(int displayMode) {
        this.displayMode = displayMode;
    }

    public int getDisplayMode() {
        return displayMode;
    }

    @Override
    public void execute(TopAdsParams params, final AdsView view) {
        if (execute) {
            Log.d(TAG, "executor already executed cancel execution");
            return;
        }
        execute = true;
        task = new AsyncTask<TopAdsParams, Void, TopAdsModel>() {

            @Override
            protected void onPreExecute() {
                view.initLoading();
            }

            @Override
            protected TopAdsModel doInBackground(TopAdsParams... params) {
                return dataSource.getTopAds(params[0].getParam());
            }

            @Override
            protected void onPostExecute(TopAdsModel topAdsModel) {
                if (topAdsModel.getError() == null && topAdsModel.getStatus().getErrorCode() == 0) {
                    List<Item> visitables = new ArrayList<>();
                    for (Data data : topAdsModel.getData()) {
                        if (data.getProduct() != null) {
                            if (displayMode == DisplayMode.GRID) {
                                visitables.add(ModelConverter.convertToProductGridViewModel(data));
                            } else {
                                visitables.add(ModelConverter.convertToProductListViewModel(data));
                            }
                        } else if (data.getShop() != null) {
                            if (displayMode == DisplayMode.GRID) {
                                visitables.add(ModelConverter.convertToShopGridViewModel(data));
                            } else {
                                visitables.add(ModelConverter.convertToShopListViewModel(data));
                            }
                        }
                    }
                    view.displayAds(visitables);
                } else if (topAdsModel.getError() != null) {
                    view.notifyAdsErrorLoaded(topAdsModel.getError().getCode(),
                            topAdsModel.getError().getTitle());
                } else {
                    view.notifyAdsErrorLoaded(topAdsModel.getStatus().getErrorCode(),
                            topAdsModel.getStatus().getMessage());
                }
                view.finishLoading();
                execute = false;
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


    @Override
    public void unsubscribe() {
        if (task != null) {
            task.cancel(true);
        }
    }
}
