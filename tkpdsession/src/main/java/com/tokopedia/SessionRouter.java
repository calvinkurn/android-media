package com.tokopedia;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.profile.view.subscriber.FollowKolSubscriber;

import okhttp3.Interceptor;

/**
 * @author by nisie on 10/19/17.
 */

public interface SessionRouter {
    Intent getHomeIntent(Context context);

    BaseDaggerFragment getKolPostFragment(String userId);

    void doFollowKolPost(int id, FollowKolSubscriber followKolPostSubscriber);

    void doUnfollowKolPost(int id, FollowKolSubscriber followKolPostSubscriber);

    Intent getTopProfileIntent(Context context, String userId);

    Interceptor getChuckInterceptor();

    Intent getShopPageIntent(Context context, String shopId);

    Intent getSecurityQuestionVerificationIntent(Context context, int userCheckSecurity2,
                                                 String email, String phone);

    Intent getCOTPIntent(Context context, String phoneNumber, int otpType,
                         boolean canUseOtherMethod, String otpMode);

    Intent getCOTPIntent(Context context, String phoneNumber, String email,
                         int otpTypeChangePhoneNumber, boolean canUseOtherMethod, String otpMode);
}
