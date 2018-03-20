package com.tokopedia.wishlist.common.data.interceptor;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;


public class WishListAuthInterceptor extends MojitoInterceptor {

    public WishListAuthInterceptor(Context context,
                                   AbstractionRouter abstractionRouter,
                                   UserSession userSession) {
        super(context, abstractionRouter, userSession);
    }
}
