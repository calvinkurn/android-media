package com.tokopedia.profilecompletion.view.subscriber;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.profile.model.GetUserInfoDomainModel;
import com.tokopedia.profilecompletion.view.listener.GetProfileListener;
import com.tokopedia.profilecompletion.view.presenter.ProfileCompletionContract;

import rx.Subscriber;

/**
 * @author by nisie on 6/19/17.
 */

public class GetUserInfoSubscriber extends Subscriber<GetUserInfoDomainModel> {

    private ProfileCompletionContract.View listener;

    public GetUserInfoSubscriber(ProfileCompletionContract.View view) {
        this.listener = view;
    }


    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        CommonUtils.dumper("NISNIS error" + e.toString());

    }

    @Override
    public void onNext(GetUserInfoDomainModel getUserInfoDomainModel) {
        CommonUtils.dumper("NISNIS sukses");
        listener.onGetUserInfo(getUserInfoDomainModel.getGetUserInfoDomainData());
    }
}
