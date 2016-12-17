package com.tokopedia.seller;

import android.app.Activity;

/**
 * Created by normansyahputa on 12/15/16.
 */

public class Router {

    public static void goToHome(Activity activity){
        if(activity == null)
            return;

        if(activity.getApplication() instanceof SellerModuleRouter){
            ((SellerModuleRouter)activity.getApplication()).goToHome(activity);
        }
    }
}
