package com.tokopedia.seller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.seller.gmsubscribe.view.activity.GmSubscribeHomeActivity;

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

    public static void goToGMSubscribe(Context context){
        if(context == null)
            return;

        context.startActivity(new Intent(context, GmSubscribeHomeActivity.class));
    }
}
