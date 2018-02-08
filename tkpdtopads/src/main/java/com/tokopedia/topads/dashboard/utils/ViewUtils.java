package com.tokopedia.topads.dashboard.utils;

import android.content.Context;

import com.tokopedia.core.network.retrofit.exception.ResponseErrorException;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.TopAdsConstant;

/**
 * @author normansyahputa on 2/16/17.
 */

public class ViewUtils {

    public static String getErrorMessage(Throwable t) {
        String errorMessage = null;
        if (t instanceof ResponseErrorException) {
            errorMessage = ((ResponseErrorException) t).getErrorList().get(0).getDetail();
        }
        return errorMessage;
    }

    public static String getClickBudgetError(Context context, double clickBudget) {
        if (clickBudget < TopAdsConstant.BUDGET_MULTIPLE_BY) {
            return context.getString(R.string.error_top_ads_click_budget_must_be_filled);
        }
        return getDefaultClickBudgetError(context, clickBudget);
    }

    public static String getKeywordClickBudgetError(Context context, double clickBudget) {
        if (clickBudget < TopAdsConstant.BUDGET_MULTIPLE_BY) {
            return context.getString(R.string.error_top_ads_click_budget_minimum, String.valueOf(TopAdsConstant.BUDGET_MULTIPLE_BY));
        }
        return getDefaultClickBudgetError(context, clickBudget);
    }

    public static String getDefaultClickBudgetError(Context context, double clickBudget) {
        if (clickBudget % TopAdsConstant.BUDGET_MULTIPLE_BY != 0) {
            return context.getString(R.string.error_top_ads_click_budget_multiple_by, String.valueOf(TopAdsConstant.BUDGET_MULTIPLE_BY));
        }
        if (clickBudget > TopAdsConstant.BUDGET_MAX) {
            return context.getString(R.string.error_top_ads_click_budget_max, CurrencyFormatter.formatRupiah(String.valueOf(TopAdsConstant.BUDGET_MAX)));
        }
        return null;
    }

    public static String getDailyBudgetError(Context context, float clickBudget, double dailyBudget) {
        if (dailyBudget <= 0) {
            return context.getString(R.string.error_top_ads_daily_budget_cannot_empyt);
        }
        if (dailyBudget < clickBudget * TopAdsConstant.BUDGET_MIN_MULTIPLE_BY) {
            return context.getString(R.string.error_top_ads_daily_budget_minimal, String.valueOf(TopAdsConstant.BUDGET_MIN_MULTIPLE_BY));
        }
        return null;
    }

}
