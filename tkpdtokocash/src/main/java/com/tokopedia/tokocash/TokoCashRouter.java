package com.tokopedia.tokocash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.core.router.digitalmodule.sellermodule.PeriodRangeModelCore;

import java.util.List;

import okhttp3.Interceptor;

/**
 * Created by nabillasabbaha on 10/5/17.
 */

public interface TokoCashRouter {

    Intent goToDatePicker(Activity activity, List<PeriodRangeModelCore> periodRangeModels, long startDate, long endDate,
                          int datePickerSelection, int datePickerType);

    String getRangeDateFormatted(Context context, long startDate, long endDate);

    WalletUserSession getTokoCashSession();

    Intent getWebviewActivityWithIntent(Context context, String url, String title);

    String getUserEmailProfil();

    Fragment getTopupTokoCashFragment();

    Interceptor getChuckInterceptor();
}
