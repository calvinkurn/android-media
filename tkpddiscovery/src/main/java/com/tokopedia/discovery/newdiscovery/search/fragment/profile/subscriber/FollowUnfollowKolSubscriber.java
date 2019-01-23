package com.tokopedia.discovery.newdiscovery.search.fragment.profile.subscriber;

import com.tokopedia.discovery.newdiscovery.search.fragment.profile.ProfileContract;
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.listener.FollowActionListener;
import com.tokopedia.kolcommon.model.FollowResponseModel;

import rx.Subscriber;

/**
 * @author by nisie on 11/3/17.
 */

public class FollowUnfollowKolSubscriber extends Subscriber<FollowResponseModel> {
    protected final ProfileContract.View view;
    protected final int adapterPosition;
    protected final Boolean followStatus;
    private final FollowActionListener followActionListener;

    public FollowUnfollowKolSubscriber(int adapterPosition, ProfileContract.View view,
                                       boolean followStatus,
                                       FollowActionListener followActionListener) {
        this.view = view;
        this.adapterPosition = adapterPosition;
        this.followStatus = followStatus;
        this.followActionListener = followActionListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        followActionListener.onErrorToggleFollow(adapterPosition, e.getMessage());
    }

    @Override
    public void onNext(FollowResponseModel followResponseModel) {
        if (followResponseModel.isSuccess()) {
            followActionListener.onSuccessToggleFollow(adapterPosition, !followStatus);
        } else {
            followActionListener.onErrorToggleFollow(adapterPosition, followResponseModel.getErrorMessage());
        }
    }
}
