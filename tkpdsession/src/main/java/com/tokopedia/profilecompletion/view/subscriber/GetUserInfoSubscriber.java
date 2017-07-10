package com.tokopedia.profilecompletion.view.subscriber;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.profile.model.GetUserInfoDomainModel;
import com.tokopedia.profilecompletion.view.listener.GetProfileListener;

import rx.Subscriber;

/**
 * @author by nisie on 6/19/17.
 */

public class GetUserInfoSubscriber extends Subscriber<GetUserInfoDomainModel> {

    private GetProfileListener listener;

    public GetUserInfoSubscriber(GetProfileListener listener) {
        this.listener = listener;
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
