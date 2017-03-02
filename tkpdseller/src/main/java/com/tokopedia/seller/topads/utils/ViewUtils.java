package com.tokopedia.seller.topads.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.Window;

import com.tokopedia.seller.R;
import com.tokopedia.seller.myproduct.utils.CurrencyFormatter;
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.exception.ResponseErrorException;

/**
 * @author normansyahputa on 2/16/17.
 */

public class ViewUtils {
    public static void setTranslucentStatusBar(Window window) {
        if (window == null) return;
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= Build.VERSION_CODES.LOLLIPOP) {
            setTranslucentStatusBarLollipop(window);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void setTranslucentStatusBarLollipop(Window window) {
        window.setStatusBarColor(
                window.getContext()
                        .getResources()
                        .getColor(R.color.green_600));
    }

    public static String getErrorMessage(Throwable t) {
        String errorMessage = null;
        if (t instanceof ResponseErrorException) {
            errorMessage = ((ResponseErrorException) t).getErrorList().get(0).getDetail();
        }
        return errorMessage;
    }

    public static String getClickBudgetError(Context context, float clickBudget) {
        if (clickBudget < TopAdsConstant.BUDGET_MULTIPLE_BY) {
            return context.getString(R.string.error_top_ads_click_budget_minimum, String.valueOf(TopAdsConstant.BUDGET_MULTIPLE_BY));
        }
        if (clickBudget % TopAdsConstant.BUDGET_MULTIPLE_BY != 0) {
            return context.getString(R.string.error_top_ads_click_budget_multiple_by, String.valueOf(TopAdsConstant.BUDGET_MULTIPLE_BY));
        }
        if (clickBudget > TopAdsConstant.BUDGET_MAX) {
            return context.getString(R.string.error_top_ads_click_budget_max, CurrencyFormatter.formatRupiah(String.valueOf(TopAdsConstant.BUDGET_MAX)));
        }
        return null;
    }

    public static String getDailyBudgetError(Context context, float clickBudget, float dailyBudget) {
        if (dailyBudget <= 0) {
            return context.getString(R.string.error_top_ads_daily_budget_cannot_empyt);
        }
        if (dailyBudget < clickBudget * TopAdsConstant.BUDGET_MIN_MULTIPLE_BY) {
            return context.getString(R.string.error_top_ads_daily_budget_minimal, String.valueOf(TopAdsConstant.BUDGET_MIN_MULTIPLE_BY));
        }
        return null;
    }

}
