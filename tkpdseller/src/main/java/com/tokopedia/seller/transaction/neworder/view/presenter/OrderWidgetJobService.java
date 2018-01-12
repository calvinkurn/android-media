package com.tokopedia.seller.transaction.neworder.view.presenter;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;

/**
 * Created by zulfikarrahman on 1/3/18.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class OrderWidgetJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Intent intent = new Intent(getApplicationContext(), GetOrderService.class);
        intent.setAction(GetOrderService.GET_ORDER_WIDGET_ACTION);
        getApplicationContext().startService(intent);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }
}
