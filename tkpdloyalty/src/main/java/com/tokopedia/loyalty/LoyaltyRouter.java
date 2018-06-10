package com.tokopedia.loyalty;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.router.loyaltytokopoint.ILoyaltyRouter;

/**
 * @author Aghny A. Putra on 3/4/18
 */

public interface LoyaltyRouter extends ILoyaltyRouter {

    Intent getPromoDetailIntent(Context context, String slug);

}