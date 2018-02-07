package com.tokopedia.topads.sdk.domain.interactor;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.UseCase;
import com.tokopedia.topads.sdk.data.datasource.CloudTopAdsDataSource;
import com.tokopedia.topads.sdk.data.datasource.TopAdsDataSource;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.MerlinRecomendation;
import com.tokopedia.topads.sdk.listener.PreferedCategoryListener;
import com.tokopedia.topads.sdk.view.AdsView;

/**
 * @author by errysuprayogi on 10/25/17.
 */

public class MerlinRecomendationUseCase extends UseCase<TopAdsParams, AdsView> {

    private static String TAG = MerlinRecomendationUseCase.class.getSimpleName();
    private TopAdsDataSource dataSource;
    private AsyncTask<TopAdsParams, Void, MerlinRecomendation> task;
    private final PreferedCategoryListener listener;
    private boolean execute = false;

    public MerlinRecomendationUseCase(Context context, PreferedCategoryListener listener) {
        this.listener = listener;
        this.dataSource = new CloudTopAdsDataSource(context);
    }

    @Override
    public void setConfig(Config config) {
        dataSource.setConfig(config);
    }

    @Override
    public void execute(TopAdsParams requestParams, final AdsView view) {
        if (execute) {
            Log.d(TAG, "executor already executed cancel execution");
            return;
        }
        execute = true;
        task = new AsyncTask<TopAdsParams, Void, MerlinRecomendation>() {
            @Override
            protected MerlinRecomendation doInBackground(TopAdsParams... params) {
                String query = params[0].getParam().get(TopAdsParams.KEY_QUERY);
                return dataSource.getMerlinRecomendation(query);
            }

            @Override
            protected void onPostExecute(MerlinRecomendation merlinRecomendation) {
                if (merlinRecomendation != null) {
                    listener.onSuccessLoadPrefered(merlinRecomendation.getProductCategoryId()
                            .get(merlinRecomendation.getProductCategoryId().size() - 1).getId());
                } else {
                    listener.onErrorLoadPrefed();
                    view.notifyAdsErrorLoaded(Config.ERROR_CODE_INVALID_RESPONSE,
                            "Merlin Error occured");
                }
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, requestParams);
        } else {
            task.execute(requestParams);
        }
    }

    @Override
    public void unsubscribe() {
        if (task != null) {
            task.cancel(true);
        }
    }
}
