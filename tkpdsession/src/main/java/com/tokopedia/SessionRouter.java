package com.tokopedia;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.profile.view.subscriber.FollowKolSubscriber;

/**
 * @author by nisie on 10/19/17.
 */

public interface SessionRouter {
    Intent getHomeIntent(Context context);

    BaseDaggerFragment getKolPostFragment(String userId);

    void doFollowKolPost(int id, FollowKolSubscriber followKolPostSubscriber);

    void doUnfollowKolPost(int id, FollowKolSubscriber followKolPostSubscriber);

    Intent getTopProfileIntent(Context context, String userId);
}
