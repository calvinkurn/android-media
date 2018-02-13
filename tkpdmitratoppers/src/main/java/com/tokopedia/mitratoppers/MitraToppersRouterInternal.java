package com.tokopedia.mitratoppers;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.mitratoppers.dashboard.MitraToppersDashboardActivity;

/**
 * put the implementation body for TkpdMitraToppers here, instead of
 * in SellerRouterApplication or in CustomerRouterApplication
 * Created by hendry on 17/01/18.
 */

public class MitraToppersRouterInternal {
    public static Intent getMitraToppersActivityIntent(Context context){
        return new Intent(context, MitraToppersDashboardActivity.class);
    }
}
