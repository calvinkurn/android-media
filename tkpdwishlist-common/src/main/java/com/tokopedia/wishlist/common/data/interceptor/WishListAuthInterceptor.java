package com.tokopedia.wishlist.common.data.interceptor;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;


public class WishListAuthInterceptor extends TkpdAuthInterceptor {

    public WishListAuthInterceptor(Context context,
                                   AbstractionRouter abstractionRouter,
                                   UserSession userSession) {
        super(context, abstractionRouter, userSession);
    }
}
