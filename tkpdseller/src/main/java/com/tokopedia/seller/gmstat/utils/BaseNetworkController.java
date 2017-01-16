package com.tokopedia.seller.gmstat.utils;

import android.content.Context;

import com.google.gson.Gson;

/**
 * Created by normansyahputa on 8/31/16.
 */

public abstract class BaseNetworkController {
    protected Context context;
    protected Gson gson;

    public BaseNetworkController(Context context, Gson gson){
        this.context = context;
        this.gson = gson;
    }
}
